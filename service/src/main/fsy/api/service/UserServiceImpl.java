package fsy.api.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import fsy.api.dao.IUserDao;
import fsy.interfaces.IUserService;
import fsy.utils.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;

/**
 * 描述:
 * User相关功能接口实现类
 *
 * @auth zln
 * @create 2017-12-03 15:06
 */
@Transactional
@org.springframework.stereotype.Service("userService")
@Service(interfaceClass = fsy.interfaces.IUserService.class, protocol = {"dubbo"})
public class UserServiceImpl implements IUserService {

    @Autowired
    public IUserDao userDao;

    @Override
    public JSONObject getUserInfoByPhone(String phone) {
        StringBuffer sql = new StringBuffer("select * from client_user where phone=?");
        JSONObject jsonObject = userDao.getUserInfoByPhone(sql.toString(), phone);
        return jsonObject;
    }

    @Override
    public JSONObject getUserInfoByUserId(Integer userId) {
        StringBuffer sql = new StringBuffer("select * from client_user where id=?");
        JSONObject jsonObject = userDao.getUserInfoByUserId(sql.toString(), userId);
        return jsonObject;
    }

    @Override
    public int saveUserPhone(String phone, String password) {
        JSONObject args = new JSONObject();
        args.put("phone", phone);
        args.put("password", password);
        args.put("create_date", new Date(System.currentTimeMillis()));
        return userDao.saveUserPhone("client_user", args);
    }

    @Override
    public synchronized int updateClientUserInfo(JSONObject args) {

        StringBuffer sql = new StringBuffer("update client_user set username=");
        sql.append("'" + Const.nullToEmptyString(args.getString("username")) + "'");

        sql.append(",password=");
        sql.append("'" + Const.nullToEmptyString(args.getString("password")) + "'");

        sql.append(",email=");
        sql.append("'" + Const.nullToEmptyString(args.getString("email")) + "'");

        sql.append(",id_card=");
        sql.append("'" + Const.nullToEmptyString(args.getString("id_card")) + "'");

        sql.append(",phone=");
        sql.append("'" + Const.nullToEmptyString(args.getString("phone")) + "'");

        sql.append(",address=");
        sql.append("'" + Const.nullToEmptyString(args.getString("address")) + "'");

        sql.append(",id_type=");
        sql.append("'" + Const.nullToEmptyString(args.getString("id_type")) + "'");

        if (args.get("sex") != null && !args.get("sex").equals("")) {
            sql.append(",sex=");
            sql.append("'" + Const.nullToEmptyString(args.getString("sex")) + "'");
        }

        sql.append(" where id=");
        sql.append(args.getString("id"));
        return userDao.updateClientUserInfo(sql.toString());
    }

    @Override
    public JSONObject getVersion(Integer versionId) {
        StringBuffer sql = new StringBuffer("select * from version where id=?");
        return userDao.getVersion(sql.toString(), versionId);
    }

    @Override
    public JSONObject getGpsInfo(Integer userId) {
        StringBuffer deviceSql = new StringBuffer("select device_no from device where client_id=?");
        List<JSONObject> deviceNo =   userDao.getDeviceNo(deviceSql, userId);
        JSONObject info = new JSONObject();
        for (int i = 0; i < deviceNo.size(); i++) {
            StringBuffer gpsInfoSql = new StringBuffer("select * from gps_info where phoneContect=?");
            String device_no = deviceNo.get(i).get("device_no").toString();
            List<JSONObject> gpsInfo = userDao.getGpsInfo(gpsInfoSql,device_no);
            info.put(device_no,gpsInfo);
        }
        return info;

    }

}
