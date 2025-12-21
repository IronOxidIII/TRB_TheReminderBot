package org.thereminderbot.service;
import org.thereminderbot.repository.UserRepositoryInterface;
import org.thereminderbot.enums.UserMenu;
import org.thereminderbot.domain.User;


public class MenuService {
    private final UserRepositoryInterface userRepository;

    public MenuService(UserRepositoryInterface userRepository) {
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
