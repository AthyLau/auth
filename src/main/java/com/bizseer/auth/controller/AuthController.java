package com.bizseer.auth.controller;

import com.bizseer.auth.service.AuthService;
import com.bizseer.auth.util.exception.AuthException;
import com.bizseer.auth.util.http.JsonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Function:
 *
 * @author liubing
 * Date: 2019/8/7 10:47 AM
 * @since JDK 1.8
 */
@RestController
@RequestMapping("/api/auth")
@ConditionalOnProperty(name = "security.auth.enable", havingValue = "true")
public class AuthController extends AutoLogController{

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public JsonResponse login(@RequestBody Map<String,Object> loginUser)throws AuthException {
        Map<String,Object> ret = authService.login(loginUser);
        if(ret==null){
            return JsonResponse.BAD_REQUEST;
        }
        return JsonResponse.success(ret);
    }

    @PostMapping("/register")
    public JsonResponse register(@RequestHeader("Authorization") String jwtToken, @RequestBody Map<String,String> registerUser)throws AuthException {
        return JsonResponse.success(authService.register(jwtToken, registerUser));
    }

    @GetMapping("/test")
    public JsonResponse test(){
        return JsonResponse.success("success");
    }

}
