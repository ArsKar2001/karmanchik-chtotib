package karmanchik.chtotib.entityservice.repositories;

import com.sun.istack.NotNull;
import karmanchik.chtotib.entityservice.entity.ChatUser;
import karmanchik.chtotib.entityservice.enums.BotState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface JpaChatUserRepository extends JpaRepository<ChatUser, Integer> {
    @Query("SELECT ch FROM ChatUser ch " +
            "LEFT JOIN FETCH ch.group " +
            "LEFT JOIN FETCH ch.teacher " +
            "WHERE ch.chatId = :chatId AND ch.userName = :userName")
    Optional<ChatUser> findByChatIdAndUserName(@NotNull Long chatId, @NotNull String userName);


    List<ChatUser> findAllByBotState(BotState botState);
}
