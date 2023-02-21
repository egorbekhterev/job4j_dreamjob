package ru.job4j.dreamjob.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.dreamjob.model.User;
import ru.job4j.dreamjob.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserControllerTest {

    private UserService userService;

    private UserController userController;

    private HttpServletRequest httpServletRequest;

    private HttpSession httpSession;

    @BeforeEach
    public void initServices() {
        userService = mock(UserService.class);
        userController = new UserController(userService);
        httpServletRequest = mock(HttpServletRequest.class);
        httpSession = mock(HttpSession.class);
    }

    @Test
    public void whenRequestForRegistrationPage() {
        var actual = userController.getRegistrationPage();
        assertThat(actual).isEqualTo("users/register");
    }

    @Test
    public void whenRegistrationOfUserIsSuccessful() {
        var user = Optional.of(new User(1, "vasya@gmail.com", "Vasya", "password"));
        var userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        when(userService.save(userArgumentCaptor.capture())).thenReturn(user);

        var model = new ConcurrentModel();
        var view = userController.register(model, user.get());
        var actualUser = userArgumentCaptor.getValue();

        assertThat(view).isEqualTo("redirect:/vacancies");
        assertThat(actualUser).isEqualTo(user.get());
    }

    @Test
    public void whenUserAlreadyExistAndRedirectToErrorPage() {
        var userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        when(userService.save(userArgumentCaptor.capture())).thenReturn(Optional.empty());

        var model = new ConcurrentModel();
        var view = userController.register(model, new User());
        var actualExceptionMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("errors/404");
        assertThat(actualExceptionMessage).isEqualTo("Пользователь с такой почтой уже существует.");
    }

    @Test
    public void whenRequestForLoginPage() {
        var actual = userController.getLoginPage();
        assertThat(actual).isEqualTo("users/login");
    }

    @Test
    public void whenUserLoggingIsSuccessful() {
        var user = Optional.of(new User(1, "vasya@gmail.com", "Vasya", "password"));

        when(userService.findByEmailAndPassword(any(String.class), any(String.class))).thenReturn(user);
        httpSession.setAttribute("user", user.get());
        when(httpServletRequest.getSession()).thenReturn(httpSession);
        when(httpServletRequest.getSession().getAttribute("user")).thenReturn(user.get());

        var model = new ConcurrentModel();
        var view = userController.loginUser(user.get(), model, httpServletRequest);
        var actualUser = (User) httpServletRequest.getSession().getAttribute("user");

        assertThat(view).isEqualTo("redirect:/vacancies");
        assertThat(actualUser).isEqualTo(user.get());
    }

    @Test
    public void whenUserLoggingAndRedirectToErrorPage() {
        when(userService.findByEmailAndPassword(any(String.class), any(String.class))).thenReturn(Optional.empty());

        var model = new ConcurrentModel();
        var view = userController.loginUser(new User(), model, httpServletRequest);
        var actualExceptionMessage = model.getAttribute("error");

        assertThat(view).isEqualTo("users/login");
        assertThat(actualExceptionMessage).isEqualTo("Почта или пароль введены неверно.");
    }

    @Test
    public void whenLogoutRequestIsMade() {
        var view = userController.logout(httpSession);
        assertThat(view).isEqualTo("redirect:/users/login");
    }
}
