package fsy.utils;/**
 * Created by zln on 2017/12/4.
 */

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.*;

import java.net.URL;
import java.sql.Date;

/**
 * 描述:
 * 上传文件
 *
 * @auth zln
 * @create 2017-12-04 14:47
 */
public class OSSUtils {

    public static String uploadFile(String fileName, String filePath, int userId) {
        String endpoint = "https://oss-cn-beijing.aliyuncs.com";
        String accessKeyId = "fgcLZqV8XQeM6YN2";
        String accessKeySecret = "LgeSczdMZBCRgk41wyZUuxvY1Bksna";
        String bucketName = "miaosos-method";

        //存放路径
        StringBuffer OSSpath = new StringBuffer("upload/");
        OSSpath.append(userId).append("/").append(Const.dateFormat("yyyy-MM-dd", System.currentTimeMillis()))
                .append("/").append(fileName);
        // 创建OSSClient实例
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        // 设置断点续传请求
        UploadFileRequest uploadFileRequest = new UploadFileRequest(bucketName, OSSpath.toString());
        // 指定上传的本地文件
        uploadFileRequest.setUploadFile(filePath);
        // 指定上传并发线程数
        uploadFileRequest.setTaskNum(Runtime.getRuntime().availableProcessors());
        // 指定上传的分片大小
        uploadFileRequest.setPartSize(5 * 1024 * 1024);
        // 开启断点续传
        uploadFileRequest.setEnableCheckpoint(true);
        // 断点续传上传
        try {
            ossClient.uploadFile(uploadFileRequest);
            // 关闭client
            ossClient.shutdown();
            return OSSpath.toString();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return "fail";
        }

    }

    /**
     * 生成访问url
     * 详情请访问 :https://help.aliyun.com/document_detail/31951.html?spm=5176.doc31952.6.845.8bVt7o 与
     * https://help.aliyun.com/document_detail/31952.html?spm=5176.doc31951.6.846.aRCwGN
     *
     * @param fileName
     * @return
     */
    public static URL downloadFile(String fileName) {
        String endpoint = "https://oss-cn-beijing.aliyuncs.com";
        String accessKeyId = "LTAIT4QxcAPpUh18";
        String accessKeySecret = "G2UG3AqMQSsXwlGXdJisG15QrDEglR";
        String bucketName = "miaosos-method";

        // 创建OSSClient实例
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        //url有效时间为一小时
        URL url = ossClient.generatePresignedUrl(bucketName, fileName, new Date(System.currentTimeMillis() + 1000 * 60 * 60));
        ossClient.shutdown();
        return url;
    }

    public static void main(String[] args) {
        URL url = downloadFile("upload/32/2017-12-05/psb111.gif");
        System.out.println(url);

    }

}
