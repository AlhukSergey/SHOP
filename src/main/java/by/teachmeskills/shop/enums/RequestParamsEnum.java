package by.teachmeskills.shop.enums;

import lombok.Getter;

@Getter
public enum RequestParamsEnum {
    COMMAND("command"),
    NAME("name"),
    SURNAME("surname"),
    BIRTHDAY("birthday"),
    EMAIL("email"),
    LOGIN("email"),
    PASSWORD("password"),
    USER("user"),
    INFO("info"),
    CATEGORIES("categories"),
    IMAGES("images"),
    CATEGORY("category"),
    CATEGORY_ID("category_id"),
    PRODUCTS("products"),
    PRODUCT("product"),
    PRODUCT_ID("product_id"),
    SHOPPING_CART("shopping-cart"),
    SHOPPING_CART_PRODUCTS("cartProductsList"),
    OLD_PASSWORD("old_password"),
    NEW_PASSWORD("new_password"),
    NEW_PASSWORD_REP("new_password_rep"),
    ORDERS("orders"),
    ACTIVE_ORDERS("activeOrders"),
    FINISHED_ORDERS("finishedOrders"),
    SEARCH_PARAM("search_param"),;

    private final String value;

    RequestParamsEnum(String value) {
        this.value = value;
    }

}