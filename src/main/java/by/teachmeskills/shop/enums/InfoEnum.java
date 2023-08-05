package by.teachmeskills.shop.enums;

import lombok.Getter;

@Getter
public enum InfoEnum {
    WELCOME_INFO( "Добро пожаловать, "),
    USER_NOT_FOUND_INFO("Пользователя с такими данными не найдено. Пожалуйста, введите данные повторно либо перейдите на страницу регистрации."),
    INCORRECT_DATA_INFO("Введены некорректные  данные. Попробуйте снова..."),
    SHOP_CART_IS_EMPTY_INFO("В корзине еще нет продуктов. Чтобы оформить заказ, добавьте продукты."),
    DATA_SUCCESSFUL_CHANGED_INFO("Данные успешно изменены. "),
    PASSWORD_INCORRECT_INFO("Введен неверный пароль. Повторите попытку."),
    PRODUCTS_NOT_FOUND_INFO("Продукты не найдены."),
    USER_ALREADY_EXISTS("Пользователь с такими данными уже существует. Перейдите на страницу входа."),
    SERVER_ERROR_INFO("При попытке подключения к базе данных произошла непредвиденная ошибка. Пожалуйста, попробуйте позже.");
    private final String info;

    InfoEnum(String info) {
        this.info = info;
    }
}