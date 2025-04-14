package com.buywell.sgg.controller;

import com.buywell.sgg.model.Event;
import com.buywell.sgg.model.Game;
import com.buywell.sgg.model.User;
import com.buywell.sgg.repository.UserRepository;
import com.buywell.sgg.request.UserUpdateRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Value("${spring.data.mongodb.uri}")
    private String mongoUri;

    public UserController(UserRepository userRepository) {
        logger.info("MongoDB URI: {}", mongoUri);
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<User> getAllUsers() {
        // Логируем URI перед выполнением запроса
        logger.info("MongoDB URI: {}", mongoUri);

        return userRepository.findAll();
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable String userId) {
        logger.info("MongoDB URI: {}", mongoUri);
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PatchMapping("/{userId}")
    private User updateUser(@PathVariable String userId, @RequestBody UserUpdateRequest userRequest) {
        logger.info("MongoDB URI: {}", mongoUri);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if(userRequest.getBackground() != null) {
            user.setBackground(userRequest.getBackground());
        }

        if(userRequest.getGame() != null) {
            user.setGame(userRequest.getGame());
        }

        if(userRequest.getItems() != null) {
            user.setItems(userRequest.getItems());
        }

        return userRepository.save(user);
    }

    // Add event to user
    @PatchMapping("/{userId}/events")
    public User addEventToUser(@PathVariable String userId, @RequestBody Event event) {
        logger.info("MongoDB URI: {}", mongoUri);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (user.getEvents() == null) {
            user.setEvents(new ArrayList<>());
        }
        user.getEvents().add(event);
        return userRepository.save(user);
    }

    // Add game to user
    @PatchMapping("/{userId}/games")
    public User addGameToUser(@PathVariable String userId, @RequestBody Game game) {
        logger.info("MongoDB URI: {}", mongoUri);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (user.getGames() == null) {
            user.setGames(new ArrayList<>());
        }
        user.getGames().add(game);
        return userRepository.save(user);
    }

    @DeleteMapping("/{userId}/events/{eventId}")
    public User deleteEventFromUser(@PathVariable String userId, @PathVariable String eventId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + userId));

        if (user.getEvents() != null) {
            boolean removed = user.getEvents().removeIf(game -> game.getId().equals(eventId));
            if (!removed) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found with id: " + eventId);
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User has no events");
        }

        return userRepository.save(user);
    }

    @DeleteMapping("/{userId}/games/{gameId}")
    public User deleteGameFromUser(@PathVariable String userId, @PathVariable String gameId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + userId));

        if (user.getGames() != null) {
            boolean removed = user.getGames().removeIf(game -> game.getId().equals(gameId));
            if (!removed) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found with id: " + gameId);
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User has no games");
        }

        return userRepository.save(user);
    }

}
