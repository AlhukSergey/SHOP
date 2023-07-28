package by.teachmeskills.shop.enums;

import lombok.Getter;

@Getter
public enum PagesPathEnum {
    REGISTRATION_PAGE("registration.html"),
    LOGIN_PAGE("login"),
    HOME_PAGE("home.html"),
    CATEGORY_PAGE("category.html"),
    PRODUCT_PAGE("product.html"),
    SHOPPING_CART_PAGE("shoppingCart.html"),
    MY_PAGE("myPage.html"),
    USER_ACCOUNT_PAGE("user-account.html"),
    SEARCH_PAGE("search.html");

    private final String path;

    PagesPathEnum(String path) {
        this.path = path;
    }

}