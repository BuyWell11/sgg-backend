package com.buywell.sgg.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "users")
public class User {
    @Id
    private String id;
    private String name;
    private String logo;
    private String channel;
    private String background;
    private String game;
    private List<String> items;
    @JsonIgnore
    private String passwordHash;
    private List<Game> games;
    private List<Event> events;
}
