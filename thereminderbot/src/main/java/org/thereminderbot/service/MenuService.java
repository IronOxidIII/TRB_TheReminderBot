package org.thereminderbot.service;
import org.thereminderbot.repository.UserRepository;
import org.thereminderbot.enums.UserMenu;
import org.thereminderbot.domain.User;


public class MenuService {
    private final UserRepository userRepository;

    public MenuService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
    * Переключение меню
    */
    public void turnOnMenu(long userId, UserMenu userMenu) {
        User user = userRepository.getUserById(userId);
        user.setCurrentMenuId(userMenu.ordinal());
    }
}
