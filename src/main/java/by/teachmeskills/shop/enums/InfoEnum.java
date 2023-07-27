package by.teachmeskills.shop.enums;

public enum InfoEnum {
    WELCOME_INFO( "Добро пожаловать, "),
    USER_NOT_FOUND_INFO("Логин либо пароль введены неверно. Пожалуйста, введите данные повторно либо перейдите на страницу регистрации."),
    ERROR_DATA_INFO("Введены некорректные  данные. "),
    SHOP_CART_IS_EMPTY_INFO("В корзине еще нет продуктов. Чтобы оформить заказ, добавьте продукты."),
    DATA_SUCCESSFUL_CHANGED_INFO("Данные успешно изменены: "),
    PASSWORD_INCORRECT_INFO("Введен неверный пароль. Повторите попытку."),
    PRODUCTS_NOT_FOUND_INFO("Продукты не найдены.");
    private final String info;

    InfoEnum(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }
}