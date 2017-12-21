package fsy.api.dao.impl;

import com.alibaba.fastjson.JSONObject;
import fsy.api.dao.BaseDao;
import fsy.api.dao.IUpOrDownloadDao;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zln on 2017/12/5.
 */
@Repository("upOrDownloadDao")
public class UpOrDownloadDaoImp extends BaseDao implements IUpOrDownloadDao {

    @Override
    public int saveUploadClient(JSONObject args) {
        try {
            return this.insert("upload_client", args);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }


    }

    @Override
    public List<JSONObject> getUploadListByUserId(StringBuffer sql, Integer userId,Integer type,Integer pageNum) {
        try {
            return  this.queryForJsonList(sql.toString(), userId,type,pageNum);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<JSONObject>();
        }
    }

    @Override
    public JSONObject getUploadById(StringBuffer sql, Integer uploadId) {
        try {
            return this.queryForJsonObject(sql.toString(),uploadId);
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONObject();
        }
    }

    @Override
    public int getUploadListCount(StringBuffer sql, Integer userId,Integer type) {
        try {
            return this.queryForJsonList(sql.toString(),userId,type).size();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int saveApplyAttest(String upload_log, JSONObject data) {
        try {
            return this.insert(upload_log,data);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public List<JSONObject> getApplyAttestList(StringBuffer sql, Integer userId, Integer pageNum) {
        try {
            return this.queryForJsonList(sql.toString(),userId,pageNum);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public int getApplyListCount(StringBuffer stringBuffer, Integer userId) {
        try {
            return this.queryForJsonList(stringBuffer.toString(),userId).size();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int updateFileStatus(StringBuffer stringBuffer, Integer uploadId) {
        try {
            return this.update(stringBuffer.toString(),uploadId);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int getUploadByName(StringBuffer stringBuffer, Integer userId, String originalFilename) {
        try {
        return this.queryForJsonList(stringBuffer.toString(),userId,originalFilename).size();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

}
