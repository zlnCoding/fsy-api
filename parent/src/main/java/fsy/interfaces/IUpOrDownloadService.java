package fsy.interfaces;

import com.alibaba.fastjson.JSONObject;

import java.util.List;


/**
 * Created by zln on 2017/12/5.
 */
public interface IUpOrDownloadService {

    /**
     *
     * @param upTime 上传时间
     * @param type  类型 0图片，1音频，2 视频 3 其他
     * @param upType  0 pc文件上传，1 手机录音 2，手机录像 3 手机照片 4 现场录音 5实名身份证
     * @param upstatus 状态 0不可用，1可用
     * @param desc 描述
     * @param userId 用户id
     * @param originalFilename 文件名
     * @param fileSize 文件大小
     * @param uploadPath 文件存储路径
     * @return
     */
    int saveUploadInfo(String upTime, Integer type, Integer upType, Integer upstatus, String desc, Integer userId, String originalFilename, long fileSize, String uploadPath);

    /**
     * 通过用户id 获取用户上传的资源列表
     * @param userId
     * @return
     */
    List<JSONObject> getUploadListByUserId(Integer userId,Integer pageNum,Integer type);

    /**
     * 通过上传id 获取信息
     * @param uploadId
     * @return
     */

    JSONObject getUploadById(Integer uploadId);

    /**
     * 获取用户上传列表总数
     * @param userId
     * @return
     */

    int getUploadListCount(Integer userId,Integer type);
}
