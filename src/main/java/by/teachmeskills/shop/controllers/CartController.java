package by.teachmeskills.shop.controllers;

import by.teachmeskills.shop.domain.Cart;
import by.teachmeskills.shop.domain.User;
import by.teachmeskills.shop.enums.ShopConstants;
import by.teachmeskills.shop.exceptions.EntityNotFoundException;
import by.teachmeskills.shop.services.CartService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import static by.teachmeskills.shop.enums.ShopConstants.SHOPPING_CART;
import static by.teachmeskills.shop.enums.ShopConstants.USER;

@RestController
@SessionAttributes({SHOPPING_CART, USER})
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/add")
    public ModelAndView addProductToCart(@RequestParam(ShopConstants.PRODUCT_ID_PARAM) String id, @ModelAttribute(SHOPPING_CART) Cart shopCart) {
        return cartService.addProductToCart(id, shopCart);
    }

    @GetMapping("/remove")
    public ModelAndView removeProductFromCart(@RequestParam(ShopConstants.PRODUCT_ID_PARAM) String id, @ModelAttribute(SHOPPING_CART) Cart shopCart) throws EntityNotFoundException {
        return cartService.removeProductFromCart(id, shopCart);
    }

    @GetMapping("/open")
    public ModelAndView openShopCart(@ModelAttribute(SHOPPING_CART) Cart shopCart) {
        return cartService.showCartProductList(shopCart);
    }

    @GetMapping("/checkout")
    public ModelAndView checkout(@ModelAttribute(SHOPPING_CART) Cart shopCart, User user) {
        return cartService.checkout(shopCart, user);
    }

    @ModelAttribute(SHOPPING_CART)
    public Cart shoppingCart() {
        return new Cart();
    }
}
