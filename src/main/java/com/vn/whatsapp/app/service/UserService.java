package com.vn.whatsapp.app.service;

import com.vn.whatsapp.app.exception.UserException;
import com.vn.whatsapp.app.modal.User;
import com.vn.whatsapp.app.request.UpdateUserRequest;
import jakarta.servlet.http.PushBuilder;
import org.hibernate.sql.Update;

import java.util.List;

public interface UserService {
    public User findUserById(Integer id) throws UserException;

    public User findUserByProfile(String jwt) throws UserException;

    public User updateUser(Integer userId, UpdateUserRequest userReq) throws UserException;

    public List<User> searchUser(String query);
}
