package com.tekarch.TafUserMS.Services;

import com.tekarch.TafUserMS.Model.UsersDTO;
import com.tekarch.TafUserMS.Services.Interface.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class UserServiceImpl implements UsersService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${datastoreServiceUrl}")
    private String crudServiceUrl;


    public UsersDTO createUser(UsersDTO user) {
        String url = crudServiceUrl ;
        try {
            return restTemplate.postForObject(url, user, UsersDTO.class);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new RuntimeException("Failed to create user: " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred while creating user", e);
        }
    }

    public List<UsersDTO> getAllUsers(){
        try {
            // Get the response as an array of User objects
            UsersDTO[] usersArray = restTemplate.getForObject(crudServiceUrl, UsersDTO[].class);
            return Arrays.asList(usersArray);  // Convert the array to a list
        } catch (HttpClientErrorException.NotFound e) {
            throw new RuntimeException("No users found: " + e.getMessage(), e);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new RuntimeException("Failed to fetch users: " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred while fetching users", e);
        }
    }


    public UsersDTO getUserById(Long userId) {
        try {
            // Use RestTemplate to fetch user data from the external service
            return restTemplate.getForObject(crudServiceUrl+"/" + userId, UsersDTO.class);
        } catch (HttpClientErrorException.NotFound e) {
            // Return null when user is not found (404)
            return null;
        } catch (Exception e) {
            // Catch other exceptions and log them if necessary
            throw new RuntimeException("Error while fetching user with ID " + userId, e);
        }
    }


//    public void updateUser(Long userId, UsersDTO userDetails) {
//        String url = crudServiceUrl + "/" + userId;
//        try {
//            restTemplate.put(url, userDetails);
//        } catch (HttpClientErrorException.NotFound e) {
//            throw new RuntimeException("User with ID " + userId + " not found", e);
//        } catch (HttpClientErrorException | HttpServerErrorException e) {
//            throw new RuntimeException("Failed to update user: " + e.getResponseBodyAsString(), e);
//        } catch (Exception e) {
//            throw new RuntimeException("An unexpected error occurred while updating user", e);
//        }
//    }

    public UsersDTO updateUser(Long userId, UsersDTO userDetails) {
        String url = crudServiceUrl + "/" + userId;
        try {
            restTemplate.put(url, userDetails);
        } catch (HttpClientErrorException.NotFound e) {
            // Handle 404 error: User not found
            String errorMessage = "User with ID " + userId + " not found.";
            // Log the error for debugging purposes
//        logger.error(errorMessage, e);
            throw new RuntimeException(errorMessage, e);
        } catch (HttpClientErrorException e) {
            // Handle other client errors (e.g., 400 Bad Request, 403 Forbidden, etc.)
            String errorMessage = "Client error occurred while updating user: " + e.getResponseBodyAsString();
            // Log the error for debugging purposes
//        logger.error(errorMessage, e);
            throw new RuntimeException(errorMessage, e);
        } catch (HttpServerErrorException e) {
            // Handle server errors (e.g., 500 Internal Server Error)
            String errorMessage = "Server error occurred while updating user: " + e.getResponseBodyAsString();
            // Log the error for debugging purposes
//        logger.error(errorMessage, e);
            throw new RuntimeException(errorMessage, e);
        } catch (Exception e) {
            // Handle unexpected errors
            String errorMessage = "An unexpected error occurred while updating user.";
            // Log the error for debugging purposes
//        logger.error(errorMessage, e);
            throw new RuntimeException(errorMessage, e);
        }
        return userDetails;
    }

    public void deleteUser(Long userId) {
        String url = crudServiceUrl + "/" + userId;
        try {
            restTemplate.delete(url);
        } catch (HttpClientErrorException.NotFound e) {
            throw new RuntimeException("User with ID " + userId + " not found", e);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new RuntimeException("Failed to delete user: " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred while deleting user", e);
        }
    }

}

