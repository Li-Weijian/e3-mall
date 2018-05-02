package cn.e3mall.controller;


import cn.e3.commom.utils.FastDFSClient;
import cn.e3.commom.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Controller
public class PictureController {

    @Value(value = "${IMAGE_SERVICE_ADDR}")
    private String IMAGE_SERVICE_ADDR;


    @RequestMapping(value = "/pic/upload", produces = MediaType.TEXT_PLAIN_VALUE+";charset=utf-8")
    @ResponseBody
    public String picUpload(MultipartFile uploadFile){
//        1、接收页面传递的图片信息uploadFile
//        2、把图片上传到图片服务器。使用封装的工具类实现。需要取文件的内容和扩展名。
        try {
            FastDFSClient fastDFSClient = new FastDFSClient("F:\\Code\\e3parent\\e3-manager-web\\src\\main\\resources\\conf\\client.properties");
            String originalFilename = uploadFile.getOriginalFilename();  //获取全路径
            String substring = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
//        3、图片服务器返回图片的url
            String path = fastDFSClient.uploadFile(uploadFile.getBytes(), substring);
//        4、将图片的url补充完整，返回一个完整的url。
            String url = IMAGE_SERVICE_ADDR + path;
//        5、把返回结果封装到一个Map对象中返回。
            Map map = new HashMap();
            map.put("error",0);
            map.put("url",url);
            String json = JsonUtils.objectToJson(map);
            return json;
        } catch (Exception e) {
            e.printStackTrace();
            Map map = new HashMap();
            map.put("error",1);
            map.put("message","图片上传失败");
            String json = JsonUtils.objectToJson(map);
            return json;
        }
    }
}
