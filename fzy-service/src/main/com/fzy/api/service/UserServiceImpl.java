package com.fzy.api.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.fsy.api.interfaces.IUserService;
import com.fsy.api.utils.Const;
import com.fzy.api.dao.IUserDao;
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
@Service(interfaceClass = com.fsy.api.interfaces.IUserService.class, protocol = {"dubbo"})
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
    public JSONObject getGpsInfo(Integer userId, Integer pageName) {
        StringBuffer deviceSql = new StringBuffer("select device_no from device where client_id=? limit ?,30 ");
        List<JSONObject> deviceNo =   userDao.getDeviceNo(deviceSql, userId,pageName);
        JSONObject info = new JSONObject();
        for (int i = 0; i < deviceNo.size(); i++) {
            StringBuffer gpsInfoSql = new StringBuffer("select * from gps_info where phoneContect=?  order by call_time desc");
            String device_no = deviceNo.get(i).get("device_no").toString();
            List<JSONObject> gpsInfo = userDao.getGpsInfo(gpsInfoSql,device_no);
            info.put(device_no,gpsInfo);
        }
        return info;
    }


    @Override
    public int getGpsInfoCount(Integer userId) {
        StringBuffer stringBuffer = new StringBuffer("select id  from gps_info where client_user_id=? ");
        return userDao.getGpsInfoCount(stringBuffer,userId);
    }

    @Override
    public List<JSONObject> getGpsUploadList(Integer gpsInfoId, int pageNum, Integer resourceType) {
        StringBuffer sql =new StringBuffer("select * from gps_upload where gps_id=? ");
        if (resourceType != null) {
            if (resourceType==0 ) {
                sql.append(" and upload_type in(0,1) ");
            }else if(resourceType==1){
                sql.append(" and upload_type =2 ");
            }
        }
        sql.append(" order by upload_date desc limit ?,30");
        List<JSONObject>list = userDao.getGpsUploadList(sql,gpsInfoId,pageNum);
        return list;
    }

    @Override
    public int getGpsUploadCount(Integer gpsInfoId, Integer resourceType) {
        StringBuffer sql = new StringBuffer("select id  from gps_upload where gps_id=? ");
        if (resourceType != null) {
            if (resourceType == 0) {
                sql.append(" and upload_type in(0,1) ");
            } else if (resourceType == 1) {
                sql.append(" and upload_type =2 ");
            }
        }
        return userDao.getGpsUploadCount(sql,gpsInfoId);
    }

    @Override
    public JSONObject getFTPAddr() {
        StringBuffer sql = new StringBuffer("select *  from http_addr");
       return  userDao.getFTPAddr(sql);
    }

    @Override
    public JSONObject getGpsAddr(Integer gpsInfoId) {
        StringBuffer sql = new StringBuffer("select * from gps_addr where gps_info_id=? order by gps_time desc");
        return userDao.getGpsAddr(sql.toString(), gpsInfoId);
    }


}
