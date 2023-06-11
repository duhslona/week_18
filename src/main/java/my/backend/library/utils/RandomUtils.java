package my.backend.library.utils;

import java.util.Random;

public class RandomUtils {

    public static String getRandomString(int length) {
        return new Random().ints(97, 122 + 1)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
