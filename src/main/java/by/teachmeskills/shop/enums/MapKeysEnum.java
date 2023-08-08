package by.teachmeskills.shop.enums;

import lombok.Getter;

@Getter
public enum MapKeysEnum {
    ID("id"),
    NAME("name"),
    SURNAME("surname"),
    BIRTHDAY("birthday"),
    EMAIL("email"),
    NEW_PASSWORD("new_password"),
    PASSWORD("password"),
    BALANCE("balance");

    private final String key;

    MapKeysEnum(String key) {
        this.key = key;
    }

}