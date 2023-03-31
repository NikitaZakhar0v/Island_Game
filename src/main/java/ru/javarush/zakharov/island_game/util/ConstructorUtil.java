package ru.javarush.zakharov.island_game.util;

import java.io.IOException;
import java.util.Properties;

public class ConstructorUtil {
    private static final Properties PROPERTIES = new Properties();

    static {
        loadProperties();
    }

    private ConstructorUtil() {
    }

    private static void loadProperties() {
        try (var inputStream = PropertiesUtil.class.getClassLoader().getResourceAsStream("constructor.properties")) {
            PROPERTIES.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String get(String key) {
        return PROPERTIES.getProperty(key, "0");
    }
}
