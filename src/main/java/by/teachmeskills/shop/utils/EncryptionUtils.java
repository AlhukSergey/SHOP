package by.teachmeskills.shop.utils;

import lombok.experimental.UtilityClass;

import java.util.Base64;

@UtilityClass
public class EncryptionUtils {
    public static String encrypt(String password) {
        return Base64.getEncoder().encodeToString(password.getBytes());
    }

    public static String decrypt(String password) {
        return new String(Base64.getDecoder().decode(password));
    }
}