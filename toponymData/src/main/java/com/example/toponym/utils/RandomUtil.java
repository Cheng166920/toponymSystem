package com.example.toponym.utils;


import java.security.SecureRandom;
import java.util.Random;
public class RandomUtil {
    private static final String SYMBOLS = "0123456789abcdefghijklmnopqrstuvwxyz"; // 数字和26个字母组成
    private static final Random RANDOM = new SecureRandom();

    public static String getRandomNumber12() {
        char[] nonceChars = new char[12];  //指定长度为6位/自己可以要求设置

        for (int index = 0; index < nonceChars.length; ++index) {
            nonceChars[index] = SYMBOLS.charAt(RANDOM.nextInt(SYMBOLS.length()));
        }
        return new String(nonceChars);
    }

    /**
     * 获取长度为 6 的随机字母+数字
     * @return 随机数字
     */
    public static String getRandomNumber8() {
        char[] nonceChars = new char[8];  //指定长度为6位/自己可以要求设置

        for (int index = 0; index < nonceChars.length; ++index) {
            nonceChars[index] = SYMBOLS.charAt(RANDOM.nextInt(SYMBOLS.length()));
        }
        return new String(nonceChars);
    }

    public static String getRandomNumber4() {
        char[] nonceChars = new char[4];  //指定长度为6位/自己可以要求设置

        for (int index = 0; index < nonceChars.length; ++index) {
            nonceChars[index] = SYMBOLS.charAt(RANDOM.nextInt(SYMBOLS.length()));
        }
        return new String(nonceChars);
    }
}
