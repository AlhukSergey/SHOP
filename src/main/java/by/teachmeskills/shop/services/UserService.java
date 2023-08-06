package by.teachmeskills.shop.services;

import by.teachmeskills.shop.domain.PasswordForm;
import by.teachmeskills.shop.domain.User;
import by.teachmeskills.shop.exceptions.EntityNotFoundException;
import by.teachmeskills.shop.exceptions.IncorrectUserDataException;
import by.teachmeskills.shop.exceptions.LoginException;
import by.teachmeskills.shop.exceptions.RegistrationException;
import jakarta.validation.Valid;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

public interface UserService extends BaseService<User> {
    User getUserById(int id);

    User getUserByEmailAndPassword(String email, String password) throws EntityNotFoundException;

    void generateForUpdate(Map<String, String> userData, int id);
    ModelAndView authenticate(User user) throws LoginException, IncorrectUserDataException, EntityNotFoundException;

    ModelAndView createUser(User user) throws RegistrationException, EntityNotFoundException;

    ModelAndView updateData(User user);

    ModelAndView updatePassword(User user, @Valid PasswordForm passwords) throws IncorrectUserDataException;
    ModelAndView generateAccountPage(User user);
}

