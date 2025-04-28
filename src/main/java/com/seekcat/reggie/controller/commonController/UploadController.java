package com.seekcat.reggie.controller.commonController;

import com.seekcat.reggie.common.OSSManage;
import com.seekcat.reggie.common.Result;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RequestMapping("/common")
@RestController
public class UploadController {

    @Value("${oss.base-param.endpoint}")
    private String endpoion;

    @Value("${oss.base-param.down-url}")
    private String downUrl;

    @Resource
    OSSManage ossManage;

    @Resource
    RestTemplate restTemplate;

    /**
     * 上传图片
     * */
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) throws Exception {
        log.warn(file.toString());
//        file.transferTo(new File("D:\\" + file.getOriginalFilename()));
        String fileName = ossManage.uploadImages(file);
        return Result.success(fileName);
    }

    /**
     * 下载图片传到前端
     * */
    @GetMapping("download")
    public Result<String> download(HttpServletResponse httpServletResponse, @RequestParam String name) throws IOException {
        String url = downUrl + name;
        ResponseEntity<byte[]> entity = restTemplate.exchange(url,
                HttpMethod.GET,
                new HttpEntity<>(new HttpHeaders()),
                byte[].class);

        String contentType = entity.getHeaders().getContentType().toString();
        httpServletResponse.setContentType(contentType);

        ServletOutputStream outputStream = httpServletResponse.getOutputStream();

        byte[] body = entity.getBody();

        outputStream.write(body);
        outputStream.flush();
        outputStream.close();
        return Result.success("ok");
    }
}
