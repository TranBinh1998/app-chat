package com.vn.whatsapp.app.controller;

import com.vn.whatsapp.app.config.JwtConstant;
import com.vn.whatsapp.app.exception.ChatException;
import com.vn.whatsapp.app.exception.UserException;
import com.vn.whatsapp.app.modal.Chat;
import com.vn.whatsapp.app.modal.User;
import com.vn.whatsapp.app.request.GroupChatRequest;
import com.vn.whatsapp.app.request.SingleChatRequest;
import com.vn.whatsapp.app.response.ApiResponse;
import com.vn.whatsapp.app.service.ChatService;
import com.vn.whatsapp.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/chats")
public class ChatController {

    @Autowired
    ChatService chatService;

    @Autowired
    UserService userService;


    // Tạo mới 1 đoạn chat
    @PostMapping("/single")
    public ResponseEntity<Chat> createChatHandler(@RequestBody SingleChatRequest singleChatRequest, @RequestHeader("Authorization") String jwt) throws UserException {

       User requestUser = userService.findUserByProfile(jwt);

        Chat chat = chatService.createChat(requestUser, singleChatRequest.getUserId());

        return new ResponseEntity<Chat>(chat, HttpStatus.OK);
    }


    // Chức năng tạo group chat
    @PostMapping("/group")
    public ResponseEntity<Chat> createGroupChatHandler(@RequestBody GroupChatRequest groupChatRequest, @RequestHeader("Authorization") String jwt) throws UserException {

        User requestUser = userService.findUserByProfile(jwt);

        Chat chat = chatService.createGroup(groupChatRequest, requestUser);

        return new ResponseEntity<Chat>(chat, HttpStatus.OK);
    }



    @GetMapping("/{chatId}")
    public ResponseEntity<Chat> findChatByIdHandler(@PathVariable Integer chatId, @RequestHeader(JwtConstant.JWT_HEADER) String jwt) throws ChatException {

        Chat chat = chatService.findChatById(chatId);

        return new ResponseEntity<Chat>(chat, HttpStatus.OK);
    }


    @PostMapping("/user")
    public ResponseEntity<List<Chat>> findAllChatByUserIdHanler( @RequestHeader("Authorization") String jwt) throws UserException {

        User requestUser = userService.findUserByProfile(jwt);

        List<Chat> chats = chatService.findAllChatByUserId(requestUser.getId());

        return new ResponseEntity<List<Chat>>(chats, HttpStatus.OK);
    }


    @PutMapping("/{chatId}/add/{userId}")
    public ResponseEntity<Chat> addUserToGroupHandler(@PathVariable Integer chatId, @PathVariable Integer userId ,
                                                      @RequestHeader("Authorization") String jwt) throws UserException, ChatException {

        User requestUser = userService.findUserByProfile(jwt);

        Chat chats = chatService.addUserToGroup(userId, chatId, requestUser);

        return new ResponseEntity<Chat>(chats, HttpStatus.OK);
    }


    @PutMapping("/{chatId}/remove/{userId}")
    public ResponseEntity<Chat> removeUserFormGroupHandler(@PathVariable Integer chatId, @PathVariable Integer userId ,
                                                      @RequestHeader("Authorization") String jwt) throws UserException, ChatException {

        User requestUser = userService.findUserByProfile(jwt);

        Chat chats = chatService.removeFromGroup(userId, chatId, requestUser);

        return new ResponseEntity<Chat>(chats, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{chatId}")
    public ResponseEntity<ApiResponse> deleteChatHandler(@PathVariable Integer chatId ,
                                                         @RequestHeader("Authorization") String jwt) throws UserException, ChatException {

        User requestUser = userService.findUserByProfile(jwt);

         chatService.deleteChat(chatId,requestUser.getId());

         ApiResponse apiResponse = new ApiResponse("Chat is deleted successfully", false);
        return new ResponseEntity<ApiResponse>(apiResponse,HttpStatus.OK);
    }




















}
