package by.teachmeskills.shop.controllers;

import by.teachmeskills.shop.domain.PasswordForm;
import by.teachmeskills.shop.domain.User;
import by.teachmeskills.shop.enums.PagesPathEnum;
import by.teachmeskills.shop.exceptions.ExportToFIleException;
import by.teachmeskills.shop.exceptions.IncorrectUserDataException;
import by.teachmeskills.shop.services.OrderService;
import by.teachmeskills.shop.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.Objects;

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
        return userService.generateAccountPage(user);
    }


    @PostMapping
    public ModelAndView updateUserData(@ModelAttribute(USER) @Validated(User.UserUpdate.class) User user, BindingResult bindingResult, ModelAndView modelAndView) {
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

    @PostMapping("/csv/import")
    public ModelAndView uploadCategoriesFromFile(@RequestParam("file") MultipartFile file, User user) {
        return orderService.saveOrdersFromFile(file, user);
    }

    @GetMapping("/csv/export/{userId}")
    public void uploadCategoriesFromBD(HttpServletResponse response, @PathVariable int userId) throws ExportToFIleException {
        orderService.saveUserOrdersFromBD(response, userId);
    }
}
