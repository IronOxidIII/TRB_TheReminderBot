package org.thereminderbot.service;

import org.thereminderbot.domain.UserSession;
import org.thereminderbot.repository.UserSessionRepository;

public class UserSessionService {
    private final UserSessionRepository userSessionRepository;

    public UserSessionService(UserSessionRepository userSessionRepository) {
        this.userSessionRepository = userSessionRepository;
    }
}
