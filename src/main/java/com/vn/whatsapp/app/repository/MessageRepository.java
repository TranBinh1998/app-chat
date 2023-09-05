package com.vn.whatsapp.app.repository;

import com.vn.whatsapp.app.modal.Chat;
import com.vn.whatsapp.app.modal.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {


    @Query("select m from Message m join m.chat c where c.id=:chatId")
    List<Message> findByChatIdToList(@Param("chatId") Integer chatId);

    /*
    @Query("select m from Message m left join Chat  c on c.id = m.chat.id where c.id=:chatId")
    List<Message> findListByChatId(@Param("chatId") Integer chatId);

     */



}
