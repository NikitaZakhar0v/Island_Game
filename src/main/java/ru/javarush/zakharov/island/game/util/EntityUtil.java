package ru.javarush.zakharov.island.game.util;

import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class EntityUtil {
    private static final Properties PROPERTIES = new Properties();

    static {
        loadObjects();
    }

    private EntityUtil() {
    }

    private static void loadObjects() {
        try (var inputStream = PropertiesUtil.class.getClassLoader().getResourceAsStream("countAnimal.properties")) {
            PROPERTIES.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<String> getAllEntity() {
        return PROPERTIES.keySet().stream().map(o -> (String) o).collect(Collectors.toList());
    }

    public static int getCount(String key) {
        return Integer.parseInt(PROPERTIES.getProperty(key,"0"));
    }
}
