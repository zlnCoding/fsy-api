package fsy.api.service;

import com.alibaba.fastjson.JSONObject;
import fsy.api.dao.IUpOrDownloadDao;
import fsy.interfaces.IUpOrDownloadService;
import fsy.utils.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;

/**
 * 描述:
 * 上传下载接口service实现类
 *
 * @auth zln
 * @create 2017-12-05 9:46
 */
@Transactional
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
     * @param uploadPathMin       缩略图存储路径
     * @return
     */
    @Override
    public int saveUploadInfo(String upTime, Integer type, Integer upType, Integer upstatus, String desc, Integer userId, String originalFilename, long fileSize, String uploadPath,String uploadPathMin) {
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
        map.put("upload_url_min", uploadPathMin);
        return upOrDownloadDao.saveUploadClient(map);
    }

    @Override
    public List<JSONObject> getUploadListByUserId(Integer userId,Integer type,Integer pageNum) {
        StringBuffer stringBuffer = new StringBuffer("select id,type,original_name,upload_date,upload_url_min from upload_client where clientUserId=? and ftp_status !=5 and upload_status=1 ");
        if (type != null)  stringBuffer.append(" and type=? ");
        else {
            type= 0;
            stringBuffer.append(" and type>= ? ");
        }
        stringBuffer.append(" limit ? , 30");
        return upOrDownloadDao.getUploadListByUserId(stringBuffer,userId,type,pageNum);
    }

    @Override
    public JSONObject getUploadById(Integer uploadId) {
        StringBuffer stringBuffer = new StringBuffer("select upload_url from upload_client where id=?");
        return upOrDownloadDao.getUploadById(stringBuffer,uploadId);
    }

    @Override
    public int getUploadListCount(Integer userId,Integer type) {
        StringBuffer stringBuffer = new StringBuffer("select *  from upload_client where clientUserId=? and upload_status=1 ");
        if (type != null)  stringBuffer.append(" and type=? ");
        else {
            type= 0;
            stringBuffer.append(" and type>= ? ");
        }
        return upOrDownloadDao.getUploadListCount(stringBuffer,userId,type);
    }

    @Override
    public int applyAttest( Integer uploadId, Integer userId,Integer log_type) {
        JSONObject data = new JSONObject();
        data.put("upload_id",uploadId);
        data.put("clientUserId",userId);
        data.put("log_time", Const.dateFormat(new Date(System.currentTimeMillis())));
        data.put("log_type", log_type);
        return upOrDownloadDao.saveApplyAttest("upload_log", data);
    }

    @Override
    public List<JSONObject> applyAttestList(Integer userId, Integer pageNum) {
        StringBuffer sql = new StringBuffer("select * from upload_log where clientUserId=?  order by log_time desc limit ?,30");
        return upOrDownloadDao.getApplyAttestList(sql,userId,pageNum);
    }

    @Override
    public int getApplyListCount(Integer userId) {
        StringBuffer stringBuffer = new StringBuffer("select *  from upload_log where clientUserId=? ");
        return upOrDownloadDao.getApplyListCount(stringBuffer,userId);
    }

    @Override
    public int deleteFile(Integer uploadId) {
        StringBuffer stringBuffer = new StringBuffer("update  upload_client set upload_status=0 where id=? ");
        return upOrDownloadDao.updateFileStatus(stringBuffer,uploadId);
    }

    @Override
    public int getUploadByName(String originalFilename, Integer userId) {
        StringBuffer stringBuffer = new StringBuffer("select *  from upload_client where clientUserId=? and original_name=?");
        return upOrDownloadDao.getUploadByName(stringBuffer,userId,originalFilename);
    }
}
