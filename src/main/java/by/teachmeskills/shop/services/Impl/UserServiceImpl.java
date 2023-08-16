package by.teachmeskills.shop.services.Impl;

import by.teachmeskills.shop.domain.Category;
import by.teachmeskills.shop.domain.Image;
import by.teachmeskills.shop.domain.Order;
import by.teachmeskills.shop.domain.OrderStatus;
import by.teachmeskills.shop.domain.PasswordForm;
import by.teachmeskills.shop.domain.User;
import by.teachmeskills.shop.enums.InfoEnum;
import by.teachmeskills.shop.enums.PagesPathEnum;
import by.teachmeskills.shop.enums.RequestParamsEnum;
import by.teachmeskills.shop.enums.ShopConstants;
import by.teachmeskills.shop.exceptions.EntityNotFoundException;
import by.teachmeskills.shop.exceptions.IncorrectUserDataException;
import by.teachmeskills.shop.exceptions.LoginException;
import by.teachmeskills.shop.exceptions.RegistrationException;
import by.teachmeskills.shop.exceptions.UserAlreadyExistsException;
import by.teachmeskills.shop.repositories.CategoryRepository;
import by.teachmeskills.shop.repositories.UserRepository;
import by.teachmeskills.shop.services.CategoryService;
import by.teachmeskills.shop.services.ImageService;
import by.teachmeskills.shop.services.OrderService;
import by.teachmeskills.shop.services.UserService;
import by.teachmeskills.shop.utils.EncryptionUtils;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static by.teachmeskills.shop.enums.RequestParamsEnum.CATEGORIES;
import static by.teachmeskills.shop.enums.RequestParamsEnum.IMAGES;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final CategoryService categoryService;
    private final ImageService imageService;
    private final OrderService orderService;
    private final CategoryRepository categoryRepository;

    public UserServiceImpl(UserRepository userRepository, CategoryService categoryService, ImageService imageService, OrderService orderService, CategoryRepository categoryRepository) {
        this.userRepository = userRepository;
        this.categoryService = categoryService;
        this.imageService = imageService;
        this.orderService = orderService;
        this.categoryRepository = categoryRepository;
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
    public ModelAndView authenticate(User user) throws LoginException, IncorrectUserDataException, EntityNotFoundException {
        ModelMap model = new ModelMap();

        if (user != null && user.getEmail() != null && user.getPassword() != null) {
            User loggedUser = userRepository.findByEmailAndPassword(user.getEmail(), EncryptionUtils.encrypt(user.getPassword()));

            if (loggedUser != null) {
                List<Category> categories = categoryRepository.findPaginatedCategories(0, ShopConstants.PAGE_SIZE);
                List<Image> images = new ArrayList<>();

                for (Category category : categories) {
                    images.add(imageService.getImageByCategoryId(category.getId()));
                }

                Long totalItems = categoryRepository.getTotalItems();
                int totalPages = (int) (Math.ceil(totalItems / ShopConstants.PAGE_SIZE));

                model.addAttribute("currentPage", 1);
                model.addAttribute("totalPages", totalPages);
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
            user.setPassword(EncryptionUtils.encrypt(user.getPassword()));
            User createdUser = create(user);

            if (createdUser != null) {
                ModelMap model = new ModelMap();

                List<Image> images = new ArrayList<>();
                List<Category> categories = categoryRepository.findPaginatedCategories(0, ShopConstants.PAGE_SIZE);

                for (Category category : categories) {
                    images.add(imageService.getImageByCategoryId(category.getId()));
                }

                Long totalItems = categoryRepository.getTotalItems();
                int totalPages = (int) (Math.ceil(totalItems / ShopConstants.PAGE_SIZE));

                model.addAttribute("currentPage", 1);
                model.addAttribute("totalPages", totalPages);
                model.addAttribute(CATEGORIES.getValue(), categories);
                model.addAttribute(IMAGES.getValue(), images);

                model.addAttribute(CATEGORIES.getValue(), categories);
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
        update(user);
        return generateAccountPage(user);
    }

    @Override
    public ModelAndView updatePassword(User user, PasswordForm passwords) throws IncorrectUserDataException {
        if (passwords.getOldPassword().equals(user.getPassword())
                && passwords.getNewPassword().equals(passwords.getNewPasswordRep()) &&
                !passwords.getNewPassword().equals(user.getPassword())) {
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

    private boolean checkUserAlreadyExists(String email, String password){
        try {
            User existUser = getUserByEmailAndPassword(email, password);
            return existUser == null;
        } catch (EntityNotFoundException e) {
            return true;
        }
    }
}
