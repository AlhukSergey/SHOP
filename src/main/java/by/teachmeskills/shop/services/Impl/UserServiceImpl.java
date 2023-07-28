package by.teachmeskills.shop.services.Impl;

import by.teachmeskills.shop.domain.Category;
import by.teachmeskills.shop.domain.Image;
import by.teachmeskills.shop.domain.Order;
import by.teachmeskills.shop.domain.OrderStatus;
import by.teachmeskills.shop.domain.User;
import by.teachmeskills.shop.enums.InfoEnum;
import by.teachmeskills.shop.enums.MapKeysEnum;
import by.teachmeskills.shop.enums.PagesPathEnum;
import by.teachmeskills.shop.enums.RequestParamsEnum;
import by.teachmeskills.shop.enums.SetterActionsEnum;
import by.teachmeskills.shop.exceptions.IncorrectUserDataException;
import by.teachmeskills.shop.exceptions.LoginException;
import by.teachmeskills.shop.exceptions.RequestCredentialsNullException;
import by.teachmeskills.shop.exceptions.UserAlreadyExistsException;
import by.teachmeskills.shop.repositories.UserRepository;
import by.teachmeskills.shop.services.CategoryService;
import by.teachmeskills.shop.services.ImageService;
import by.teachmeskills.shop.services.OrderService;
import by.teachmeskills.shop.services.UserService;
import by.teachmeskills.shop.utils.HttpRequestCredentialsValidator;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import static by.teachmeskills.shop.enums.RequestParamsEnum.CATEGORIES;
import static by.teachmeskills.shop.enums.RequestParamsEnum.IMAGES;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final CategoryService categoryService;
    private final ImageService imageService;
    private final OrderService orderService;

    private final Map<String, BiConsumer<String, User>> settersMap = Map.of(
            MapKeysEnum.NAME.getKey(), SetterActionsEnum.NAME_ACTION.getAction(),
            MapKeysEnum.SURNAME.getKey(), SetterActionsEnum.SURNAME_ACTION.getAction(),
            MapKeysEnum.BIRTHDAY.getKey(), SetterActionsEnum.BIRTHDAY_ACTION.getAction(),
            MapKeysEnum.EMAIL.getKey(), SetterActionsEnum.EMAIL_ACTION.getAction(),
            MapKeysEnum.NEW_PASSWORD.getKey(), SetterActionsEnum.PASSWORD_ACTION.getAction());

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
    public User getUserByEmailAndPassword(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password);
    }

    @Override
    public void generateForUpdate(Map<String, String> userData, int id) {
        userRepository.generateUpdateQuery(userData, id);
    }

    @Override
    public ModelAndView authenticate(User user) throws LoginException, IncorrectUserDataException {
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
    public ModelAndView createUser(User user) throws UserAlreadyExistsException {
        ModelAndView modelAndView = new ModelAndView();
        ModelMap model = new ModelMap();

        if (user != null
                && user.getName() != null
                && user.getSurname() != null
                && user.getBirthday() != null
                && user.getEmail() != null
                && user.getPassword() != null) {

            try {
                HttpRequestCredentialsValidator.validateUserBirthday(user);
                checkUserAlreadyExists(user.getEmail(), user.getPassword());

                User createdUser = create(user);

                if (createdUser != null) {
                    List<Category> categories = categoryService.read();
                    List<Image> images = new ArrayList<>();

                    for (Category category : categories) {
                        images.add(imageService.getImageByCategoryId(category.getId()));
                    }
                    model.addAttribute(CATEGORIES.getValue(), categories);
                    model.addAttribute(IMAGES.getValue(), images);
                    model.addAttribute(RequestParamsEnum.INFO.getValue(), InfoEnum.WELCOME_INFO.getInfo() + createdUser.getName() + ".");

                    model.addAttribute(RequestParamsEnum.INFO.getValue(), InfoEnum.WELCOME_INFO.getInfo() + createdUser.getName() + ".");
                    modelAndView.setViewName(PagesPathEnum.HOME_PAGE.getPath());
                    modelAndView.addAllObjects(model);
                } else {
                    model.addAttribute(RequestParamsEnum.INFO.getValue(), InfoEnum.USER_NOT_FOUND_INFO.getInfo());
                    modelAndView.setViewName(PagesPathEnum.LOGIN_PAGE.getPath());
                }
            } catch (IncorrectUserDataException e) {
                model.addAttribute(RequestParamsEnum.INFO.getValue(), InfoEnum.INCORRECT_DATA_INFO.getInfo() + e.getMessage());
                modelAndView.setViewName(PagesPathEnum.REGISTRATION_PAGE.getPath());
            } catch (UserAlreadyExistsException e) {
                throw new UserAlreadyExistsException(InfoEnum.USER_ALREADY_EXISTS.getInfo());
            }
        }
        return modelAndView;
    }

    @Override
    public ModelAndView updateData(User user, Map<String, String> allParams) {
        ModelMap model = new ModelMap();
        try {
            if (allParams.containsKey(MapKeysEnum.NEW_PASSWORD.getKey())) {
                HttpRequestCredentialsValidator.validatePasswords(allParams, user);
            } else {
                HttpRequestCredentialsValidator.validateUserBirthday(user);
            }
        } catch (IncorrectUserDataException | RequestCredentialsNullException e) {

            model.addAttribute(RequestParamsEnum.INFO.getValue(), e.getMessage());
            return new ModelAndView(PagesPathEnum.USER_ACCOUNT_PAGE.getPath(), model);
        }

        setNewUserData(allParams, user);

        generateForUpdate(allParams, user.getId());
        update(user);

        model.addAttribute(RequestParamsEnum.NAME.getValue(), user.getName());
        model.addAttribute(RequestParamsEnum.SURNAME.getValue(), user.getSurname());
        model.addAttribute(RequestParamsEnum.BIRTHDAY.getValue(), user.getBirthday().toString());
        model.addAttribute(RequestParamsEnum.EMAIL.getValue(), user.getEmail());

        List<Order> orders = orderService.getOrdersByUserId(user.getId());
        model.addAttribute(RequestParamsEnum.ACTIVE_ORDERS.getValue(), orders.stream().filter(order -> order.getOrderStatus() == OrderStatus.ACTIVE).collect(Collectors.toList()));
        model.addAttribute(RequestParamsEnum.FINISHED_ORDERS.getValue(), orders.stream().filter(order -> order.getOrderStatus() == OrderStatus.FINISHED).collect(Collectors.toList()));

        model.addAttribute(RequestParamsEnum.INFO.getValue(), InfoEnum.DATA_SUCCESSFUL_CHANGED_INFO.getInfo());
        return new ModelAndView(PagesPathEnum.USER_ACCOUNT_PAGE.getPath(), model);
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

    private void checkUserAlreadyExists(String email, String password) throws UserAlreadyExistsException {
        User user = getUserByEmailAndPassword(email, password);
        if (user != null) {
            throw new UserAlreadyExistsException("Пользователь с таким логином уже существует. " +
                    "Чтобы войти в аккаунт, перейдите на страницу входа...");
        }
    }

    private void setNewUserData(Map<String, String> userData, User user) {
        List<String> userFieldsNames = Arrays.stream(user.getClass().getDeclaredFields()).map(Field::getName).toList();
        Set<String> keys = userData.keySet();
        for (String name : userFieldsNames) {
            if (keys.contains(name)) {
                settersMap.get(name).accept(userData.get(name), user);
            }
        }
    }
}
