package com.tekarch.TafUserMS.Controllers;

import com.tekarch.TafUserMS.Model.UsersDTO;
import com.tekarch.TafUserMS.Services.UserServiceImpl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.Data;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

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
            System.out.println("Inside register::" + userobj.getUsername());
            return ResponseEntity.ok(usersServiceImpl.createUser(userobj));
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(500).body(userobj);
        }
    }


    @GetMapping("/{userId}")
    public ResponseEntity<UsersDTO> getUserById(@PathVariable Long userId) {
        UsersDTO user = usersServiceImpl.getUserById(userId);
        if (user == null) {
            // Return 404 if user is not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        // Return 200 OK if user is found
        return ResponseEntity.ok(user);
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
    public ResponseEntity<UsersDTO> updateUser(@PathVariable Long userId, @RequestBody UsersDTO user) {
        try {
            // Attempt to update the user
            UsersDTO updatedUser = usersServiceImpl.updateUser(userId, user);
            return ResponseEntity.ok(updatedUser); // Return the updated user data with 200 OK status
        } catch (HttpClientErrorException.NotFound e) {
            // If user not found, return 404 Not Found
            String errorMessage = "User with ID " + userId + " not found.";
//            logger.error(errorMessage, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (HttpClientErrorException.BadRequest e) {
            // If the request is invalid (e.g., malformed data), return 400 Bad Request
            String errorMessage = "Invalid data provided for the user update.";
//            logger.error(errorMessage, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (HttpServerErrorException.InternalServerError e) {
            // If server error occurs (e.g., database issues), return 500 Internal Server Error
            String errorMessage = "Internal server error occurred while updating user.";
//            logger.error(errorMessage, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            // Catch any other unexpected errors and return 500 Internal Server Error
            String errorMessage = "Unexpected error occurred while updating user.";
//            logger.error(errorMessage, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
//        try {
//            usersServiceImpl.deleteUser(id);
//            return ResponseEntity.noContent().build();
//        } catch (RuntimeException e) {
//            return ResponseEntity.status(500).build();
//        }
//    }
@DeleteMapping("/{id}")
public ResponseEntity<String> deleteUser(@PathVariable Long id) {
    try {
        usersServiceImpl.deleteUser(id);
        return ResponseEntity.noContent().build(); // HTTP 204: No Content
    } catch (UserNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); // HTTP 404: Not Found
    } catch (RuntimeException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An error occurred while deleting the user."); // HTTP 500: Internal Server Error
    }
}

}