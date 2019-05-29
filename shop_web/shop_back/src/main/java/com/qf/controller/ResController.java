package com.qf.controller;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @version 1.0
 * @data 5/20/2019 21:31
 * @user yingyunzhizi
 */
@Controller
@RequestMapping(value = "/res")
public class ResController {

    @Autowired
    private FastFileStorageClient fastFileStorageClient;

    /**
     * 上传图片,可能是多张
     * @param file
     * @return
     */
    @RequestMapping(value = "/uploadImg")
    @ResponseBody
    public Map<String, String> uploadImg(MultipartFile file){

        //获取上传图片名称
        String filename = file.getOriginalFilename();
        //获得上传的图片大小
        long flength = file.getSize();
        //截取后缀
        int index = filename.lastIndexOf(".");
        //因为上传服务器的的后缀不需要点了,所以加一
        String houzhui = filename.substring(index+1);
        Map<String,String> map = new HashMap<>();

        //上传图片到Ffds的服务器
        try {
            StorePath storePath = fastFileStorageClient.uploadImageAndCrtThumbImage(
                    file.getInputStream(),
                    flength,
                    houzhui,
                    null);
            map.put("code","0000");
            map.put("filepath",storePath.getFullPath());
            return map;
        } catch (IOException e) {
            e.printStackTrace();
        }
        map.put("code","0001");
        return map;
        /*//获取上传路径
        String path = ResController.class.getResource("/").getPath() + "static/file";
        //上传文件的流

        try (InputStream in = file.getInputStream();
             OutputStream fos = new FileOutputStream(path + "/" + UUID.randomUUID().toString() + houzhui);
        ) {                
            IOUtils.copy(in,fos);
        }  catch (IOException e) {
            e.printStackTrace();
        }*/
}

}
