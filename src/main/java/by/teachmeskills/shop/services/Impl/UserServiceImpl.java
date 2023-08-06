package by.teachmeskills.shop.services.Impl;

import by.teachmeskills.shop.domain.Category;
import by.teachmeskills.shop.domain.Image;
import by.teachmeskills.shop.domain.Order;
import by.teachmeskills.shop.domain.OrderStatus;
import by.teachmeskills.shop.domain.PasswordForm;
import by.teachmeskills.shop.domain.User;
import by.teachmeskills.shop.enums.InfoEnum;
import by.teachmeskills.shop.enums.MapKeysEnum;
import by.teachmeskills.shop.enums.PagesPathEnum;
import by.teachmeskills.shop.enums.RequestParamsEnum;
import by.teachmeskills.shop.exceptions.EntityNotFoundException;
import by.teachmeskills.shop.exceptions.IncorrectUserDataException;
import by.teachmeskills.shop.exceptions.LoginException;
import by.teachmeskills.shop.exceptions.RegistrationException;
import by.teachmeskills.shop.exceptions.UserAlreadyExistsException;
import by.teachmeskills.shop.repositories.UserRepository;
import by.teachmeskills.shop.services.CategoryService;
import by.teachmeskills.shop.services.ImageService;
import by.teachmeskills.shop.services.OrderService;
import by.teachmeskills.shop.services.UserService;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static by.teachmeskills.shop.enums.RequestParamsEnum.CATEGORIES;
import static by.teachmeskills.shop.enums.RequestParamsEnum.IMAGES;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final CategoryService categoryService;
    private final ImageService imageService;
    private final OrderService orderService;

    public UserServiceImpl(UserRepository userRepository, CategoryService categoryService, ImageService imageService, OrderService orderService) {
        this.userRepository = userRepository;
        this.categoryService = categoryService;
        this.imageService = imageService;
        this.orderService = orderService;
    }

    @Override
    public User create(User entity) {
        return userRepository.create(entity);
    }

    @Override
    public List<User> read() {
        return userRepository.read();
    }

    @Override
    public User update(User entity) {
        return userRepository.update(entity);
    }

    @Override
    public void delete(int id) {
        userRepository.delete(id);
    }

    @Override
    public User getUserById(int id) {
        return userRepository.findById(id);
    }

    @Override
    public User getUserByEmailAndPassword(String email, String password) throws EntityNotFoundException {
        return userRepository.findByEmailAndPassword(email, password);
    }

    @Override
    public void generateForUpdate(Map<String, String> userData, int id) {
        userRepository.generateUpdateQuery(userData, id);
    }

    @Override
    public ModelAndView authenticate(User user) throws LoginException, IncorrectUserDataException, EntityNotFoundException {
        ModelMap model = new ModelMap();

        if (user != null && user.getEmail() != null && user.getPassword() != null) {
            User loggedUser = userRepository.findByEmailAndPassword(user.getEmail(), user.getPassword());

            if (loggedUser != null) {
                List<Category> categories = categoryService.read();
                List<Image> images = new ArrayList<>();

                for (Category category : categories) {
                    images.add(imageService.getImageByCategoryId(category.getId()));
                }

                model.addAttribute(CATEGORIES.getValue(), categories);
                model.addAttribute(IMAGES.getValue(), images);
                model.addAttribute(RequestParamsEnum.INFO.getValue(), InfoEnum.WELCOME_INFO.getInfo() + loggedUser.getName() + ".");
                model.addAttribute(RequestParamsEnum.USER.getValue(), loggedUser);

                return new ModelAndView(PagesPathEnum.HOME_PAGE.getPath(), model);
            } else {
                throw new LoginException(InfoEnum.USER_NOT_FOUND_INFO.getInfo());
            }
        }
        throw new IncorrectUserDataException(InfoEnum.INCORRECT_DATA_INFO.getInfo());
    }

    @Override
    public ModelAndView createUser(User user) throws RegistrationException, EntityNotFoundException {
        if (checkUserAlreadyExists(user.getEmail(), user.getPassword())) {
            User createdUser = create(user);

            if (createdUser != null) {
                ModelMap model = new ModelMap();

                List<Category> categories = categoryService.read();
                List<Image> images = new ArrayList<>();

                for (Category category : categories) {
                    images.add(imageService.getImageByCategoryId(category.getId()));
                }
                model.addAttribute(CATEGORIES.getValue(), categories);
                model.addAttribute(IMAGES.getValue(), images);
                model.addAttribute(RequestParamsEnum.INFO.getValue(), InfoEnum.WELCOME_INFO.getInfo() + createdUser.getName() + ".");

                model.addAttribute(RequestParamsEnum.INFO.getValue(), InfoEnum.WELCOME_INFO.getInfo() + createdUser.getName() + ".");
                return new ModelAndView(PagesPathEnum.HOME_PAGE.getPath(), model);
            }
            throw new RegistrationException("При регистрации нового пользователя произошла ошибка. Попробуйте позже.");
        }
        throw new UserAlreadyExistsException("Пользователь с таким логином уже существует. " +
                "Чтобы войти в аккаунт, перейдите на страницу входа...");
    }

    @Override
    public ModelAndView updateData(User user) {
        Map<String, String> allParams = new HashMap<>();
        allParams.put(MapKeysEnum.NAME.getKey(), user.getName());
        allParams.put(MapKeysEnum.SURNAME.getKey(), user.getSurname());
        allParams.put(MapKeysEnum.BIRTHDAY.getKey(), user.getBirthday().toString());
        allParams.put(MapKeysEnum.EMAIL.getKey(), user.getEmail());

        generateForUpdate(allParams, user.getId());
        update(user);

        return generateAccountPage(user);
    }

    @Override
    public ModelAndView updatePassword(User user, PasswordForm passwords) throws IncorrectUserDataException {
        if (passwords.getOldPassword().equals(user.getPassword())
                && passwords.getNewPassword().equals(passwords.getNewPasswordRep()) &&
                !passwords.getNewPassword().equals(user.getPassword())) {
            Map<String, String> params = new HashMap<>();
            params.put(MapKeysEnum.NEW_PASSWORD.getKey(), passwords.getNewPassword());
            generateForUpdate(params, user.getId());
            user.setPassword(passwords.getNewPassword());
            update(user);

            return generateAccountPage(user);
        }

        throw new IncorrectUserDataException("Введены некорректные данные: неверный действующий пароль, либо новый пароль и повтор нового пароля не совпадают.");
    }

    @Override
    public ModelAndView generateAccountPage(User user) {
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

    private boolean checkUserAlreadyExists(String email, String password) throws EntityNotFoundException {
        return getUserByEmailAndPassword(email, password) == null;
    }
}
