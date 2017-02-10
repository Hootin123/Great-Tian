package com.xtr.manager.controller.operate;

import com.baidu.ueditor.ActionEnter;
import com.xtr.comm.oss.AliOss;
import com.xtr.comm.util.PropertyUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * <p></p>
 *
 * @author 任齐
 * @createTime: 2016/7/18 14:05
 */
@Controller
public class UploadController {

    @RequestMapping("/upload/controller.htm")
    @ResponseBody
    public String upload(HttpServletRequest request){
        String rootPath = request.getRealPath( "/" );
        return new ActionEnter( request, rootPath ).exec();
    }

    /**
     * 上传到oss
     *
     * @param file
     * @return
     */
    @RequestMapping(value = "/upload/oss.htm")
    @ResponseBody
    public Map<String, Object> uploadOss(@RequestParam("file") MultipartFile file){

        Map<String, Object> result = new HashMap<>();
        try {

            String ext = file.getOriginalFilename();

            InputStream in = file.getInputStream();

            String fileName = "thumbnail/" + RandomStringUtils.randomNumeric(18) + "_" + ext;

            String url = PropertyUtils.getString("oss.download.url") + fileName;

            AliOss.uploadFile(in, fileName, PropertyUtils.getString("oss.bucketName.img"));

            result.put("state", true);
            result.put("url", url);

        } catch (IOException e) {
            e.printStackTrace();
            result.put("state", false);
            result.put("msg", "上传失败");
        }
        return result;
    }

}
