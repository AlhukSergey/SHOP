package by.teachmeskills.shop.services.Impl;

import by.teachmeskills.shop.domain.Category;
import by.teachmeskills.shop.domain.Order;
import by.teachmeskills.shop.domain.OrderStatus;
import by.teachmeskills.shop.domain.PasswordForm;
import by.teachmeskills.shop.domain.User;
import by.teachmeskills.shop.enums.InfoEnum;
import by.teachmeskills.shop.enums.PagesPathEnum;
import by.teachmeskills.shop.enums.RequestParamsEnum;
import by.teachmeskills.shop.enums.ShopConstants;
import by.teachmeskills.shop.exceptions.IncorrectUserDataException;
import by.teachmeskills.shop.exceptions.LoginException;
import by.teachmeskills.shop.exceptions.RegistrationException;
import by.teachmeskills.shop.exceptions.UserAlreadyExistsException;
import by.teachmeskills.shop.repositories.CategoryRepository;
import by.teachmeskills.shop.repositories.UserRepository;
import by.teachmeskills.shop.services.CustomUserDetailsService;
import by.teachmeskills.shop.services.OrderService;
import by.teachmeskills.shop.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final OrderService orderService;
    private final CategoryRepository categoryRepository;
    private final CustomUserDetailsService customUserDetailsService;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, OrderService orderService, CategoryRepository categoryRepository, CustomUserDetailsService customUserDetailsService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.orderService = orderService;
        this.categoryRepository = categoryRepository;
        this.customUserDetailsService = customUserDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User create(User entity) {
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        return userRepository.save(entity);
    }

    @Override
    public void read() {
        userRepository.findAll();
    }

    @Override
    public User update(User entity) {
        return userRepository.save(entity);
    }

    @Override
    public void delete(int id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Пользователя с id %d не найдено.", id)));
        userRepository.delete(user);

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Пользователя с почтой %s не найдено.", email)));
    }

    @Override
    public ModelAndView authenticate(User user) throws LoginException, IncorrectUserDataException {
        if (user != null && user.getEmail() != null && user.getPassword() != null) {
            User loggedUser = userRepository.findByEmailAndPassword(user.getEmail(), user.getPassword());

            if (loggedUser != null) {
                ModelMap model = new ModelMap();

                Pageable paging = PageRequest.of(0, ShopConstants.PAGE_SIZE, Sort.by("name").ascending());
                List<Category> categories = categoryRepository.findAll(paging).getContent();
                long totalItems = categoryRepository.count();
                int totalPages = (int) (Math.ceil((double) totalItems / ShopConstants.PAGE_SIZE));

                model.addAttribute(RequestParamsEnum.PAGE_NUMBER.getValue(), 1);
                model.addAttribute(RequestParamsEnum.PAGE_SIZE.getValue(), ShopConstants.PAGE_SIZE);
                model.addAttribute(RequestParamsEnum.SELECTED_PAGE_SIZE.getValue(), ShopConstants.PAGE_SIZE);
                model.addAttribute("totalPages", totalPages);
                model.addAttribute(RequestParamsEnum.CATEGORIES.getValue(), categories);
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
    public ModelAndView createUser(User user) throws RegistrationException {
        if (checkUserAlreadyExists(user.getEmail())) {
            User createdUser = create(user);

            if (createdUser != null) {
                ModelMap model = new ModelMap();

                Pageable paging = PageRequest.of(0, ShopConstants.PAGE_SIZE, Sort.by("name").ascending());
                List<Category> categories = categoryRepository.findAll(paging).getContent();
                long totalItems = categoryRepository.count();
                int totalPages = (int) (Math.ceil((double) totalItems / ShopConstants.PAGE_SIZE));

                model.addAttribute(RequestParamsEnum.PAGE_NUMBER.getValue(), 1);
                model.addAttribute(RequestParamsEnum.PAGE_SIZE.getValue(), ShopConstants.PAGE_SIZE);
                model.addAttribute(RequestParamsEnum.SELECTED_PAGE_SIZE.getValue(), ShopConstants.PAGE_SIZE);
                model.addAttribute("totalPages", totalPages);
                model.addAttribute(RequestParamsEnum.CATEGORIES.getValue(), categories);
                model.addAttribute(RequestParamsEnum.INFO.getValue(), InfoEnum.WELCOME_INFO.getInfo() + createdUser.getName() + ".");
                model.addAttribute(RequestParamsEnum.USER.getValue(), createdUser);

                return new ModelAndView(PagesPathEnum.HOME_PAGE.getPath(), model);
            }
            throw new RegistrationException("При регистрации нового пользователя произошла ошибка. Попробуйте позже.");
        }
        throw new UserAlreadyExistsException("Пользователь с таким логином уже существует. " +
                "Чтобы войти в аккаунт, перейдите на страницу входа...");
    }

    @Override
    public ModelAndView updateData(User user) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User existingUser = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Пользователя с почтой %s не найдено.", userEmail)));
        existingUser.setName(user.getName());
        existingUser.setSurname(user.getSurname());
        existingUser.setBirthday(user.getBirthday());
        existingUser.setEmail(user.getEmail());
        update(existingUser);

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(existingUser.getEmail());
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        return generateAccountPage(existingUser.getEmail());
    }

    @Override
    public ModelAndView updatePassword(PasswordForm passwords) throws IncorrectUserDataException {
        //Реализация смены пароля без проверки, на то, что новый пароль может быть эквивалентен действующему паролю.
        //Сравниваю только новый пароль и повтор нового пароля. В будущем разобраться с алгоритмом хеширования для возможности декодинга пароля.
        if (passwords.getNewPassword().equals(passwords.getNewPasswordRep())) {
            String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            User existingUser = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new EntityNotFoundException(String.format("Пользователя с почтой %s не найдено.", userEmail)));

            existingUser.setPassword(passwordEncoder.encode(passwords.getNewPassword()));
            update(existingUser);

            UserDetails userDetails = customUserDetailsService.loadUserByUsername(existingUser.getEmail());
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);

            return generateAccountPage(existingUser.getEmail());
        }

        throw new IncorrectUserDataException("Введены некорректные данные: неверный действующий пароль, либо новый пароль и повтор нового пароля не совпадают.");

        /*if (passwords.getOldPassword().equals(user.getPassword())
                && passwords.getNewPassword().equals(passwords.getNewPasswordRep()) &&
                !passwords.getNewPassword().equals(user.getPassword())) {

            String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            User existingUser = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new EntityNotFoundException(String.format("Пользователя с почтой %s не найдено.", userEmail)));

            existingUser.setPassword(passwordEncoder.encode(passwords.getNewPassword()));
            update(existingUser);

            UserDetails userDetails = customUserDetailsService.loadUserByUsername(existingUser.getEmail());
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);

            return generateAccountPage(existingUser.getEmail());
        }

        throw new IncorrectUserDataException("Введены некорректные данные: неверный действующий пароль, либо новый пароль и повтор нового пароля не совпадают.");*/
    }

    @Override
    public ModelAndView generateAccountPage(String userEmail) {
        ModelMap model = new ModelMap();

        User user = getUserByEmail(userEmail);
        model.addAttribute(RequestParamsEnum.USER_ID.getValue(), user.getId());
        model.addAttribute(RequestParamsEnum.NAME.getValue(), user.getName());
        model.addAttribute(RequestParamsEnum.SURNAME.getValue(), user.getSurname());
        model.addAttribute(RequestParamsEnum.BIRTHDAY.getValue(), user.getBirthday().toString());
        model.addAttribute(RequestParamsEnum.EMAIL.getValue(), user.getEmail());

        List<Order> orders = orderService.getOrdersByUserId(user.getId());
        model.addAttribute(RequestParamsEnum.ACTIVE_ORDERS.getValue(), orders.stream().filter(order -> order.getOrderStatus() == OrderStatus.ACTIVE).collect(Collectors.toList()));
        model.addAttribute(RequestParamsEnum.FINISHED_ORDERS.getValue(), orders.stream().filter(order -> order.getOrderStatus() == OrderStatus.FINISHED).collect(Collectors.toList()));

        return new ModelAndView(PagesPathEnum.USER_ACCOUNT_PAGE.getPath(), model);
    }

    private boolean checkUserAlreadyExists(String email) {
        try {
            User existUser = getUserByEmail(email);
            return existUser == null;
        } catch (EntityNotFoundException e) {
            return true;
        }
    }
}
