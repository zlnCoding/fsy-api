package fsy.api.controller;

import com.alibaba.fastjson.JSONObject;
import fsy.interfaces.IUserService;
import fsy.utils.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 描述:
 * User相关功能接口
 *
 * @auth zln
 * @create 2017-11-28 15:20
 */

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    public IUserService userService;

    /**
     * 登录接口
     *
     * @param phone    手机号
     * @param password 密码(app 已加密)
     * @return
     */
    @RequestMapping("/login")
    @ResponseBody
    public JSONObject Login(String phone, String password) {

        if (!Const.isNotNull(phone, password)) return Const.returnFail("'用户名或密码错误!");

        JSONObject userInfo = userService.getUserInfoByPhone(phone);
        if (userInfo == null) {
            return Const.returnFail("'该账户不存在!");
        }

        if (password.equals(userInfo.get("password"))) {
            JSONObject result = Const.returnSuccess("登录成功!");
            result.put("username", Const.nullToEmptyString(userInfo.get("username")));
            result.put("id", Const.nullToEmptyString(userInfo.get("id")));
            result.put("auth", userInfo.get("username") != null && userInfo.get("id_card") != null ? "1" : "0");
            return result;
        } else {
            return Const.returnFail("账号或密码错误!");
        }
    }

    /**
     * 注册接口
     *
     * @param phone
     * @return
     */
    @RequestMapping("/registry")
    @ResponseBody
    public JSONObject registry(String phone, @RequestParam(defaultValue = "") String password) {
        if (!Const.isNotNull(phone)) return Const.returnFail("手机号不能为空!");
        JSONObject user = userService.getUserInfoByPhone(phone);
        if (user != null) {
            return Const.returnFail("该手机号已存在!");
        }
        int i = userService.saveUserPhone(phone, password);
        if (i > 0) {
            JSONObject userInfo = userService.getUserInfoByPhone(phone);
            JSONObject result = Const.returnSuccess("注册成功!");
            result.put("username", Const.nullToEmptyString(userInfo.get("username")));
            result.put("id", Const.nullToEmptyString(userInfo.get("id")));
            result.put("auth", userInfo.get("username") != null && userInfo.get("id_card") != null ? "1" : "0");
            return result;
        } else {
            return Const.returnFail("注册失败!");
        }
    }

    /**
     * 校验用户
     *
     * @param phone
     * @return
     */
    @RequestMapping("/checkUserByPhone")
    @ResponseBody
    public JSONObject checkUserByPhone(String phone) {
        if (!Const.isNotNull(phone)) return Const.returnFail("手机号不能为空!");

        JSONObject userInfo = userService.getUserInfoByPhone(phone);
        if (userInfo != null) {
            return Const.returnSuccess("该手机号已存在!");
        } else {
            return Const.returnFail("该手机号不存在!");
        }
    }

    /**
     * 忘记密码及修改密码接口
     *
     * @param phone
     * @param password
     * @return
     */
    @RequestMapping("/updatePassword")
    @ResponseBody
    public JSONObject updatePassword(String phone, String password) {
        if (!Const.isNotNull(phone)) return Const.returnFail("手机号不能为空!");
        JSONObject jsonObject = userService.getUserInfoByPhone(phone);
        if (jsonObject == null) {
            return Const.returnFail("没有该用户!");
        } else {
            jsonObject.put("password", password);
            int result = userService.updateClientUserInfo(jsonObject);
            if (result > 0) return Const.returnSuccess("修改成功!");
            else return Const.returnFail("修改失败!");
        }
    }

    /**
     * 修改用户名接口
     *
     * @param username
     * @return
     */
    @RequestMapping("/updateUsername")
    @ResponseBody
    public JSONObject updateUsername(@RequestParam(required = true) String username, @RequestParam(required = true) Integer userId) {
        if (!Const.isNotNull(userId)) return Const.returnFail("用户id不能为空!");
        JSONObject jsonObject = userService.getUserInfoByUserId(userId);
        if (jsonObject == null) {
            return Const.returnFail("没有该用户!");
        } else {
            jsonObject.put("username", username);
            int result = userService.updateClientUserInfo(jsonObject);
            if (result > 0) return Const.returnSuccess("修改成功!");
            else return Const.returnFail("修改失败!");
        }
    }

    /**
     * 修改手机号接口
     *
     * @param userId
     * @param phone
     * @return
     */
    @RequestMapping("/updatePhone")
    @ResponseBody
    public JSONObject updatePhone(@RequestParam(required = true) Integer userId, @RequestParam(required = true) String phone) {
        if (!Const.isNotNull(userId)) return Const.returnFail("用户id不能为空!");
        JSONObject check = this.checkUserByPhone(phone);

        if (check.get("result") == 1) {
            return Const.returnFail("新手机号已存在!");
        }
        JSONObject jsonObject = userService.getUserInfoByUserId(userId);
        if (jsonObject == null) {
            return Const.returnFail("没有该用户!");
        } else {
            jsonObject.put("phone", phone);
            int result = userService.updateClientUserInfo(jsonObject);
            if (result > 0) return Const.returnSuccess("修改成功!");
            else return Const.returnFail("修改失败!");
        }
    }

    /**
     * 实名认证
     *
     * @param username 真实姓名
     * @param idCard   证件号
     * @param idType
     * @param gender   性别
     * @return
     */
    @RequestMapping("/authRealname")
    @ResponseBody
    public JSONObject authRealname(String username, String idCard, Integer idType, Integer gender, Integer userId) {
        if (!Const.isNotNull(username, idCard, idType, gender, userId)) return Const.returnFail("参数不能为空!");

        JSONObject jsonObject = userService.getUserInfoByUserId(userId);
        if (jsonObject == null) {
            return Const.returnFail("没有该用户!");
        } else {
            jsonObject.put("username", username);
            jsonObject.put("id_card", idCard);
            jsonObject.put("id_type", idType);
            jsonObject.put("sex", gender);
            int result = userService.updateClientUserInfo(jsonObject);
            if (result > 0) return Const.returnSuccess("修改成功!");
            else return Const.returnFail("修改失败!");
        }
    }

    /**
     * 获取版本号
     *
     * @param versionId
     * @return
     */
    @RequestMapping("/getVersion")
    @ResponseBody
    public JSONObject getVersion(Integer versionId) {
        if (!Const.isNotNull(versionId)) return Const.returnFail("versionId不能为空!");
        JSONObject jsonObject = userService.getVersion(versionId);

        if (jsonObject == null) {
            return Const.returnFail("暂无版本信息!");
        } else {
            return jsonObject;
        }
    }

    /**
     * 获取用户秒帮报险信息
     * @param userId
     * @return
     */
    @RequestMapping("/getGpsInfo")
    @ResponseBody
    public JSONObject getGpsInfo(Integer userId) {
        if (!Const.isNotNull(userId)) return Const.returnFail("userId不能为空!");
        JSONObject gpsInfo = userService.getGpsInfo(userId);
        return  gpsInfo;
    }
}
