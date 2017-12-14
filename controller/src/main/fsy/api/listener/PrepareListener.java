package fsy.api.listener;/**
 * Created by zln on 2017/12/13.
 */

import fsy.utils.Const;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.File;

/**
 * 描述:
 * 前置准备监听器
 *
 * @auth zln
 * @create 2017-12-13 13:21
 */
public class PrepareListener implements ServletContextListener {

    /**
     * tomcat启动时创建临时文件夹
     * @param sce
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        File tempfile=null;
        if (Const.OSType().contains("windows")) {
            tempfile = new File(Const.WINDOWS_FILE_PATH);

        }else {
            tempfile = new File(Const.LINUX_FILE_PATH);
        }
        if (!tempfile.exists()) {
            tempfile.mkdir();
        }
    }

    /**
     * tomcat关闭时删除所有临时文件
     * @param sce
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        File tempfile =null;
        if (Const.OSType().contains("windows")) {
            tempfile = new File(Const.WINDOWS_FILE_PATH);

        }else {
            tempfile = new File(Const.LINUX_FILE_PATH);
        }
        if (!tempfile.exists()) {
            tempfile.mkdir();
        }
        deleteAllFiles(tempfile);
    }

    //递归删除
    public static void deleteAllFiles(File file){
        if(file == null || !file.exists())
            return ;
        if(file.isDirectory()){
            File[] files = file.listFiles();
            if(files != null){
                for(File f : files)
                    deleteAllFiles(f);
            }
        }
        file.delete();
    }
}
