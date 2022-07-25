package com.A108.Watchme.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class HomeController {
    private HttpSession httpSession;
    @GetMapping("home")
    public String home(HttpServletRequest request) {
        Map<String, String> map = new LinkedHashMap<>();

        return "home";
    }
}
