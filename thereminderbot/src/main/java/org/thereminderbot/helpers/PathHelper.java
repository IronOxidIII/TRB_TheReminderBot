package org.thereminderbot.helpers;

import java.nio.file.Paths;

public class PathHelper {
    /**
     * Путь до корневого каталога проекта.
     */
    public static String RepositoryRootPath = System.getProperty("user.dir");

    /**
     * Путь до конфига.
     */
    public static String ConfigPath = Paths.get(
            RepositoryRootPath, "thereminderbot", ".env").toString();
}
