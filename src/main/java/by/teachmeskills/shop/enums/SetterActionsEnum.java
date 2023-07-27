package by.teachmeskills.shop.enums;

import by.teachmeskills.shop.domain.User;

import java.time.LocalDate;
import java.util.function.BiConsumer;

public enum SetterActionsEnum {
    NAME_ACTION((name, user) -> user.setName(name)),
    SURNAME_ACTION((surname, user) -> user.setSurname(surname)),
    BIRTHDAY_ACTION((birthday, user) -> user.setBirthday(LocalDate.parse(birthday))),
    EMAIL_ACTION((email, user) -> user.setEmail(email)),
    PASSWORD_ACTION((password, user) -> user.setPassword(password));

    private final BiConsumer<String, User> action;

    SetterActionsEnum(BiConsumer<String, User> action) {
        this.action = action;
    }

    public BiConsumer<String, User> getAction() {
        return action;
    }
}