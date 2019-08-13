package com.bizseer.auth.controller;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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



}
