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
    List<JSONObject> getUploadListByUserId(StringBuffer sql, Integer userId,Integer pageNum,Integer type);

    /**
     * 通upload id 获取信息
     * @param stringBuffer
     * @param uploadId
     * @return
     */
    JSONObject getUploadById(StringBuffer stringBuffer, Integer uploadId);

    /**
     * 获取用户上传列表总数
     * @param stringBuffer
     * @param userId
     * @return
     */
    int getUploadListCount(StringBuffer stringBuffer, Integer userId,Integer type);
}
