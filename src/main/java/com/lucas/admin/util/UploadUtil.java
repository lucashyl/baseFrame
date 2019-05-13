package com.lucas.admin.util;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.xiaoleilu.hutool.date.DateUtil;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.Random;


public class UploadUtil {

    private volatile static Client client;

    /**
     * 单例获取 jersey Client
     * @return client
     */
    public static Client getClient(){
        if (client == null) {
            synchronized (Client.class) {
                if (client == null) {
                    client = new Client();
                }
            }
        }
        return client;
    }

    /**
     * 上传文件
     * @param file 文件
     * @return serverPath 文件路径
     */
    public static String uploadFile(MultipartFile file, String serverPath){
        //图片名称生成策略---采用时间格式(精确到毫秒)并追加随机3位(10以内)数字
        //精确到毫秒
        String picName = DateUtil.format(new Date(),"yyyyMMddHHmmss");
        //随机再生成3位 10以内的数
        Random r = new Random();
        for(int i=0;i<3;i++){
            picName += r.nextInt(10);
        }
        //获取扩展名
        String originalFilename = file.getOriginalFilename();
        String ext = FilenameUtils.getExtension(originalFilename);
        //相对路径  图片服务器里的文件路径
        String path = "/upload/" + picName + "." + ext;
        //全路径 访问路径
        String url = serverPath + path;
        //jersey 发送另一台Tomcat(可读写的)
        Client client = getClient();
        WebResource resource = client.resource(url);

        try {
            resource.put(String.class, file.getBytes());
        } catch (UniformInterfaceException e) {
            path = "err";
            e.printStackTrace();
        } catch (ClientHandlerException e) {
            path = "err";
            e.printStackTrace();
        } catch (IOException e) {
            path = "err";
            e.printStackTrace();
        }

        return path;
    }

    /**
     * 删除文件
     * @param path 文件路径
     */
    public static void deleteFile(String path){
        Client client = getClient();
        WebResource resource = client.resource(path);
        try {
            resource.delete();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}