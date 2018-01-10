package fsy.api.dao.impl;

import com.alibaba.fastjson.JSONObject;
import fsy.api.dao.BaseDao;
import fsy.api.dao.IUserDao;
import fsy.utils.Const;
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
        try {
            return this.queryForJsonObject(sql,phone);
        } catch (Exception e) {
            Const.logger.error(e.toString());
            return null;
        }
    }

    @Override
    public JSONObject getUserInfoByUserId(String sql, Integer userId) {
        try {
            return this.queryForJsonObject(sql,userId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int saveUserPhone( String tableName, JSONObject args) {
        try {
            return this.insert(tableName,args);
        } catch (Exception e) {
            Const.logger.error(e.toString());
            return 0;
        }
    }

    @Override
    public int updateClientUserInfo(String sql) {
        try {
            return this.update(sql);
        } catch (Exception e) {
            Const.logger.error(e.toString());
            return 0;
        }
    }

    @Override
    public JSONObject getVersion(String sql, Integer versionId) {
        try {
            return this.queryForJsonObject(sql,versionId);
        } catch (Exception e) {
            Const.logger.error(e.toString());
            return null;
        }
    }

    @Override
    public List<JSONObject> getDeviceNo(StringBuffer sql, Integer userId, Integer pageNum) {
        return this.queryForJsonList(sql.toString(),userId,pageNum);
    }

    @Override
    public List<JSONObject> getGpsInfo(StringBuffer gpsInfoSql, String device_no) {
        try {
            return this.queryForJsonList(gpsInfoSql.toString(),device_no);
        } catch (Exception e) {
            Const.logger.error(e.toString());
            return null;
        }
    }

    @Override
    public JSONObject getGpsAddr(String sql, Integer gpsInfoId) {
        List<JSONObject> list = this.queryForJsonList(sql, gpsInfoId);
        if(list.size() > 0) {
            return list.get(0);
        }
        return new JSONObject();
    }

    @Override
    public int getGpsInfoCount(StringBuffer sql, Integer userId) {
        try {
            return this.queryForJsonList(sql.toString(),userId).size();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public List<JSONObject> getGpsUploadList(StringBuffer sql, Integer gpsInfoId, int pageNum) {
        try {
            return this.queryForJsonList(sql.toString(),gpsInfoId,pageNum);
        } catch (Exception e) {
            Const.logger.error(e.toString());
            return null;
        }
    }

    @Override
    public int getGpsUploadCount(StringBuffer sql, Integer gpsInfoId) {
        try {
            return this.queryForJsonList(sql.toString(),gpsInfoId).size();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public JSONObject getFTPAddr(StringBuffer sql) {
        JSONObject jsonObject = this.queryForJsonObject(sql.toString());
        if(jsonObject == null) {
            jsonObject = new JSONObject();
        }
        return jsonObject;
    }
}
