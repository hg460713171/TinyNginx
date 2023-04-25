package com.h.system.tinynignx.util;


import java.io.File;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

/**
 * 定义一个单例的 资源管理器
 *
 */
public class ResourcesService {

    private HashMap<String, String> urlMap = new HashMap<String, String>();

    private HashMap<String, String> upStreamUrl = new HashMap<String, String>();
    private HashMap<String, String> propertiesMap = new HashMap<String, String>();
    private static volatile ResourcesService INSTANCE;

    private ResourcesService() {
        //先读取默认配置文件
        //再读取外部配置文件
        Properties prop = new Properties();
        try{
            prop.load(ResourcesService.class.getClassLoader().getResourceAsStream(Constant.CONFIG_FILE_NAME));
        }catch (Exception e){
            System.err.println("未读取到默认配置文件");
        }
        setConfig(prop);
        prop.clear();
        String path = System.getProperty("java.class.path");
        int firstIndex = path.lastIndexOf(System.getProperty("path.separator")) + 1;
        int lastIndex = path.lastIndexOf(File.separator) + 1;
        path = path.substring(firstIndex, lastIndex)+ Constant.CONFIG_FILE_NAME;
        InputStream in = ResourcesService.class.getClassLoader().getResourceAsStream(path);
        try{
            prop.load(in);
        }catch (Exception e){
            System.err.println("未读取到外部配置文件");
        }
        setConfig(prop);
    }

    private void setConfig(Properties prop) {
        Enumeration<Object> keys = prop.keys();
        while (keys.hasMoreElements()){
            String key = (String) keys.nextElement();

            if(key.startsWith(Constant.WEB_PATH_PREFIX)){
                String url = key.substring(16);
                url = url.replaceAll("\\.", "/");
                urlMap.put("/"+url, prop.getProperty(key));
            }else{
                propertiesMap.put(key, prop.getProperty(key));
            }
        }
    }

    public static ResourcesService getInstance() {
        if (INSTANCE == null) {
            synchronized(ResourcesService.class){
                if(INSTANCE == null){
                    INSTANCE = new ResourcesService();
                }
            }
        }
        return INSTANCE;
    }

    public String getPath(String url){
        if(urlMap.containsKey(url)){
            return urlMap.get(url);
        }else
            return null;
    }

    public String getProperties(String key){
        if(propertiesMap.containsKey(key)){
            return propertiesMap.get(key);
        }else
            return null;
    }

    public HashMap<String, String> getUrlMap() {
        return urlMap;
    }

    public void setUrlMap(HashMap<String, String> urlMap) {
        this.urlMap = urlMap;
    }

    public HashMap<String, String> getUpStreamUrl() {
        return upStreamUrl;
    }

    public void setUpStreamUrl(HashMap<String, String> upStreamUrl) {
        this.upStreamUrl = upStreamUrl;
    }
}
