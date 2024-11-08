package com.swpgavf.back.controller;

import com.swpgavf.back.dto.UserRequestDTO;
import com.swpgavf.back.dto.UserResponseDTO;
import com.swpgavf.back.entity.User;
import com.swpgavf.back.service.IUserService;
import com.swpgavf.back.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final IUserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping
    public ResponseEntity<UserResponseDTO> create(@RequestBody UserRequestDTO userRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(userRequestDTO));
    }

    // Simplified GET method to check username and password
    @GetMapping("/login")
    public ResponseEntity<?> authenticate(@RequestParam String username, @RequestParam String password) {
        User user = userService.authenticate(username, password);

        if (user != null) {
            return ResponseEntity.ok(user);  // Return the user's details if authentication is successful
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }


}
