package com.vn.whatsapp.app.controller;

import com.vn.whatsapp.app.exception.UserException;
import com.vn.whatsapp.app.modal.User;
import com.vn.whatsapp.app.request.UpdateUserRequest;
import com.vn.whatsapp.app.response.ApiResponse;
import com.vn.whatsapp.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<User> getUserProfileHandler(@RequestHeader("Authorization") String token) throws UserException {
        User user = userService.findUserByProfile(token);

        return new ResponseEntity<User>(user, HttpStatus.ACCEPTED);
    }

    @GetMapping("/{query}")
    public ResponseEntity<List<User>> searchUserHandler(@PathVariable("query") String query) {
        List<User> users = userService.searchUser(query);
        return new ResponseEntity<List<User>>(users, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse> updateUserHandler(@RequestBody UpdateUserRequest userRequest, @RequestHeader("Authorization") String token) throws UserException {
        User user = userService.findUserByProfile(token);
        userService.updateUser(user.getId(), userRequest);

        ApiResponse response = new ApiResponse("user updated successfully", true);
        return new ResponseEntity<ApiResponse>(response, HttpStatus.ACCEPTED);

    }


}

