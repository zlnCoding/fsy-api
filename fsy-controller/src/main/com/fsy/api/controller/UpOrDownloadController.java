package com.fsy.api.controller;

import com.alibaba.fastjson.JSONObject;
import com.fsy.api.interfaces.IUpOrDownloadService;
import com.fsy.api.utils.Const;
import com.fsy.api.utils.OSSUtils;
import com.fsy.api.utils.ReducePhotoUtils;
import com.fsy.api.utils.TSAUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

;

/**
 * 描述:
 * 上传下载接口
 *
 * @auth zln
 * @create 2017-12-05 9:44
 */
@Controller
@Scope("prototype")
@RequestMapping("/file")
public class UpOrDownloadController {

    @Autowired
    public IUpOrDownloadService upOrDownService;

    ExecutorService executors = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    ReentrantLock lock = new ReentrantLock();
    Condition minResourceLock = lock.newCondition();

    /**
     * 为了能快速返回给app结果,文件采用多线程上传,如果upoload_client中的数据upload_size字段为0表示上传oss失败,资源不删除
     *
     * @param file   文件
     * @param userId 用户id
     * @param type   类型 0图片，1音频，2 视频 3 其他
     * @param upType 0 pc文件上传，1 手机录音 2，手机录像 3 手机照片 4 现场录音 5实名身份证
     * @param desc   描述
     * @return
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject uploadFile(@RequestParam("file") MultipartFile[] file, Integer userId, Integer type, Integer upType, String desc) {
        if (!Const.isNotNull(file[0])) return Const.returnFail("请不要上传空文件");
        for (MultipartFile mult : file) {
            String originalFilename = mult.getOriginalFilename();
            long fileSize = mult.getSize();
            if ("".equals(originalFilename) && fileSize == 0) {
                continue;
            }
            int size = upOrDownService.getUploadByName(originalFilename, userId);
            if (size > 0) {
                return Const.returnFail("文件名重复,请修改文件名");
            }
            try {
                File tempfile = null;
                String uploadPath = OSSUtils.OSSPath(originalFilename, userId);
                String uploadPathMin = null;

                try {
                    if (Const.OSType().contains("windows")) {
                        tempfile = new File(Const.WINDOWS_FILE_PATH + Const.OSSeparator() + originalFilename);
                    } else {
                        tempfile = new File(Const.LINUX_FILE_PATH + Const.OSSeparator() + originalFilename);
                    }

                    //上传资源
                    uploadResource(userId, originalFilename,mult.getInputStream());
                    //生成本地文件
                    if (!tempfile.exists()) {
                        mult.transferTo(tempfile);
                    }

                    if (type == 0) {
                        uploadPathMin = OSSUtils.OSSPath("min-" + originalFilename, userId);
                        //生成缩略图
                        new ReducePhotoUtils(tempfile.getPath(), originalFilename);
                        //上传缩略图
                        uploadMinPhoto(userId, originalFilename, tempfile.getParent() + Const.OSSeparator() + "min-" + originalFilename,tempfile);
                    }
                    File tsaFile = TSAUtils.tsa(tempfile.getParent() + Const.OSSeparator(), originalFilename);
                    if (tsaFile.exists()) {
                        //上传tsa
                        uploadTsa(userId, tsaFile);
                    }
                    //上传成功入库
                    upOrDownService.saveUploadInfo(Const.dateFormat(new Date(System.currentTimeMillis())), type, upType, 1, desc, userId, originalFilename, fileSize, uploadPath, uploadPathMin);
                } catch (IOException e) {
                    //上传失败入库 但size为0表示失败
                    upOrDownService.saveUploadInfo(Const.dateFormat(new Date(System.currentTimeMillis())), type, upType, 1, desc, userId, originalFilename, 0, uploadPath, uploadPathMin);
                    e.printStackTrace();
                    Const.logger.error(e.toString());
                    return Const.returnFail("上传失败!");
                }
            } catch (Exception e) {
                e.printStackTrace();
                Const.logger.error(e.toString());
                return Const.returnFail("上传失败!");
            }
        }
        return Const.returnSuccess("上传成功!");
    }

    /**
     * 上传原文件
     */
    public synchronized void uploadResource(final Integer userId, final String originalFilename, final InputStream file) throws ExecutionException, InterruptedException {

        executors.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    lock.lock();
                    OSSUtils.uploadFile(originalFilename, file, userId);
                    //OSSUtils.uploadFile(originalFilename, tempfile.getPath(), userId);
                } catch (Exception e) {
                    Const.logger.error(e.toString());
                } finally {
                    lock.unlock();
                }
            }
        });

    }

    /**
     * 上传缩略图
     *
     * @param userId
     * @param originalFilename
     * @param minPath
     * @param tempfile
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public synchronized void uploadMinPhoto(final Integer userId, final String originalFilename, final String minPath,final File tempfile) throws ExecutionException, InterruptedException {
        executors.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    lock.lock();
                    File fileMin = new File(minPath);
                    if (fileMin.exists()) {
                    OSSUtils.uploadFile("min-" + originalFilename, minPath, userId);
                        fileMin.delete();
                    }
                    minResourceLock.await();
                    if (tempfile != null) tempfile.delete();
                } catch (Exception e) {
                    Const.logger.error(e.toString());
                } finally {
                    lock.unlock();
                }
            }
        });

    }

    /**
     * 上传tsa文件
     *
     * @param userId
     * @param tsaFile
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public synchronized void uploadTsa(final Integer userId, final File tsaFile) throws ExecutionException, InterruptedException {

        executors.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    lock.lock();
                    OSSUtils.uploadFile(tsaFile.getName(), tsaFile.getPath(), userId);
                    if (tsaFile.exists()) {
                        tsaFile.delete();
                    }
                } catch (Exception e) {
                    Const.logger.error(e.toString());
                } finally {
                    minResourceLock.signal();
                    lock.unlock();
                }
            }
        });
    }


    /**
     * 获取用户上传列表通过userid
     *
     * @param userId
     * @param pageNum 页码
     * @return
     */
    @RequestMapping(value = "/uploadList")
    @ResponseBody
    public Object uploadList(Integer userId, Integer pageNum, Integer type) {
        if (!Const.isNotNull(userId)) return Const.returnFail("id为空");
        List<JSONObject> list = upOrDownService.getUploadListByUserId(userId, type, ((pageNum == null ? 1 : pageNum) - 1) * 30);
        //生成缩略图地址
        for (JSONObject jsonObject : list) {
            if (jsonObject.getInteger("type") != 0) {
                continue;
            }
            String s = minPhotoUrl(jsonObject.getString(("upload_url_min")));
            jsonObject.put("upload_url_min", s);
        }
        int totle = upOrDownService.getUploadListCount(userId, type);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("uploadList", list);
        jsonObject.put("totle", totle);
        return jsonObject;
    }

    /**
     * 生成下载链接有效期60分钟
     *
     * @param uploadId
     * @return
     */
    @RequestMapping(value = "/download")
    @ResponseBody
    public Object downloadFile(Integer uploadId) {
        if (!Const.isNotNull(uploadId)) return Const.returnFail("id为空");
        JSONObject uploadById = upOrDownService.getUploadById(uploadId);
        if (uploadById.size() > 0) {
            JSONObject url = new JSONObject();
            url.put("url", OSSUtils.downloadFile(uploadById.getString("upload_url")));
            return url;
        } else {
            return Const.returnFail("没有uploadId为" + uploadById + "的资源!");
        }

    }

    public String minPhotoUrl(String uploadUrlMin) {
        return OSSUtils.downloadFile(uploadUrlMin).toString();
    }

    /**
     * 伪删除,修改upload_client中的 upload_status为0
     * @param uploadId
     * @return
     */
    @ResponseBody
    @RequestMapping("/deleteFile")
    public Object deleteFile(Integer uploadId) {
        if (!Const.isNotNull(uploadId)) return Const.returnFail("id为空");
        int result = upOrDownService.deleteFile(uploadId);
        if (result > 0) {
            return Const.returnSuccess("删除成功!");
        } else {
            return Const.returnFail("修改失败");
        }
    }

    /**
     * 申请出证
     *
     * @param uploadId
     * @param userId
     * @param log_type 0 文件 1 秒帮
     * @return
     */
    @ResponseBody
    @RequestMapping("/applyAttest")
    public JSONObject applyAttest(Integer uploadId, Integer userId, Integer log_type) {
        if (!Const.isNotNull(uploadId, userId)) return Const.returnFail("id为空");
        int result = upOrDownService.applyAttest(uploadId, userId, log_type);
        if (result > 0) return Const.returnSuccess("申请成功!");
        else return Const.returnFail("申请失败!");
    }

    /**
     * 出证列表
     *
     * @param userId
     * @return
     */
    @ResponseBody
    @RequestMapping("/applyAttestList")
    public Object applyAttestList(Integer userId, Integer pageNum) {
        if (!Const.isNotNull(userId)) return Const.returnFail("id为空");
        List<JSONObject> list = upOrDownService.applyAttestList(userId, ((pageNum == null ? 1 : pageNum) - 1) * 30);
        int totle = upOrDownService.getApplyListCount(userId);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("applyList", list);
        jsonObject.put("totle", totle);
        return jsonObject;
    }


}
