package karmanchik.chtotib.telegrambot.bot.command;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс-перечисление для основных команд чат-бота.
 */
public enum MainCommand {
    COMMAND_1(1),
    COMMAND_2(2),
    COMMAND_3(3),
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
        for (MainCommand command : values()) {
            if (command.equals(o) || command.name().equals(o) || command.getVal().equals(o) || command.getVal().toString().equals(o))
                return command;
        }
        return null;
    }

    /**
     * Проверяет входной объект на соответствие MainCommand
     * @param o Любой объект
     * @return Вернет true, если входной объект соответстует елементу MainCommand, иначе - false
     */
    public static boolean isCommand(Object o) {
        for (MainCommand command : values()) {
            if (command.equals(o) || command.name().equals(o) || command.getVal().equals(o) || command.getVal().toString().equals(o))
                return true;
        }
        return false;
    }

    public Integer getVal() {
        return val;
    }
}
