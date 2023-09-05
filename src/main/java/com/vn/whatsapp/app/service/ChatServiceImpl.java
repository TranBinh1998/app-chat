package com.vn.whatsapp.app.service;

import com.vn.whatsapp.app.exception.ChatException;
import com.vn.whatsapp.app.exception.UserException;
import com.vn.whatsapp.app.modal.Chat;
import com.vn.whatsapp.app.modal.User;
import com.vn.whatsapp.app.repository.ChatRepository;
import com.vn.whatsapp.app.request.GroupChatRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChatServiceImpl implements ChatService{

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private UserService userService;


    @Override
    public Chat createChat(User reqUser, Integer userId) throws UserException {

        User user = userService.findUserById(userId);

        Chat isChatExist= chatRepository.findSingleChatByUsers(user, reqUser);

        if (isChatExist!= null) {
            return isChatExist;
        }
        Chat chat = new Chat();
        chat.setCreatedBy(reqUser);
        chat.getUsers().add(user);
        chat.getUsers().add(user);
        chat.setGroup(false);


        return chat;
    }

    @Override
    public Chat findChatById(Integer chatId) throws ChatException {
        Optional<Chat> chat = chatRepository.findById(chatId);
        if (chat.isPresent()) {
            return chat.get();
        }
        throw  new ChatException("Chat not found with id");
    }

    @Override
    public List<Chat> findAllChatByUserId(Integer userId) throws UserException {
        User user = userService.findUserById(userId);
        List<Chat> chats= chatRepository.findChatByUserId(user.getId());


        return chats;
    }

    @Override
    public Chat createGroup(GroupChatRequest requestGroup, User requestUser) throws UserException {
        Chat group = new Chat();
        group.setGroup(true);
        group.setChatImage(requestGroup.getChatImg());
        group.setChatName(requestGroup.getChatName());
        group.setCreatedBy(requestUser);
        group.getAdmins().add(requestUser);

        for (Integer userId : requestGroup.getUserIds()) {
            User user = userService.findUserById(userId);
            group.getUsers().add(user);
        }
        return group;
    }

    @Override
    public Chat addUserToGroup(Integer userId, Integer chatId, User requestUser) throws UserException, ChatException {

        Optional<Chat> optionalChat = chatRepository.findById(chatId);

        User user = userService.findUserById(userId);

        if (optionalChat.isPresent()) {
            Chat chat = optionalChat.get();
            if (chat.getAdmins().contains(requestUser)) {
                chat.getUsers().add(user);
                return chat;
            }
        }else {
                throw new UserException("You are not admin");
        }
        throw new ChatException("chat not found with id : " +chatId);
    }

    @Override
    public Chat renameGroup(Integer chatId, String groupName, User requestUser) throws ChatException, UserException {
        Optional<Chat> optionalChat = chatRepository.findById(chatId);
        if (optionalChat.isPresent()) {
            Chat chat = optionalChat.get();
            if (chat.getUsers().contains(requestUser)) {
                chat.setChatName(groupName);
                return chatRepository.save(chat);
            }
            throw new UserException("You are not member of this group");
        }
        throw new ChatException("Chat not found with id : " +chatId);
    }

    @Override
    public Chat removeFromGroup(Integer chatId, Integer userId, User reqUser) throws ChatException, UserException {
        Optional<Chat> optionalChat = chatRepository.findById(chatId);
        User user = userService.findUserById(userId);
        if (optionalChat.isPresent()) {
            Chat chat = optionalChat.get(); // Lấy đoạn chat ra
            if (chat.getAdmins().contains(reqUser)) { // Nếu là admin xóa chisnh admin đó
                chat.getUsers().remove(user);
                return chatRepository.save(chat);
            }else if (chat.getUsers().contains(reqUser)) {
                if (user.getId().equals(reqUser.getId())) {
                    chat.getUsers().remove(user);
                    return chatRepository.save(chat);
                }
            }else {
                throw new UserException("You are not admin");
            }
        }

        throw new UserException("Chat not found with id "+chatId);
    }

    @Override
    public void deleteChat(Integer chatId, Integer userId) throws ChatException, UserException {

        Optional<Chat> optionalChat = chatRepository.findById(chatId); // find chat by id

        if (optionalChat.isPresent()) {
            Chat chat = optionalChat.get();
            chatRepository.deleteById(chat.getId());
        }
    }
}
