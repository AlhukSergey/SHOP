package by.teachmeskills.shop.enums;

import lombok.Getter;

@Getter
public enum PagesPathEnum {
    REGISTRATION_PAGE("registration"),
    LOGIN_PAGE("login"),
    HOME_PAGE("home"),
    CATEGORY_PAGE("category"),
    PRODUCT_PAGE("product"),
    SHOPPING_CART_PAGE("shoppingCart"),
    MY_PAGE("myPage"),
    USER_ACCOUNT_PAGE("user-account"),
    SEARCH_PAGE("search"),
    REGISTRATION_OR_LOGIN_ERROR_PAGE("registrationOrLoginError"),
    ERROR_PAGE("error"),
    USER_STATISTIC_PAGE("user-monitoring");

    private final String path;

    PagesPathEnum(String path) {
        this.path = path;
    }

}