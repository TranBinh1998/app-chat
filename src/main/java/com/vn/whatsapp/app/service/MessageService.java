package com.vn.whatsapp.app.service;

import com.vn.whatsapp.app.exception.ChatException;
import com.vn.whatsapp.app.exception.MessageException;
import com.vn.whatsapp.app.exception.UserException;
import com.vn.whatsapp.app.modal.Message;
import com.vn.whatsapp.app.modal.User;
import com.vn.whatsapp.app.request.SendmessageRequest;

import java.util.List;

public interface MessageService {
    // Lấy dto từ request gửi
    public Message sendMessage(SendmessageRequest sendmessageRequest) throws UserException, ChatException;

    // Lấy tin nhắn theo chatid;
    public List<Message> getChatMessages(Integer chatId, User requestUser) throws ChatException, UserException;


    public Message findMessageById(Integer messageId) throws MessageException;

    public void deleteMessage(Integer messageId, User requestUser) throws MessageException, UserException;

}
