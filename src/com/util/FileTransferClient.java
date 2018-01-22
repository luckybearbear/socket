package com.util;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;

/**
 * 文件传输Client端
 */
public class FileTransferClient extends Socket {
    private Socket client;

    private FileInputStream fis;

    private DataOutputStream dos;
    private ReadProperty r =new ReadProperty();

    /**
     * 构造函数<br/>
     * 与服务器建立连接
     * @throws Exception
     */
    public FileTransferClient(String host,int port) throws Exception {
        super(host,port);
        this.client = this;
        System.out.println("Cliect[port:" + client.getLocalPort() + "] 成功连接服务端");
    }
    /**
     * 连接服务器
     * */
    /**
     * 向服务端传输文件
     * @throws Exception
     */
    public void sendFile() throws Exception {
        try {
            String dir = r.propertyRead("Client","dir");//读取配置文件key = dir
            String fileName= ReadDirector.getFiles( dir);//需要上传的文件的路径目录下的创建日期最早的文件名
            System.out.println(dir+"/"+fileName);
            File file = new File(dir+"/"+fileName);
            if(file.exists()) {
                fis = new FileInputStream(file);
                dos = new DataOutputStream(client.getOutputStream());
                // 文件名和长度
                dos.writeUTF(file.getName());
                dos.flush();
                dos.writeLong(file.length());
                dos.flush();
                // 开始传输文件
                System.out.println("======== 开始传输文件 ========");
                byte[] bytes = new byte[1024];
                int length = 0;
                long progress = 0;
                while((length = fis.read(bytes, 0, bytes.length)) != -1) {
                    dos.write(bytes, 0, length);
                    dos.flush();
                    progress += length;
                    System.out.print("| " + (100*progress/file.length()) + "% |");
                }
                System.out.println();
                System.out.println("======== 文件传输成功 ========");
            }
            else{
                System.out.println("文件不存在");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(fis != null)
                fis.close();
            if(dos != null)
                dos.close();
            client.close();
        }
    }

    /**
     * 入口
     * @param args
     */
    public static void main(String[] args) {
        try {
            ReadProperty r = new ReadProperty();
            String host = r.propertyRead("Client","SERVER_IP");
            int port = Integer.parseInt(r.propertyRead("Client","SERVER_PORT"));
            FileTransferClient client = new FileTransferClient(host,port); // 启动客户端连接
            client.sendFile(); // 传输文件
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}