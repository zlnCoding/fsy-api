package fsy.api.dao;/**
 * Created by zln on 2017/12/3.
 */

import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * 描述:
 * IUserService 数据库访问类
 *
 * @auth zln
 * @create 2017-12-03 15:08
 */
public interface IUserDao {

    /**
     * 通过手机号获取用户信息
     *
     * @param phone
     * @return
     */
    JSONObject getUserInfoByPhone(String sql, String phone);

    /**
     * 通过用户id 获取用户信息
     * @param sql
     * @param userId
     * @return
     */
    JSONObject getUserInfoByUserId(String sql, Integer userId);

    /**
     * 保存用户注册的手机
     *
     * @param tableName
     * @param args
     * @return
     */
    int saveUserPhone(String tableName, JSONObject args);

    /**
     * 修改用户各种信息(如:username,phone,password)
      * @param sql
     * @return
     */
    int updateClientUserInfo(String sql);

    /**
     * 获取版本信息
     * @param sql
     * @param versionId
     * @return
     */
    JSONObject getVersion(String sql, Integer versionId);

    /**
     * 获取用户下的设备号
     * @param sql
     * @param userId
     * @return
     */
    List<JSONObject> getDeviceNo(StringBuffer sql, Integer userId);

    /**
     * 通过设备号获取gpsInfo 信息
     *
     * @param gpsInfoSql
     * @param device_no
     * @return
     */
    List<JSONObject> getGpsInfo(StringBuffer gpsInfoSql, String device_no);
}