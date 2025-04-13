package com.buywell.sgg.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class Event {
    private String id = UUID.randomUUID().toString();
    private String name;
    private String comment;
    private LocalDateTime createdAt = LocalDateTime.now();
}
