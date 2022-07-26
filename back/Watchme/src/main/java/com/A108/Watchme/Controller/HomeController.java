package com.A108.Watchme.Controller;

import com.A108.Watchme.Http.ApiResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @GetMapping("/test")
    public ApiResponse root(HttpServletRequest request){

//        System.out.println("hello1");
        ApiResponse result = new ApiResponse();
//        System.out.println("hello2");
        Map<String, Object> response = new LinkedHashMap<>();
//        System.out.println("hello3");

        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if(!authentication.getAuthorities().equals("ROLE_ANONYMOUS")){
                result.setCode(200);
                result.setMessage("homeview success");





                /*Map<String, Object> studyTime =
                TypedQuery<Member> query = "Select ";

                result.addResult();*/
            }

        } catch(Exception e){
            result.setCode(400);
            result.setMessage("homeview fail");
        } finally {
            result.setResponseData(response);
        }

        return result;
    }
}
