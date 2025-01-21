package com.tekarch.TafUserMS.Services;

import com.tekarch.TafUserMS.Model.UsersDTO;
import com.tekarch.TafUserMS.Services.Interface.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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

    public Optional<UsersDTO> getUserById(Long userId) {
        String url = crudServiceUrl + "/" + userId;
        try {
            return Optional.ofNullable(restTemplate.getForObject(url, UsersDTO.class));
        } catch (HttpClientErrorException.NotFound e) {
            throw new RuntimeException("User with ID " + userId + " not found", e);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new RuntimeException("Failed to fetch user: " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred while fetching user with ID " + userId, e);
        }
    }

    public void updateUser(Long userId, UsersDTO userDetails) {
        String url = crudServiceUrl + "/" + userId;
        try {
            restTemplate.put(url, userDetails);
        } catch (HttpClientErrorException.NotFound e) {
            throw new RuntimeException("User with ID " + userId + " not found", e);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new RuntimeException("Failed to update user: " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred while updating user", e);
        }
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

