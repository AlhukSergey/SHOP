package by.teachmeskills.shop.services;

import by.teachmeskills.shop.domain.User;
import by.teachmeskills.shop.exceptions.IncorrectUserDataException;
import by.teachmeskills.shop.exceptions.LoginException;
import by.teachmeskills.shop.exceptions.UserAlreadyExistsException;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

public interface UserService extends BaseService<User> {
    User getUserById(int id);

    User getUserByEmailAndPassword(String email, String password);

    void generateForUpdate(Map<String, String> userData, int id);
    ModelAndView authenticate(User user) throws LoginException, IncorrectUserDataException;

    ModelAndView createUser(User user) throws UserAlreadyExistsException;

    ModelAndView updateData(User user, Map<String, String> allParams);

    ModelAndView generateAccountPage(User user);
}

