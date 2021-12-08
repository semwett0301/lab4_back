package servlets;

import ejb_beans.Token_EJB;
import ejb_beans.Users_EJB;
import exceptions.DataNotUpdateException;
import exceptions.UserAlreadyExistException;
import org.json.JSONObject;
import utilities.LoginResponse;
import utilities.factories.JsonFactory;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/registration")
public class RegistrationServlet extends HttpServlet {
    @EJB
    Token_EJB token_ejb;

    @EJB
    Users_EJB users_ejb;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JSONObject jsonObject = JsonFactory.getJsonFromRequest(req);


        String username;
        String password;

        try {
            username = jsonObject.getString("username");
            password = jsonObject.getString("password");
        } catch (NullPointerException e) {
            resp.sendError(400, "Полученные данные некорректны");
            return;
        }

        try {
            users_ejb.add(username, password);
            resp.getWriter().write(new JSONObject(new LoginResponse()).toString());
            resp.addCookie(new Cookie("jws", token_ejb.generateToken(username)));
        } catch (DataNotUpdateException e) {
            resp.sendError(500, "При работе с базой данных возникла ошибка, ваши действия не могут быть сохранены");
        } catch (UserAlreadyExistException e) {
            resp.sendError(400, "Пользователь с таким именем уже существует");
        }
    }
}
