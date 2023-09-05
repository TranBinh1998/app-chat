package com.vn.whatsapp.app.service;

import com.vn.whatsapp.app.exception.ChatException;
import com.vn.whatsapp.app.exception.UserException;
import com.vn.whatsapp.app.modal.Chat;
import com.vn.whatsapp.app.modal.User;
import com.vn.whatsapp.app.request.GroupChatRequest;

import java.util.List;

public interface ChatService {

    public Chat createChat(User reqUser, Integer userId) throws UserException;

    public Chat findChatById(Integer chatId) throws ChatException;

    public List<Chat> findAllChatByUserId(Integer userId) throws UserException;

    public Chat createGroup(GroupChatRequest request, User requestUser) throws UserException;

    public Chat addUserToGroup(Integer userId, Integer chatId, User requestUser) throws UserException, ChatException;

    public Chat renameGroup(Integer chatId, String groupName, User requestUser) throws ChatException,UserException;

    public Chat removeFromGroup(Integer chatId, Integer userId, User reqUserId) throws ChatException,UserException;

    public void deleteChat(Integer chatId, Integer userId) throws ChatException,UserException;


}
