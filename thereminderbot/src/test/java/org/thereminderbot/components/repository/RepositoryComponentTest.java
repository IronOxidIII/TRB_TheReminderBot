package org.thereminderbot.components.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.thereminderbot.repository.RemindRepository;
import org.thereminderbot.repository.TagRepository;
import org.thereminderbot.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;

class RepositoryComponentTest {

    private RepositoryComponent repositoryComponent;

    @BeforeEach
    void setUp() {
        repositoryComponent = new RepositoryComponent();
    }

    @Test
    void constructor_shouldInitializeRepositories() {
        assertNotNull(repositoryComponent.getRemindRepository(), "RemindRepository should be initialized");
        assertNotNull(repositoryComponent.getUserRepository(), "UserRepository should be initialized");
        assertNotNull(repositoryComponent.getTagRepository(), "TagRepository should be initialized");
    }

    @Test
    void getRemindRepository_shouldReturnCorrectInstance() {
        assertTrue(repositoryComponent.getRemindRepository() instanceof RemindRepository,
                "getRemindRepository should return instance of RemindRepository");
    }

    @Test
    void getUserRepository_shouldReturnCorrectInstance() {
        assertTrue(repositoryComponent.getUserRepository() instanceof UserRepository,
                "getUserRepository should return instance of UserRepository");
    }

    @Test
    void getTagRepository_shouldReturnCorrectInstance() {
        assertTrue(repositoryComponent.getTagRepository() instanceof TagRepository,
                "getTagRepository should return instance of TagRepository");
    }
}
