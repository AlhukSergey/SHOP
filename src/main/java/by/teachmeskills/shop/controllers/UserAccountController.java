package by.teachmeskills.shop.controllers;

import by.teachmeskills.shop.domain.Order;
import by.teachmeskills.shop.domain.OrderStatus;
import by.teachmeskills.shop.domain.PasswordForm;
import by.teachmeskills.shop.domain.User;
import by.teachmeskills.shop.enums.PagesPathEnum;
import by.teachmeskills.shop.enums.RequestParamsEnum;
import by.teachmeskills.shop.exceptions.IncorrectUserDataException;
import by.teachmeskills.shop.services.OrderService;
import by.teachmeskills.shop.services.UserService;
import by.teachmeskills.shop.utils.beanvalidationgroup.UpdateDate;
import jakarta.validation.Valid;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static by.teachmeskills.shop.enums.ShopConstants.USER;

@RestController
@SessionAttributes({USER})
@RequestMapping("/account")
public class UserAccountController {
    private final UserService userService;
    private final OrderService orderService;

    public UserAccountController(UserService userService, OrderService orderService) {
        this.userService = userService;
        this.orderService = orderService;
    }

    @GetMapping
    public ModelAndView openAccountPage(User user) {
        ModelMap model = new ModelMap();

        model.addAttribute(RequestParamsEnum.NAME.getValue(), user.getName());
        model.addAttribute(RequestParamsEnum.SURNAME.getValue(), user.getSurname());
        model.addAttribute(RequestParamsEnum.BIRTHDAY.getValue(), user.getBirthday().toString());
        model.addAttribute(RequestParamsEnum.EMAIL.getValue(), user.getEmail());

        List<Order> orders = orderService.getOrdersByUserId(user.getId());
        model.addAttribute(RequestParamsEnum.ACTIVE_ORDERS.getValue(), orders.stream().filter(order -> order.getOrderStatus() == OrderStatus.ACTIVE).collect(Collectors.toList()));
        model.addAttribute(RequestParamsEnum.FINISHED_ORDERS.getValue(), orders.stream().filter(order -> order.getOrderStatus() == OrderStatus.FINISHED).collect(Collectors.toList()));

        return new ModelAndView(PagesPathEnum.USER_ACCOUNT_PAGE.getPath(), model);
    }


    @PostMapping
    public ModelAndView updateUserData(@ModelAttribute(USER) @Validated(UpdateDate.class) User user, BindingResult bindingResult, ModelAndView modelAndView) {
        if (bindingResult.hasErrors()) {
            populateError("name", modelAndView, bindingResult);
            populateError("surname", modelAndView, bindingResult);
            populateError("birthday", modelAndView, bindingResult);
            populateError("email", modelAndView, bindingResult);

            modelAndView.setViewName(PagesPathEnum.USER_ACCOUNT_PAGE.getPath());
            return modelAndView;
        }

        return userService.updateData(user);
    }

    @PostMapping("/updatePassword")
    public ModelAndView updateUserData(@ModelAttribute("passwordForm") @Valid PasswordForm passwords, BindingResult bindingResult, ModelAndView modelAndView, User user) throws IncorrectUserDataException {
        if (bindingResult.hasErrors()) {
            populateError("oldPassword", modelAndView, bindingResult);
            populateError("newPassword", modelAndView, bindingResult);
            populateError("newPasswordRep", modelAndView, bindingResult);

            modelAndView.setViewName(PagesPathEnum.USER_ACCOUNT_PAGE.getPath());
            return modelAndView;
        }

        return userService.updatePassword(user, passwords);
    }

    @ModelAttribute(USER)
    public User setUpUserForm() {
        return new User();
    }

    private void populateError(String field, ModelAndView modelAndView, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors(field)) {
            modelAndView.addObject(field + "Error", Objects.requireNonNull(bindingResult.getFieldError(field))
                    .getDefaultMessage());
        }
    }
}
