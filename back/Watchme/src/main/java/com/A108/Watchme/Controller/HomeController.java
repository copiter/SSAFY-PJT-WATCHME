package com.A108.Watchme.Controller;


import com.A108.Watchme.Http.ApiResponse;

import com.A108.Watchme.Service.S3Uploader;

import com.A108.Watchme.Service.HomeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

@RestController
public class HomeController {
    @Autowired
    private S3Uploader s3Uploader;
    @Autowired
    private HomeService homeService;


    @GetMapping("/main")
    public ApiResponse root(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        authentication = SecurityContextHolder.getContext().getAuthentication();
        return homeService.main(request, response, authentication);
    }
}

