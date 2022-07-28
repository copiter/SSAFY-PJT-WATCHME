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
    public Map<String, String> home(HttpServletRequest request) {
        Map<String, String> map = new LinkedHashMap<>();
        httpSession=request.getSession(false);
        if(httpSession!=null){
            map.put("email", (String)httpSession.getAttribute("email"));
            map.put("image",(String)httpSession.getAttribute("image"));
            map.put("name",(String)httpSession.getAttribute("name"));
        }
        else{
            throw new RuntimeException("잘못된 접근");
        }
        return map;
    }

    @PostMapping("/addRoom")
    public ApiResponse addRoom(@RequestBody RoomCreateDTO roomCreateDTO){
        return homeService.addRoom(roomCreateDTO);
    }

    @GetMapping("/MainPage")
    public ApiResponse root(HttpServletRequest request){
        return homeService.mainPage();
    }


}
