package com.fsy.api.interfaces;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * 描述:
 * ClientUser表相关 接口
 *
 * @auth zln
 * @create 2017-12-03 14:58
 */
public interface IUserService {

    /**
     * 通过手机获取用户信息
     *
     * @param phone
     * @return
     */
    JSONObject getUserInfoByPhone(String phone);

    /**
     * 通过用户id获取用户信息
     *
     * @param userId
     * @return
     */
    JSONObject getUserInfoByUserId(Integer userId);

    /**
     * 注册账号
     *
     * @param phone
     * @param password
     * @return
     */
    int saveUserPhone(String phone, String password);

    /**
     * 修改各种用户信息(如:username,phone)
     *
     * @param jsonObject
     * @return
     */
    int updateClientUserInfo(JSONObject jsonObject);

    /**
     * 获取版本信息
     *
     * @param versionId
     * @return
     */
    JSONObject getVersion(Integer versionId);

    /**
     * 获取gpsInfo 信息
     *
     * @param userId
     * @param pageName
     * @return
     */
    JSONObject getGpsInfo(Integer userId, Integer pageName);

    /**
     * 获取gpsAddr信息
     * @param gpsInfoId
     * @return
     */
    JSONObject getGpsAddr(Integer gpsInfoId);

    /**
     * 获取用户报险总条数
     * @param userId
     * @return
     */
    int getGpsInfoCount(Integer userId);

    /**
     * 获取秒帮上传资源
     * @param gpsInfoId
     * @param pageNUm
     * @param resourceType 资源类型 0为图片与音频,一为视频
     * @return
     */
    List<JSONObject> getGpsUploadList(Integer gpsInfoId, int pageNUm, Integer resourceType);

    /**
     * 获取秒帮上传资源条数
     * @param gpsInfoId
     * @param resourceType
     * @return
     */
    int getGpsUploadCount(Integer gpsInfoId, Integer resourceType);

    /**
     * 获取ftp地址
     * @return
     */
    JSONObject getFTPAddr();

}
