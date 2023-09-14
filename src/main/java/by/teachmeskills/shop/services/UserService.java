package by.teachmeskills.shop.services;

import by.teachmeskills.shop.domain.PasswordForm;
import by.teachmeskills.shop.domain.User;
import by.teachmeskills.shop.exceptions.EntityNotFoundException;
import by.teachmeskills.shop.exceptions.IncorrectUserDataException;
import by.teachmeskills.shop.exceptions.LoginException;
import by.teachmeskills.shop.exceptions.RegistrationException;
import org.springframework.web.servlet.ModelAndView;

public interface UserService extends BaseService<User> {
    User getUserByEmail(String email);

    ModelAndView authenticate(User user) throws LoginException, IncorrectUserDataException, EntityNotFoundException;

    ModelAndView createUser(User user) throws RegistrationException, EntityNotFoundException;

    ModelAndView updateData(User user);

    ModelAndView updatePassword(PasswordForm passwords) throws IncorrectUserDataException;

    ModelAndView generateAccountPage(String userEmail);
}

