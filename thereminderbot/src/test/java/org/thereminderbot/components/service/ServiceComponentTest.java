package org.thereminderbot.components.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.thereminderbot.service.RemindService;
import org.thereminderbot.service.TagService;
import org.thereminderbot.service.UserService;

import static org.junit.jupiter.api.Assertions.*;

class ServiceComponentTest {

    private ServiceComponent serviceComponent;

    @BeforeEach
    void setUp() {
        serviceComponent = new ServiceComponent();
    }

    @Test
    void constructor_shouldInitializeServices() {
        assertNotNull(serviceComponent.getUserService(), "UserService should be initialized");
        assertNotNull(serviceComponent.getRemindService(), "RemindService should be initialized");
        assertNotNull(serviceComponent.getTagService(), "TagService should be initialized");
    }

    @Test
    void getUserService_shouldReturnCorrectInstance() {
        assertTrue(serviceComponent.getUserService() instanceof UserService,
                "getUserService should return instance of UserService");
    }

    @Test
    void getRemindService_shouldReturnCorrectInstance() {
        assertTrue(serviceComponent.getRemindService() instanceof RemindService,
                "getRemindService should return instance of RemindService");
    }

    @Test
    void getTagService_shouldReturnCorrectInstance() {
        assertTrue(serviceComponent.getTagService() instanceof TagService,
                "getTagService should return instance of TagService");
    }
}
