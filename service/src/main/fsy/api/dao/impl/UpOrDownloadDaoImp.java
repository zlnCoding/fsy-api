package fsy.api.dao.impl;

import com.alibaba.fastjson.JSONObject;
import fsy.api.dao.BaseDao;
import fsy.api.dao.IUpOrDownloadDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by zln on 2017/12/5.
 */
@Repository("upOrDownloadDao")
public class UpOrDownloadDaoImp extends BaseDao implements IUpOrDownloadDao {

    @Override
    public int saveUploadClient(JSONObject args) {
        return this.insert("upload_client",args);
    }

    @Override
    public List<JSONObject> getUploadListByUserId(StringBuffer sql, Integer userId,Integer pageNum,Integer type) {
        List<JSONObject> list = this.queryForJsonList(sql.toString(), userId,pageNum,type);
        return list;
    }

    @Override
    public JSONObject getUploadById(StringBuffer sql, Integer uploadId) {
        return this.queryForJsonObject(sql.toString(),uploadId);
    }

    @Override
    public int getUploadListCount(StringBuffer sql, Integer userId,Integer type) {
        return this.queryForJsonList(sql.toString(),userId,type).size();
    }

}
