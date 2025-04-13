package com.buywell.sgg.request;

import lombok.Data;

import java.util.List;

@Data
public class UserUpdateRequest {
    private String background;
    private String game;
    private List<String> items;
}
