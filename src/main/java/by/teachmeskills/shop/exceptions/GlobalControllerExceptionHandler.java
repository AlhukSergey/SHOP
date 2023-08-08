package by.teachmeskills.shop.exceptions;

import by.teachmeskills.shop.enums.InfoEnum;
import by.teachmeskills.shop.enums.PagesPathEnum;
import by.teachmeskills.shop.enums.RequestParamsEnum;
import org.springframework.http.HttpStatus;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;

@ControllerAdvice
public class GlobalControllerExceptionHandler {
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler({LoginException.class, EntityNotFoundException.class})
    public ModelAndView handlerAuthorisationException(Exception ex) {
        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute(RequestParamsEnum.ERROR_PARAM.getValue(),
                String.format("При попытке входа произошла ошибка: %s.",
                        ex.getMessage()));
        return new ModelAndView(PagesPathEnum.REGISTRATION_OR_LOGIN_ERROR_PAGE.getPath(), modelMap);
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler({RegistrationException.class})
    public ModelAndView handleRegistrationException(Exception ex) {
        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute(RequestParamsEnum.ERROR_PARAM.getValue(), ex.getMessage());
        return new ModelAndView(PagesPathEnum.REGISTRATION_OR_LOGIN_ERROR_PAGE.getPath(), modelMap);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({IncorrectUserDataException.class})
    public ModelAndView handlerIncorrectDataException(Exception ex) {
        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute(RequestParamsEnum.ERROR_PARAM.getValue(),
                String.format("Введены неверные данные: %s. Пожалуйста, попробуйте снова.",
                        ex.getMessage()));
        return new ModelAndView(PagesPathEnum.ERROR_PAGE.getPath(), modelMap);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({SQLException.class})
    public ModelAndView handlerDBConnectionException() {
        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute(RequestParamsEnum.ERROR_PARAM.getValue(), InfoEnum.SERVER_ERROR_INFO.getInfo());
        return new ModelAndView(PagesPathEnum.ERROR_PAGE.getPath(), modelMap);
    }
}
