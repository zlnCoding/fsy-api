package fsy.api.service;

import com.alibaba.fastjson.JSONObject;
import fsy.api.dao.IUpOrDownloadDao;
import fsy.interfaces.IUpOrDownloadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 描述:
 * 上传下载接口service实现类
 *
 * @auth zln
 * @create 2017-12-05 9:46
 */
@Service("upOrDownService")
@com.alibaba.dubbo.config.annotation.Service(interfaceClass = fsy.interfaces.IUpOrDownloadService.class, protocol = {"dubbo"})
public class UpOrDownloadServiceImpl implements IUpOrDownloadService {

    @Autowired
    public IUpOrDownloadDao upOrDownloadDao;

    /**
     * @param upTime           上传时间
     * @param type             类型 0图片，1音频，2 视频 3 其他
     * @param upType           0 pc文件上传，1 手机录音 2，手机录像 3 手机照片 4 现场录音 5实名身份证
     * @param upstatus         状态 0不可用，1可用
     * @param desc             描述
     * @param userId           用户id
     * @param originalFilename 文件名
     * @param fileSize         文件大小
     * @param uploadPath       文件存储路径
     * @return
     */
    @Override
    public int saveUploadInfo(String upTime, Integer type, Integer upType, Integer upstatus, String desc, Integer userId, String originalFilename, long fileSize, String uploadPath) {
        JSONObject map = new JSONObject();
        map.put("upload_date", upTime);
        map.put("description", desc);
        map.put("type", type);
        map.put("ftp_status", upType);
        map.put("clientUserId", userId);
        map.put("upload_status", upstatus);
        map.put("original_name", originalFilename);
        map.put("upload_size", fileSize);
        map.put("upload_url", uploadPath);
        return upOrDownloadDao.saveUploadClient(map);
    }

    @Override
    public List<JSONObject> getUploadListByUserId(Integer userId,Integer pageNum,Integer type) {
        StringBuffer stringBuffer = new StringBuffer("select id,type,original_name,upload_date,upload_url_min from upload_client where clientUserId=? and ftp_status !=5 and type=? limit ? , 10");
        return upOrDownloadDao.getUploadListByUserId(stringBuffer,userId,pageNum,type);
    }

    @Override
    public JSONObject getUploadById(Integer uploadId) {
        StringBuffer stringBuffer = new StringBuffer("select upload_url from upload_client where id=?");
        return upOrDownloadDao.getUploadById(stringBuffer,uploadId);
    }

    @Override
    public int getUploadListCount(Integer userId,Integer type) {
        StringBuffer stringBuffer = new StringBuffer("select *  from upload_client where clientUserId=? and type=? ");
        return upOrDownloadDao.getUploadListCount(stringBuffer,userId,type);
    }
}
