package fsy.utils;

import cn.tsa.timestamp.client.TSAClient;
import cn.tsa.utils.FileUtil;
import cn.tsa.utils.HashUtil;

import java.io.File;

/**
 * 描述:
 * tsa认证
 *
 * @auth zln
 * @create 2017-12-17 14:13
 */
public class TSAUtils {

    public static File tsa(String path, String fileName) {
        try {
            // 构建时间戳类对象 时间戳服务测试接口：http://test1.tsa.cn/tsa 用户名：tsademo 密码：tsademo
            TSAClient tsClient = new TSAClient("http://test1.tsa.cn/tsa",
                    "tsademo", "tsademo");
            // 本地读取需要申请时间戳的文件并计算SHA-1算法的HashCode
            byte[] hash = HashUtil.getHash(FileUtil.readFile(path+Const.OSSeparator()+fileName));
            // 获取可信时间戳信息
            byte[] tsaToken = tsClient.getTimeStampToken(hash);
            //将时间戳存储至本地截止  注意：存储至数据库中需进行base64编码
            String tsaPath =path+fileName.substring(0,fileName.lastIndexOf("."))+".tsa";
            FileUtil.writeFile(tsaPath, tsaToken);
            return new File(tsaPath);
            // 获取并打印可信时间戳签名时间
            //System.out.println("时间戳签名信息Base64编码形式：" + Base64.encode(tsaToken));
        } catch (Exception e) {
            return null;
        }
    }

    public static void main(String[] args) {
        System.out.println(tsa("C:\\Users\\Public\\Pictures\\Sample Pictures\\","菊花.jpg"));
    }
}
