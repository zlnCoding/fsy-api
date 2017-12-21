package fsy.api.dao;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * Created by zln on 2017/12/5.
 */
public interface IUpOrDownloadDao {
    int saveUploadClient(JSONObject map);

    /**
     * 通过userid查询用户上传列表
     *
     * @param sql
     * @param userId
     * @return
     */
    List<JSONObject> getUploadListByUserId(StringBuffer sql, Integer userId, Integer type, Integer pageNum);

    /**
     * 通upload id 获取信息
     *
     * @param stringBuffer
     * @param uploadId
     * @return
     */
    JSONObject getUploadById(StringBuffer stringBuffer, Integer uploadId);

    /**
     * 获取用户上传列表总数
     *
     * @param stringBuffer
     * @param userId
     * @return
     */
    int getUploadListCount(StringBuffer stringBuffer, Integer userId, Integer type);

    /**
     * 申请出证
     *
     * @param upload_log
     *
     * @return
     */
    int saveApplyAttest(String upload_log, JSONObject data);

    /**
     * 获取申请列表
     * @param sql
     * @param userId
     * @param pageNum
     * @return
     */
    List<JSONObject> getApplyAttestList(StringBuffer sql, Integer userId, Integer pageNum);

    /**
     * 获取申请列表总条数
     * @param stringBuffer
     * @param userId
     * @return
     */
    int getApplyListCount(StringBuffer stringBuffer, Integer userId);

    /**
     * 伪删除,修改upload_client中的 upload_status为0
     * @param stringBuffer
     * @param uploadId
     * @return
     */
    int updateFileStatus(StringBuffer stringBuffer, Integer uploadId);

    /**
     * 查询用户下是否有重复的文件名
     * @param stringBuffer
     * @param userId
     * @param originalFilename
     * @return
     */
    int getUploadByName(StringBuffer stringBuffer, Integer userId, String originalFilename);
}
