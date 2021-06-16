package karmanchik.chtotib.telegrambot.bot.command;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс-перечисление для основных команд чат-бота.
 */
public enum MainCommand {
    // Расписание на следующий день
    COMMAND_1(1),
    // Расписание на эту неделю
    COMMAND_2(2),
    // Узнать расписание педагога/группы
    COMMAND_3(3),
    // Изменить анкету
    COMMAND_4(4);

    private final int val;

    MainCommand(int val) {
        this.val = val;
    }

    /**
     * Вернет коллекцию имен всех команд
     * @return Коллекция имен
     */
    public static List<String> names() {
        return Arrays.stream(values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    public static Object[] vals() {
        return Arrays.stream(values())
                .map(MainCommand::getVal)
                .toArray();
    }

    /**
     * Вернет объект MainCommand
     * @param o Любой объект
     * @return
     */
    public static MainCommand get(Object o) {
        return Arrays.stream(values())
                .filter(command -> command.equals(o) || command.name().equals(o) || command.getVal().equals(o) || command.getVal().toString().equals(o))
                .findFirst().orElse(null);
    }

    /**
     * Проверяет входной объект на соответствие MainCommand
     * @param o Любой объект
     * @return Вернет true, если входной объект соответстует елементу MainCommand, иначе - false
     */
    public static boolean isCommand(Object o) {
        return Arrays.stream(values()).anyMatch(command -> command.equals(o) || command.name().equals(o) || command.getVal().equals(o) || command.getVal().toString().equals(o));
    }

    public Integer getVal() {
        return val;
    }
}
