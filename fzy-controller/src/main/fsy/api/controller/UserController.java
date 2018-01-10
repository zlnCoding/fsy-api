package fsy.api.controller;

import com.alibaba.fastjson.JSONObject;
import fsy.interfaces.IUserService;
import fsy.utils.Const;
import fsy.utils.Encrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

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
     * 账密登录接口
     *
     * @param phone    手机号
     * @param password 密码(app 已加密)
     * @return
     */
    @RequestMapping("/login")
    @ResponseBody
    public JSONObject login(String phone, String password) {

        if (!Const.isNotNull(phone, password)) return Const.returnFail("'用户名或密码错误!");

        JSONObject userInfo = userService.getUserInfoByPhone(phone);
        if (userInfo == null) {
            return Const.returnFail("'该账户不存在!");
        }

        if (password.equals(userInfo.get("password"))) {
            JSONObject result = Const.returnSuccess("登录成功!");
            result.put("username", Const.nullToEmptyString(userInfo.get("username")));
            result.put("id", Const.nullToEmptyString(userInfo.get("id")));
            result.put("password", Const.nullToEmptyString(userInfo.get("password")));
            result.put("auth", userInfo.get("username") != null && userInfo.get("id_card") != null ? "1" : "0");
            return result;
        } else {
            return Const.returnFail("账号或密码错误!");
        }
    }


    /**
     * 手机验证码登录接口
     *
     * @param phone 手机号
     * @return
     */
    @RequestMapping("/phoneLogin")
    @ResponseBody
    public JSONObject phoneLogin(String phone) {
        if (!Const.isNotNull(phone)) return Const.returnFail("手机号不能为空!");

        JSONObject userInfo = userService.getUserInfoByPhone(phone);
        if (userInfo == null) {
            int i = userService.saveUserPhone(phone, Encrypt.md5(phone));
            if (i > 0) {
                JSONObject user = userService.getUserInfoByPhone(phone);
                JSONObject result = Const.returnSuccess("登录成功!");
                result.put("username", Const.nullToEmptyString(user.get("username")));
                result.put("id", Const.nullToEmptyString(user.get("id")));
                result.put("password", Const.nullToEmptyString(user.get("password")));
                result.put("auth", user.get("username") != null && user.get("id_card") != null ? "1" : "0");
                return result;
            } else {
                return Const.returnFail("请求失败!");
            }
        } else {
            JSONObject result = Const.returnSuccess("登录成功!");
            result.put("username", Const.nullToEmptyString(userInfo.get("username")));
            result.put("id", Const.nullToEmptyString(userInfo.get("id")));
            result.put("password", Const.nullToEmptyString(userInfo.get("password")));
            result.put("auth", userInfo.get("username") != null && userInfo.get("id_card") != null ? "1" : "0");
            return result;
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
            result.put("password", Const.nullToEmptyString(userInfo.get("password")));
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
     * 如果是修改密码,app端进行新旧密码校验,通过之后传新密码,
     * 如果是忘记密码,app端进行验证码校验,通过之后传新密码
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

        if (check.getInteger("result") == 1) {
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
     *
     * @param userId
     * @return
     */
    @RequestMapping("/getGpsInfo")
    @ResponseBody
    public Object getGpsInfo(Integer userId, Integer pageNum) {
        if (!Const.isNotNull(userId)) return Const.returnFail("userId不能为空!");
        JSONObject gpsInfo = userService.getGpsInfo(userId, ((pageNum == null ? 1 : pageNum) - 1) * 30);
        if (gpsInfo != null) {
            int totle = userService.getGpsInfoCount(userId);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("gpsInfoList", gpsInfo);
            jsonObject.put("totle", totle);
            return jsonObject;
        }else {
            return Const.returnFail("暂无报险信息!");
        }
    }

    /**
     * 获取用户秒帮报险地址
     *
     * @param gpsInfoId
     * @return
     */
    @RequestMapping("/getGpsAddr")
    @ResponseBody
    public JSONObject getGpsAddr(Integer gpsInfoId) {
        if (!Const.isNotNull(gpsInfoId)) return Const.returnFail("gpsInfoId不能为空!");
        JSONObject gpsInfo = userService.getGpsAddr(gpsInfoId);
        return gpsInfo;
    }

    /**
     * 获取秒帮上传资源列表
     *
     * @param gpsInfoId
     * @param pageNum      页码
     * @param resourceType 资源类型 0为图片与音频,1为视频
     * @return
     */
    @RequestMapping("/getGpsUploadList")
    @ResponseBody
    public Object getGpsUploadList(Integer gpsInfoId, Integer pageNum, Integer resourceType) {
        if (!Const.isNotNull(gpsInfoId)) return Const.returnFail("gpsInfoId不能为空!");
        List<JSONObject> gpsUpload = userService.getGpsUploadList(gpsInfoId, ((pageNum == null ? 1 : pageNum) - 1) * 30, resourceType);
        if (gpsUpload != null) {
            int totle = userService.getGpsUploadCount(gpsInfoId, resourceType);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("gpsUploadList", gpsUpload);
            jsonObject.put("totle", totle);
            return jsonObject;
        } else {
            return Const.returnFail("暂无上传资源!");
        }
    }

    /**
     * 获取FTP地址
     *
     * @return
     */
    @RequestMapping("/getFTPAddr")
    @ResponseBody
    public Object getFTPAddr() {
        return userService.getFTPAddr();
    }

    public static void main(String[] args) {
        System.out.println(System.currentTimeMillis());
    }
}
