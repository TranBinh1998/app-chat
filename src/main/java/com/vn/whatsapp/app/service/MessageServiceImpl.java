package com.vn.whatsapp.app.service;

import com.vn.whatsapp.app.exception.ChatException;
import com.vn.whatsapp.app.exception.MessageException;
import com.vn.whatsapp.app.exception.UserException;
import com.vn.whatsapp.app.modal.Chat;
import com.vn.whatsapp.app.modal.Message;
import com.vn.whatsapp.app.modal.User;
import com.vn.whatsapp.app.repository.MessageRepository;
import com.vn.whatsapp.app.request.SendmessageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MessageServiceImpl implements MessageService{

    /**
     * Dễ dàng kiểm tra và debug code,
     * có thể truyền vào các mock object cho các dependency khi thực hiện unit testing,
     * hoặc xem rõ ràng các dependency được truyền vào khi khởi tạo đối tượng
     */

    private MessageRepository messageRepository;

    private UserService userService;

    private ChatService chatService;

    @Autowired
    public MessageServiceImpl(MessageRepository messageRepository, UserService userService, ChatService chatService) {
        this.messageRepository = messageRepository;
        this.userService = userService;
        this.chatService = chatService;
    }

    @Override
    public Message sendMessage(SendmessageRequest sendmessageRequest) throws UserException, ChatException {
        User user = userService.findUserById(sendmessageRequest.getUserId());
        Chat chat = chatService.findChatById(sendmessageRequest.getChatId());

        Message message = new Message();

        message.setChat(chat);
        message.setUser(user);
        message.setContent(sendmessageRequest.getContent());
        message.setTimestamp(LocalDateTime.now());

//        messageRepository.save(message);

        return message;
    }

    @Override
    public List<Message> getChatMessages(Integer chatId, User requestUser) throws ChatException, UserException {

        Chat chat = chatService.findChatById(chatId);

        if (!chat.getUsers().contains(requestUser)) {
            throw new UserException ("you are not releted to chat "+chat.getId());
        }

        List<Message> messages = messageRepository.findByChatIdToList(chat.getId());

        return messages;
    }

    @Override
    public Message findMessageById(Integer messageId) throws MessageException {

        Optional<Message> optionalMessage = messageRepository.findById(messageId);
        if (optionalMessage.isPresent()) {
            return optionalMessage.get();
        }
        throw new  MessageException("Message not found with id "+messageId);
    }

    @Override
    public void deleteMessage(Integer messageId,  User requestUser) throws MessageException, UserException {
        Message message = findMessageById(messageId);
        if (message.getUser().getId().equals(requestUser.getId())) {
            messageRepository.deleteById(messageId);
        }
        throw new UserException("You cant delete another user's message "+requestUser.getFullName());
    }
}
