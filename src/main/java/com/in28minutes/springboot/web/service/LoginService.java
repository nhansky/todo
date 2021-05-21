package com.in28minutes.springboot.web.service;

import org.springframework.stereotype.Component;

@Component
public class LoginService {

    public boolean validateUser(String userid, String password) {
        // in28minutes, dummy
        return userid.equalsIgnoreCase("nngo")
                && password.equalsIgnoreCase("123");
    }

}