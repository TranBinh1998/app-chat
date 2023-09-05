package com.vn.whatsapp.app.service;

import com.vn.whatsapp.app.config.TokenProvider;
import com.vn.whatsapp.app.exception.UserException;
import com.vn.whatsapp.app.modal.User;
import com.vn.whatsapp.app.repository.UserRepository;
import com.vn.whatsapp.app.request.UpdateUserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private TokenProvider tokenProvider;


    @Override
    public User findUserById(Integer id) throws UserException {
         Optional<User> optionalUser = userRepository.findById(id);
         if (optionalUser.isPresent()) {
             return optionalUser.get();
         }
         throw new UserException("User not found with id "+id);
    }



    @Override
    public User findUserByProfile(String jwt) throws UserException {
        String email = tokenProvider.getEmailFromToken(jwt);
        if (email == null) {
            throw new BadCredentialsException("recieved invalid token ===");
        }
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserException("user not found with email "+email);
        }
        return user;
    }

    @Override
    public User updateUser(Integer userId, UpdateUserRequest userReq) throws UserException {
        User user = findUserById(userId);
        if (userReq.getFullName() != null) {
            user.setFullName(userReq.getFullName());
        }
        if (userReq.getProfilePicture() != null) {
            user.setProfile_picture(userReq.getProfilePicture());
        }

        return userRepository.save(user);
    }

    @Override
    public List<User> searchUser(String query) {

        return  userRepository.searchUser(query);
    }
}
