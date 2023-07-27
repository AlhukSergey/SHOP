package by.teachmeskills.shop.services;

import by.teachmeskills.shop.domain.User;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

public interface UserService extends BaseService<User> {
    User getUserById(int id);

    User getUserByEmailAndPassword(String email, String password);

    void generateForUpdate(Map<String, String> userData, int id);
    ModelAndView authenticate(User user);

    ModelAndView createUser(User user);

    ModelAndView updateData(User user, Map<String, String> allParams);

    ModelAndView generateAccountPage(User user);
}

