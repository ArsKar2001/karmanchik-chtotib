package karmanchik.chtotib.telegrambot.services;

import karmanchik.chtotib.models.entity.ChatUser;
import karmanchik.chtotib.models.enums.BotState;
import karmanchik.chtotib.models.enums.Role;
import karmanchik.chtotib.models.enums.UserState;
import karmanchik.chtotib.models.repositories.JpaChatUserRepository;
import org.springframework.stereotype.Service;

@Service
public class ChatUserService {
    private final JpaChatUserRepository userRepository;

    public ChatUserService(JpaChatUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ChatUser findOrCreate(Long chatId, String username) {
        return userRepository.findByChatIdAndUserName(chatId, username)
                .orElseGet(() -> userRepository.save(ChatUser.builder()
                        .chatId(chatId)
                        .userName(username)
                        .botState(BotState.START)
                        .userState(UserState.NONE)
                        .role(Role.NONE)
                        .build()));
    }

    public ChatUser saveChatUser(ChatUser chatUser) {
        return userRepository.save(chatUser);
    }
}
