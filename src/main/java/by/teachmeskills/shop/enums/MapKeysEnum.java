package by.teachmeskills.shop.enums;

public enum MapKeysEnum {
    ID("id"),
    NAME("name"),
    SURNAME("surname"),
    BIRTHDAY("birthday"),
    EMAIL("email"),
    NEW_PASSWORD("new_password"),
    OLD_PASSWORD("old_password"),
    PASSWORD("password"),
    NEW_PASSWORD_REP("new_password_rep"),
    BALANCE("balance");

    private final String key;

    MapKeysEnum(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}