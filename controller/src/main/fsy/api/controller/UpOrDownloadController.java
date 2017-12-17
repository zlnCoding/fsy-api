package fsy.api.controller;

import com.alibaba.fastjson.JSONObject;
import fsy.interfaces.IUpOrDownloadService;
import fsy.utils.Const;
import fsy.utils.OSSUtils;
import fsy.utils.ReducePhotoUtils;
import fsy.utils.TSAUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.util.List;
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
@RequestMapping("/file")
public class UpOrDownloadController {

    @Autowired
    public IUpOrDownloadService upOrDownService;

    /**
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
        ReentrantLock lock = new ReentrantLock();
        for (MultipartFile mult : file) {
            String originalFilename = mult.getOriginalFilename();
            try {
                threadUpload(mult, userId, type,upType, desc,originalFilename);
            } catch (Exception e) {
                e.printStackTrace();
                Const.returnSuccess("上传失败!");
            }
        }
        return Const.returnSuccess("上传成功!");
    }

    /**
     * 此方法为上传线程,为了能快速返回给app结果,如果upoload_client中的数据upload_size字段为0表示上传oss失败,资源不删除
     */
    private synchronized void threadUpload(final MultipartFile mult, final Integer userId, final Integer type,final Integer upType, final String desc, final String originalFilename) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                File tempfile = null;
                File minPath = null;
                long fileSize = mult.getSize();
                String uploadPathMin = null;
                String uploadPath = null;
                try {
                    if (Const.OSType().contains("windows")) {
                        tempfile = new File(Const.WINDOWS_FILE_PATH + originalFilename);
                        minPath = new File(Const.WINDOWS_FILE_PATH );
                    } else {
                        tempfile = new File(Const.LINUX_FILE_PATH + originalFilename);
                        minPath = new File(Const.LINUX_FILE_PATH );
                    }

                    mult.transferTo(tempfile);
                    //上传资源
                    uploadPath = OSSUtils.uploadFile(originalFilename, tempfile.getPath(), userId);

                    if (type == 0) {
                        //生成缩略图
                        new ReducePhotoUtils(tempfile.getPath(), originalFilename);
                        //上传缩略图
                        uploadPathMin =  OSSUtils.uploadFile("min-" + originalFilename, minPath + "min-" + originalFilename, userId);
                    }
                    upOrDownService.saveUploadInfo(Const.dateFormat(new Date(System.currentTimeMillis())), type, upType, 1, desc, userId, originalFilename, fileSize, uploadPath, uploadPathMin);

                    TSAUtils.tsa(minPath.getPath(),originalFilename);
                    //上传tsa
                    OSSUtils.uploadFile(originalFilename, minPath.getPath()+originalFilename, userId);
                    if (tempfile != null) tempfile.delete();
                    File fileMin = new File(minPath + "min-" + originalFilename);
                    if (fileMin.exists()) {
                        fileMin.delete();
                    }
                } catch (IOException e) {
                    upOrDownService.saveUploadInfo(Const.dateFormat(new Date(System.currentTimeMillis())), type, upType, 1, desc, userId, originalFilename, 0, uploadPath, uploadPathMin);
                    e.printStackTrace();
                }

            }

        }).start();
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
        List<JSONObject> list = upOrDownService.getUploadListByUserId(userId, type, ((pageNum == null ? 0 : pageNum) - 1) * 10);
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
            return OSSUtils.downloadFile(uploadById.getString("upload_url"));
        } else {
            return Const.returnFail("没有uploadId为" + uploadById + "的资源!");
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
    public Object applyAttestList(Integer userId) {
        if (!Const.isNotNull(userId)) return Const.returnFail("id为空");
        List<JSONObject> list = upOrDownService.applyAttestList(userId);
        return list;
    }

}
