package com.tekarch.TafUserMS.Controllers;

import com.tekarch.TafUserMS.Model.UsersDTO;
import com.tekarch.TafUserMS.Services.UserServiceImpl;

import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import lombok.Data;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@Data
public class UserController {

    private final UserServiceImpl usersServiceImpl;

    @PostMapping("/register")
    public ResponseEntity<UsersDTO> registerUser(@RequestBody UsersDTO userobj) {
        try {
            return ResponseEntity.ok(usersServiceImpl.createUser(userobj));
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(null);
        }
    }


    @GetMapping("/{userId}")
    public ResponseEntity<UsersDTO> getUserById(@PathVariable Long userId) {
        Optional<UsersDTO> uid = usersServiceImpl.getUserById(userId);
        return uid.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());

    }



    @GetMapping
    public ResponseEntity<List<UsersDTO>> getAllUsers() {
        try {
            return ResponseEntity.ok(usersServiceImpl.getAllUsers());
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Void> updateUser(@PathVariable Long userId, @RequestBody UsersDTO user) {
        try {
            usersServiceImpl.updateUser(userId, user);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        try {
            usersServiceImpl.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).build();
        }
    }
}