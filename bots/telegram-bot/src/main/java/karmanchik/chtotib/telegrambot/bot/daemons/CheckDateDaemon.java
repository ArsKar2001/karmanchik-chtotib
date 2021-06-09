package karmanchik.chtotib.telegrambot.bot.daemons;

import karmanchik.chtotib.entityservice.entity.ChatUser;
import karmanchik.chtotib.entityservice.enums.BotState;
import karmanchik.chtotib.entityservice.repositories.JpaChatUserRepository;
import karmanchik.chtotib.telegrambot.bot.Bot;
import karmanchik.chtotib.telegrambot.util.DateHelperUtils;
import karmanchik.chtotib.telegrambot.util.HelperUtils;
import karmanchik.chtotib.telegrambot.util.TelegramUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Log4j2
@Service("checkDateDaemon")
public class CheckDateDaemon {
    private final JpaChatUserRepository userRepository;
    private final Bot bot;

    @Value("${bot.check-date-period}")
    private int period;

    public CheckDateDaemon(Bot bot, JpaChatUserRepository userRepository) {
        this.bot = bot;
        this.userRepository = userRepository;
    }

//    @PostConstruct
    public void start() {
        log.info("Start {} ...", getClass().getName());
        Executors.newSingleThreadScheduledExecutor()
                .scheduleAtFixedRate(() -> {
                    LocalDate nextDate = DateHelperUtils.getNextSchoolDate(LocalDateTime.now());
                    LocalDate nowDate = LocalDate.now();
                    if (nowDate.isBefore(nextDate)) {
                        List<ChatUser> users = userRepository.findAllByBotState(BotState.AUTHORIZED);
                        users.forEach(chatUser -> {
                            try {
                                bot.execute(
                                        TelegramUtil.createMessageTemplate(chatUser)
                                                .text("Доступно расписание на " +
                                                        "<b>" + nextDate.getDayOfMonth() + " " +
                                                        nextDate.getMonth().getDisplayName(TextStyle.FULL, HelperUtils.getLocale()) + "</b>.\n" +
                                                        "Чтобы узнать - нажми кнопку \"1\".")
                                                .replyMarkup(TelegramUtil.createReplyKeyboardMarkup()
                                                        .keyboardRow(TelegramUtil.createKeyboardRow(new Object[]{"Понятно"}))
                                                        .build())
                                                .build()
                                );
                            } catch (TelegramApiException e) {
                                log.error(e.getMessage(), e);
                            }
                        });
                    }
                }, 0, period, TimeUnit.HOURS);
    }
}
