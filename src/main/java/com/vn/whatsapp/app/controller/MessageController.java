package com.vn.whatsapp.app.controller;

import com.vn.whatsapp.app.config.JwtConstant;
import com.vn.whatsapp.app.exception.ChatException;
import com.vn.whatsapp.app.exception.UserException;
import com.vn.whatsapp.app.modal.Message;
import com.vn.whatsapp.app.modal.User;
import com.vn.whatsapp.app.request.SendmessageRequest;
import com.vn.whatsapp.app.service.MessageService;
import com.vn.whatsapp.app.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    // hạn chế phụ thuộc vào spring container

    private MessageService messageService;

    private UserService userService;

    public MessageController(MessageService messageService, UserService userService) {
        this.messageService = messageService;
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<Message> sendMessageHandler(@RequestBody SendmessageRequest sendMessageRequest,
                                                      @RequestHeader(JwtConstant.JWT_HEADER) String jwt) throws UserException, ChatException {

        User user = userService.findUserByProfile(jwt);

        sendMessageRequest.setUserId(user.getId());
        Message message = messageService.sendMessage(sendMessageRequest);

        return new ResponseEntity<>(message, HttpStatus.OK);
    }


    @PostMapping("/chat/{chatId}")
    public ResponseEntity<Message> getChatsMessageHandler(@PathVariable Integer chatId,
                                                      @RequestHeader(JwtConstant.JWT_HEADER) String jwt) throws UserException, ChatException {

        User user = userService.findUserByProfile(jwt);

        sendMessageRequest.setUserId(user.getId());
        Message message = messageService.sendMessage(sendMessageRequest);

        return new ResponseEntity<>(message, HttpStatus.OK);
    }


}
