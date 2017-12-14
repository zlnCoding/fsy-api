package fsy.api.controller;

import com.alibaba.fastjson.JSONObject;
import fsy.interfaces.IUpOrDownloadService;
import fsy.utils.Const;
import fsy.utils.OSSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

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
     * @param upTime 上传时间
     * @param type   类型 0图片，1音频，2 视频 3 其他
     * @param upType 0 pc文件上传，1 手机录音 2，手机录像 3 手机照片 4 现场录音 5实名身份证
     * @param desc   描述
     * @return
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject uploadFile(@RequestParam("file") MultipartFile[] file, Integer userId, String upTime, Integer type, Integer upType, String desc) {
        if (!Const.isNotNull(file[0])) return Const.returnFail("请不要上传空文件");
        for (MultipartFile mult : file) {
            String originalFilename = mult.getOriginalFilename();
            long fileSize = mult.getSize();
            File tempfile = null;
            try {
                if (Const.OSType().contains("windows")) {
                    tempfile = new File(Const.WINDOWS_FILE_PATH+originalFilename);

                }else {
                    tempfile = new File(Const.LINUX_FILE_PATH+originalFilename);
                }
                mult.transferTo(tempfile);

                String uploadPath = OSSUtils.uploadFile(originalFilename, tempfile.getPath(), userId);
                upOrDownService.saveUploadInfo(upTime, type, upType, 1, desc, userId, originalFilename, fileSize, uploadPath);
            } catch (IOException e) {
                e.printStackTrace();
                return Const.returnFail("上传失败!");
            } finally {
                if (tempfile != null) tempfile.delete();
            }
        }
        return Const.returnSuccess("上传成功!");
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
    public Object uploadList(Integer userId, Integer pageNum,Integer type) {
        if (!Const.isNotNull(userId)) return Const.returnFail("id为空");
        List<JSONObject> list = upOrDownService.getUploadListByUserId(userId, pageNum,type);
        int totle = upOrDownService.getUploadListCount(userId,type);
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
        URL url = OSSUtils.downloadFile(uploadById.getString("upload_url"));
        return url;
    }
}
