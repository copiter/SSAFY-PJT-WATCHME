package com.A108.Watchme.Controller;

import antlr.Tool;
import com.A108.Watchme.Http.ApiResponse;
import com.A108.Watchme.Repository.*;
import com.A108.Watchme.Service.HomeService;
import com.A108.Watchme.VO.Entity.member.Member;
import com.A108.Watchme.VO.Entity.member.MemberInfo;
import com.A108.Watchme.VO.Entity.room.Room;
import com.A108.Watchme.VO.Entity.room.RoomInfo;
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
    private MemberRepository memberRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private MRLRepository mrlRepository;
    @Autowired
    private MemberInfoRepository memberInfoRepository;
    @Autowired
    private RoomInfoRepository roomInfoRepository;
    @Autowired
    private HomeService homeService;


    @GetMapping("/main")
    public ApiResponse root(HttpServletRequest request){

        return homeService.main();
    }

}
