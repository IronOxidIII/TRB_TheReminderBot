package org.thereminderbot;

import org.thereminderbot.domain.Remind;
import org.thereminderbot.domain.User;
import org.thereminderbot.enums.UserMenu;
import org.thereminderbot.repository.RemindRepository;
import org.thereminderbot.repository.UserRepository;
import org.thereminderbot.service.RemindService;
import org.thereminderbot.service.UserService;
import org.thereminderbot.service.MenuService;
import java.time.Duration;

import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;

public class Main {
    public static void main(String[] args) {
        var userRepository = new UserRepository();
        var remindRepository = new RemindRepository();

        var user1 = new User(1, "Vasya", ZoneId.systemDefault(), UserMenu.MainPage.ordinal());
        var user2 = new User(2, "Misha", ZoneId.systemDefault(), UserMenu.MainPage.ordinal());

        var remind1 = new Remind(
                1,
                "Switch light off",
                user1.getUserId(),
                OffsetDateTime.now(),
                Duration.between(LocalTime.of(0, 0), LocalTime.of(1, 0)));

        var remind2 = new Remind(2,
                "Switch oven off",
                user1.getUserId(),
                OffsetDateTime.now(),
                Duration.ofHours(1));

        var remind3 = new Remind(2,
                "Walkout dog",
                user2.getUserId(),
                OffsetDateTime.now(),
                Duration.ofHours(4).plusMinutes(30));

        var remind4 = new Remind(2,
                "Walkout cat",
                user2.getUserId(),
                OffsetDateTime.now(),
                Duration.ofHours(23));

        remindRepository.addRemind(remind1);
        remindRepository.addRemind(remind2);
        remindRepository.addRemind(remind3);
        remindRepository.addRemind(remind4);

        userRepository.addUser(user1);
        userRepository.addUser(user2);

        var userService = new UserService(userRepository, remindRepository);
        var remindService = new RemindService();

        System.out.println("Заметки первого пользователя:");
        userService.printUsersReminds(user1.getUserId());

        System.out.println("Заметки второго пользователя: \n");
        userService.printUsersReminds(user2.getUserId());

        System.out.println("Пользователю один должно придти уведомление о напоминании(вывести в консоль)");
        userService.notifyUser(user1.getUserId(), remind1.getId());

        System.out.println("Пользователь два переходит в меню настройки");
        MenuService menuService = new MenuService(userRepository);
        menuService.turnOnMenu(user2.getUserId(), UserMenu.RemindCreate);
        userService.printUserInfo(user2.getUserId());

        System.out.println("Пользователи один делится напоминаниями с пользователем два");
        userService.shareReminds(user1.getUserId(), user2.getUserId());
        userService.printUsersReminds(user1.getUserId());
        userService.printUsersReminds(user2.getUserId());

        System.out.println("Выводим все напоминания:");
        remindService.printAllReminds();

        System.out.println("Пользователь один выключает напоминание 1");
        remindService.switchOffRemind(remind1.getId());

        System.out.println("Пользователь два меняет текст напоминания 4");
        remindService.changeRemindText(remind4.getId());

        System.out.println("Пользователь два выводит информацию о заметке 4");
        remindService.printRemindInfo(remind4.getId());
        return;
    }
}
