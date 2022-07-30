package com.A108.Watchme.Controller;


import com.A108.Watchme.Http.ApiResponse;

import com.A108.Watchme.Service.S3Uploader;

import com.A108.Watchme.Service.HomeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

@RestController

public class HomeController {
    @Autowired
    private S3Uploader s3Uploader;
    @Autowired
    private HomeService homeService;


    public HomeController() {
    }

    @GetMapping("/main")
    public ApiResponse root(HttpServletRequest request) {

        return homeService.main();
    }

    @PostMapping("/images")
    public ApiResponse uploadImages(@RequestParam("images") MultipartFile images) throws IOException {
        ApiResponse result = new ApiResponse();
        String url = s3Uploader.upload(images, "Watchme");
        result.setCode(200);
        result.setMessage("SUCCESS UPLOAD");
        result.setResponseData("URL", url);
        return result;
    }
}

