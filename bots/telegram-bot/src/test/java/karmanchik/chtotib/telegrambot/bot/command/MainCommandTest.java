package karmanchik.chtotib.telegrambot.bot.command;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MainCommandTest {
    @Test
    void test_getNames() {
        System.out.println(MainCommand.names());
    }

    @Test
    void test_isCommand() {
        String command_1 = "COMMAND_1";
        int command_2 = 2;

        System.out.println(command_1 + " - " + MainCommand.isCommand(command_1));
        System.out.println(command_2 + " - " + MainCommand.isCommand(command_2));
        System.out.println(MainCommand.COMMAND_3 + " - " + MainCommand.isCommand(MainCommand.COMMAND_3));
    }

    @Test
    void test_getCommand() {
        String command_1 = "COMMAND_1";
        int command_2 = 2;

        System.out.println(command_1 + " - " + MainCommand.get(command_1));
        System.out.println(command_2 + " - " + MainCommand.get(command_2));
        System.out.println(MainCommand.COMMAND_3 + " - " + MainCommand.get(MainCommand.COMMAND_3));
    }
}