package cn.e3mall.fastDFS;

import cn.e3.commom.utils.FastDFSClient;
import org.csource.common.MyException;
import org.csource.fastdfs.*;
import org.junit.Test;

import java.io.IOException;

public class TestFastDFS {

    //原始上传方式
    @Test
    public void testUpload() {
//    1、加载配置文件，配置文件中的内容就是tracker服务的地址。
//    配置文件内容：tracker_server=192.168.25.133:22122
      try {
            ClientGlobal.init("F:\\Code\\e3parent\\e3-manager-web\\src\\main\\resources\\conf\\client.properties");
//    2、创建一个TrackerClient对象。直接new一个。
          TrackerClient trackerClient = new TrackerClient();
//    3、使用TrackerClient对象创建连接，获得一个TrackerServer对象。
          TrackerServer connection = trackerClient.getConnection();
//    4、创建一个StorageServer的引用，值为null
          StorageServer storageServer = null;
//    5、创建一个StorageClient对象，需要两个参数TrackerServer对象、StorageServer的引用
          StorageClient client = new StorageClient(connection,storageServer);
//    6、使用StorageClient对象上传图片。
          String[] upload_file = client.upload_file("F:\\黑马 北京JavaEE就业班32期 20160717-20161206精心整理\\项目二：宜立方商城(80-93天）\\e3商城_day01\\黑马32期\\01.教案-3.0\\01.参考资料\\广告图片\\9a80e2d06170b6bb01046233ede701b3.jpg", "jpg", null);
//    7、返回数组。包含组名和图片的路径。
          for (String file: upload_file) {
              System.out.println(file);
          }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }

    }

    //使用工具类进行文件上传
    @Test
    public void testFastDFSClient(){

        try {
            FastDFSClient fastDFSClient = new FastDFSClient("F:\\Code\\e3parent\\e3-manager-web\\src\\main\\resources\\conf\\client.properties");
            String s = fastDFSClient.uploadFile("F:\\黑马 北京JavaEE就业班32期 20160717-20161206精心整理\\项目二：宜立方商城(80-93天）\\e3商城_day01\\黑马32期\\01.教案-3.0\\01.参考资料\\广告图片\\9a80e2d06170b6bb01046233ede701b3.jpg");
            System.out.println(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
