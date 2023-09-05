package com.vn.whatsapp.app.repository;

import com.vn.whatsapp.app.modal.Chat;
import com.vn.whatsapp.app.modal.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Integer> {

    @Query("SELECT c from Chat  c join c.users u where u.id = :userId")
    public List<Chat> findChatByUserId(@Param("userId") Integer userId);



    /*
    Sử dụng toán tử member of để kiểm tra xem user và reqUser có thuộc về danh sách users của Chat hay không.
    Danh sách users là một thuộc tính của lớp Chat, chứa các đối tượng User tham gia vào chat.
     */
    @Query("select c from Chat c where c.isGroup=false  " +
            " and :user member of c.users " +
            "and :reqUser member of c.users")
    public Chat findSingleChatByUsers(@Param("user")User user, @Param("reqUser") User reqUser);


}
