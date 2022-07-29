package com.A108.Watchme.Controller;

import com.A108.Watchme.DTO.RoomCreateDTO;
import com.A108.Watchme.Http.ApiResponse;
import com.A108.Watchme.Service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class HomeController {

    @Autowired
    private HomeService homeService;
    private HttpSession httpSession;


    @GetMapping("home")
    public String home(HttpServletRequest request) {
        Map<String, String> map = new LinkedHashMap<>();

        return "home";
    }



    @GetMapping("/main")
    public ApiResponse root(HttpServletRequest request){
        return homeService.main();
    }


}
