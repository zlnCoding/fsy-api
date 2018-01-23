package com.fzy.api.dao;

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
     *
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
     *
     * @param sql
     * @return
     */
    int updateClientUserInfo(String sql);

    /**
     * 获取版本信息
     *
     * @param sql
     * @param versionId
     * @return
     */
    JSONObject getVersion(String sql, Integer versionId);

    /**
     * 获取用户下的设备号
     *
     * @param sql
     * @param userId
     * @param pageNum
     * @return
     */
    List<JSONObject> getDeviceNo(StringBuffer sql, Integer userId, Integer pageNum);

    /**
     * 通过设备号获取gpsInfo 信息
     *
     * @param gpsInfoSql
     * @param device_no
     * @return
     */
    List<JSONObject> getGpsInfo(StringBuffer gpsInfoSql, String device_no);

    /**
     * 获取gpsAddr信息
     *
     * @param sql
     * @param gpsInfoId
     * @return
     */
    JSONObject getGpsAddr(String sql, Integer gpsInfoId);

    /**
     * 获取用户报险总条数
     *
     * @param sql
     * @param userId
     * @return
     */
    int getGpsInfoCount(StringBuffer sql, Integer userId);

    /**
     * 获取秒帮上传资源
     *
     * @param stringBuffer
     * @param gpsInfoId
     *@param pageNum  @return
     */
    List<JSONObject> getGpsUploadList(StringBuffer stringBuffer, Integer gpsInfoId, int pageNum);

    /**
     * 获取秒帮上传资源条数
     *
     * @param sql
     * @param gpsInfoId
     * @return
     */
    int getGpsUploadCount(StringBuffer sql, Integer gpsInfoId);

    /**
     * 获取ftp地址
     * @param sql
     * @return
     */
    JSONObject getFTPAddr(StringBuffer sql);
}
