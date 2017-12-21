package fsy.utils;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.fastjson.JSONObject;
import org.aspectj.lang.JoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 描述:
 * 通用工具类
 *
 * @auth zln
 * @create 2017-11-28 15:47
 */
public class Const {

    public static final String APP_KEY = "7EECF7A8337C4C8489674C6F3DC794F3";

    // C:\temefile
    public static final String WINDOWS_FILE_PATH = new StringBuffer("C:").append(OSSeparator()).append("tempfile").toString();
    //   /usr/local/tempfile
    public static final String LINUX_FILE_PATH = new StringBuffer(OSSeparator()).append("usr").append(OSSeparator()).append("local").append(OSSeparator()).append("tempfile").toString();

    /**
     * 日志
     */
    public static Logger logger = LoggerFactory.getLogger(Const.class);

    public static String Log(JoinPoint jp) {
        StringBuffer stringBuffer = new StringBuffer("调用方法:")
                //获取调用方法包名+类名
                .append(jp.getTarget().getClass().getName())
                .append(".")
                //获取调用方法名
                .append(jp.getSignature().getName());

        StringBuffer args = new StringBuffer();
        //获取参数
        Object[] objects = jp.getArgs();
        if (objects.length > 0) {
            for (int i = 0; i < objects.length; i++) {
                args.append("参数")
                        .append(i)
                        .append(": ")
                        .append(objects[i] == null ? "" : objects[i].toString());
            }
        }
        return stringBuffer.append(args).toString();

    }

    /**
     * 校验
     */
    public static boolean isCheck(Map paramMap, String sign, String Encode) throws NoSuchAlgorithmException {

        if (sign == null && "".equals(sign.trim())) return false;
        if (Encode == null && "".equals(Encode.trim())) return false;
        if (paramMap == null && paramMap.size() == 0) return false;

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.delete(0, stringBuffer.length());

        List<String> list = new ArrayList<>();
        //将参数转换成key=value格式的list方便排序
        Iterator<Map.Entry<String, String>> iterator = paramMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> next = iterator.next();
            list.add(stringBuffer.append(next.getKey()).append(next.getValue()).toString());
            stringBuffer.delete(0, stringBuffer.length());
        }

        //排序并将结果放进stringbuffer中
        List<String> sort = CollectionUtils.sort(list);
        for (int i = 0; i < sort.size(); i++) {
            stringBuffer.append(sort.get(i));
        }

        //MD5加密
        String myEncode = Encrypt.md5(stringBuffer.append("sign").append(APP_KEY).toString());
        byte[] bytes = DatatypeConverter.parseBase64Binary(myEncode);
        //比较
        if (myEncode.equals(Encode)) return true;
        return false;
    }

    /**
     * 时间格式化
     *
     * @param date
     * @return
     */
    public static String dateFormat(Date date) {
        if (date == null) return null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    /**
     * 获取当前时间 自定义时间格式,如:
     * "yyyy-MM-dd HH:mm:ss
     * yyyymmdd
     *
     * @param format
     * @return
     */
    public static String dateFormat(String format, long time) {
        if (format == null) return null;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(time));
    }

    /**
     * 返回成功信息
     *
     * @param msg
     */
    public static JSONObject returnSuccess(String msg) {
        Map<String, Object> map = new HashMap<>();
        map.put("result", 1);
        map.put("msg", msg);
        return new JSONObject(map);
    }

    /**
     * 返回失败信息
     *
     * @param msg
     */
    public static JSONObject returnFail(String msg) {
        Map<String, Object> map = new HashMap<>();
        map.put("result", 0);
        map.put("msg", msg);
        return new JSONObject(map);
    }


    /**
     * 判断多个参数是否为null
     *
     * @param args
     * @return
     */
    public static boolean isNotNull(Object... args) {
        for (Object arg : args) {
            if (arg == null) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取当前系统
     * @return
     */
    public static String OSType() {
        String property = System.getProperties().getProperty("os.name").toLowerCase();
        return property;
    }

    /**
     * 获取当前系统文件分割符
     * @return
     */
    public static String OSSeparator() {
        String property = System.getProperties().getProperty("file.separator");
        return property;
    }

    /**
     * nullToEmptyString
     *
     * @param arg
     * @return
     */
    public static Object nullToEmptyString(Object arg) {
        return arg == null ? "" : arg;
    }

    public static File fileSuffix(String filename) throws ParseException {
        String date = dateFormat("yyyyMMddHHmmss",System.currentTimeMillis());

        StringBuffer prefix = new StringBuffer(filename.substring(0,filename.lastIndexOf(".")));
        prefix.append(date);
        prefix.append(filename.substring(filename.lastIndexOf(".")));
        File file = new File(prefix.toString());
        return file;
    }
    public static void main(String[] args) throws ParseException {
        File file = fileSuffix("D://aaaaa.adf");
        System.out.println(file.getPath());
    }
}
