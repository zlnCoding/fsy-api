package fsy.interfaces;/**
 * Created by zln on 2017/12/3.
 */

import com.alibaba.fastjson.JSONObject;

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
     * @return
     */
    JSONObject getGpsInfo(Integer userId);
}
