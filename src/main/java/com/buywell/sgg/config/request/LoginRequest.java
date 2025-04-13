package com.buywell.sgg.config.request;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}