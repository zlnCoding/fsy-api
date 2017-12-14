package fsy.api.dao.impl;

import com.alibaba.fastjson.JSONObject;
import fsy.api.dao.BaseDao;
import fsy.api.dao.IUserDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 描述:
 * User功能数据访问类
 *
 * @auth zln
 * @create 2017-12-03 15:20
 */
@Repository("userDao")
public class UserDaoImpl extends BaseDao implements IUserDao {

    public JSONObject getUserInfoByPhone(String sql, String phone) {
        return this.queryForJsonObject(sql,phone);
    }

    @Override
    public JSONObject getUserInfoByUserId(String sql, Integer userId) {
        return this.queryForJsonObject(sql,userId);
    }

    @Override
    public int saveUserPhone( String tableName, JSONObject args) {
        return this.insert(tableName,args);
    }

    @Override
    public int updateClientUserInfo(String sql) {
        return this.update(sql);
    }

    @Override
    public JSONObject getVersion(String sql, Integer versionId) {
        return this.queryForJsonObject(sql,versionId);
    }

    @Override
    public List<JSONObject> getDeviceNo(StringBuffer sql, Integer userId) {
        return this.queryForJsonList(sql.toString(),userId);
    }

    @Override
    public List<JSONObject> getGpsInfo(StringBuffer gpsInfoSql, String device_no) {
        return this.queryForJsonList(gpsInfoSql.toString(),device_no);
    }
}
