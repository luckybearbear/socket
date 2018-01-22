package com.util;


import java.io.InputStream;
import java.util.Properties;

public class ReadProperty {
    public   String propertyRead(String propertyName,String key) {
        Properties prop = new Properties();
        try{
            //读取属性文件Client.properties
            InputStream inStream=getClass().getResourceAsStream("../../"+propertyName+".properties");//获取配置文件输入流
            prop.load(inStream);     ///加载属性列表
            inStream.close();
            return prop.getProperty(key);
        }
        catch(Exception e){
            System.out.println(e);
        }
        return null;
    }


}