package by.teachmeskills.shop.services;

import by.teachmeskills.shop.domain.Cart;
import by.teachmeskills.shop.domain.Image;
import by.teachmeskills.shop.domain.Order;
import by.teachmeskills.shop.domain.OrderStatus;
import by.teachmeskills.shop.domain.Product;
import by.teachmeskills.shop.domain.User;
import by.teachmeskills.shop.enums.InfoEnum;
import by.teachmeskills.shop.enums.PagesPathEnum;
import by.teachmeskills.shop.enums.RequestParamsEnum;
import by.teachmeskills.shop.repositories.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static by.teachmeskills.shop.enums.RequestParamsEnum.IMAGES;

@Service
public class CartService {
    private final ProductRepository productRepository;
    private final OrderService orderService;
    private final ImageService imageService;

    public CartService(ProductRepository productRepository, OrderService orderService, ImageService imageService) {
        this.productRepository = productRepository;
        this.orderService = orderService;
        this.imageService = imageService;
    }

    public ModelAndView addProductToCart(String id, Cart shopCart) {
        ModelMap model = new ModelMap();

        int productId = Integer.parseInt(id);
        Product product = productRepository.findById(productId);
        shopCart.addProduct(product);

        model.addAttribute(RequestParamsEnum.PRODUCT.getValue(), product);
        model.addAttribute(RequestParamsEnum.IMAGES.getValue(), imageService.getImagesByProductId(productId));

        return new ModelAndView(PagesPathEnum.PRODUCT_PAGE.getPath(), model);
    }

    public ModelAndView removeProductFromCart(String productId, Cart shopCart) {
        ModelMap model = new ModelMap();

        shopCart.removeProduct(Integer.parseInt(productId));

        List<Product> products = shopCart.getProducts();
        List<List<Image>> images = new ArrayList<>();

        for (Product product : products) {
            images.add(imageService.getImagesByProductId(product.getId()));
        }

        model.addAttribute(RequestParamsEnum.SHOPPING_CART_PRODUCTS.getValue(), products);
        model.addAttribute(IMAGES.getValue(), images.stream().flatMap(Collection::stream).collect(Collectors.toList()));

        return new ModelAndView(PagesPathEnum.SHOPPING_CART_PAGE.getPath(), model);
    }

    public ModelAndView showCartProductList(Cart shopCart) {
        ModelMap model = new ModelMap();

        List<Product> products = shopCart.getProducts();
        List<List<Image>> images = new ArrayList<>();

        for (Product product : products) {
            images.add(imageService.getImagesByProductId(product.getId()));
        }

        model.addAttribute(RequestParamsEnum.SHOPPING_CART_PRODUCTS.getValue(), products);
        model.addAttribute(IMAGES.getValue(), images.stream().flatMap(Collection::stream).collect(Collectors.toList()));

        return new ModelAndView(PagesPathEnum.SHOPPING_CART_PAGE.getPath(), model);
    }

    public ModelAndView checkout(Cart shopCart, User user) {
        ModelMap model = new ModelMap();

        List<Product> productList = shopCart.getProducts();

        if (productList.isEmpty()) {
            model.addAttribute(RequestParamsEnum.INFO.getValue(), InfoEnum.SHOP_CART_IS_EMPTY_INFO.getInfo());
            return new ModelAndView(PagesPathEnum.SHOPPING_CART_PAGE.getPath(), model);
        }

        Order order = Order.builder()
                .userId(user.getId())
                .createdAt(LocalDateTime.now())
                .orderStatus(OrderStatus.ACTIVE)
                .price(shopCart.getTotalPrice())
                .build();

        orderService.create(order);

        shopCart.clear();
        return new ModelAndView(PagesPathEnum.SHOPPING_CART_PAGE.getPath());
    }
}
