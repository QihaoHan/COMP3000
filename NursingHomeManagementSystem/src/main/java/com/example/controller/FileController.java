package com.example.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import com.example.common.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

/**
 * File Interface
 */
@RestController
@RequestMapping("/files")
public class FileController {

    // File upload storage path
    private static final String filePath = System.getProperty("user.dir") + "/files/";

    @Value("${server.port:9999}")
    private String port;

    @Value("${ip:localhost}")
    private String ip;

    /**
     * File upload
     */
    @PostMapping("/upload")
    public Result upload(MultipartFile file) {
        String flag;
        synchronized (FileController.class) {
            flag = System.currentTimeMillis() + "";
            ThreadUtil.sleep(1L);
        }
        String fileName = file.getOriginalFilename();
        try {
            if (!FileUtil.isDirectory(filePath)) {
                FileUtil.mkdir(filePath);
            }
            // File storage form: timestamp-file name
            FileUtil.writeBytes(file.getBytes(), filePath + flag + "-" + fileName);
            System.out.println(fileName + "--upload successful");

        } catch (Exception e) {
            System.err.println(fileName + "--file upload failed");
        }
        String http = "http://" + ip + ":" + port + "/files/";
        return Result.success(http + flag + "-" + fileName);
    }

    /**
     * Rich text file upload
     */
    @PostMapping("/editor/upload")
    public Dict editorUpload(MultipartFile file) {
        String flag;
        synchronized (FileController.class) {
            flag = System.currentTimeMillis() + "";
            ThreadUtil.sleep(1L);
        }
        String fileName = file.getOriginalFilename();
        try {
            if (!FileUtil.isDirectory(filePath)) {
                FileUtil.mkdir(filePath);
            }
            // File storage form: timestamp-file name
            FileUtil.writeBytes(file.getBytes(), filePath + flag + "-" + fileName);
            System.out.println(fileName + "--upload successful");

        } catch (Exception e) {
            System.err.println(fileName + "--file upload failed");
        }
        String http = "http://" + ip + ":" + port + "/files/";
        return Dict.create().set("errno", 0).set("data", CollUtil.newArrayList(Dict.create().set("url", http + flag + "-" + fileName)));
    }


    /**
     * Retrieve file
     *
     * @param flag
     * @param response
     */
    @GetMapping("/{flag}")
    public void avatarPath(@PathVariable String flag, HttpServletResponse response) {
        OutputStream os;
        try {
            if (StrUtil.isNotEmpty(flag)) {
                response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(flag, "UTF-8"));
                response.setContentType("application/octet-stream");
                byte[] bytes = FileUtil.readBytes(filePath + flag);
                os = response.getOutputStream();
                os.write(bytes);
                os.flush();
                os.close();
            }
        } catch (Exception e) {
            System.out.println("File download failed");
        }
    }

    /**
     * Delete file
     *
     * @param flag
     */
    @DeleteMapping("/{flag}")
    public void delFile(@PathVariable String flag) {
        FileUtil.del(filePath + flag);
        System.out.println("File deletion of " + flag + " successful");
    }

}
