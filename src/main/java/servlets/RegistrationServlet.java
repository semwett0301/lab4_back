package servlets;

import ejb_beans.Token_EJB;
import ejb_beans.Users_EJB;
import exceptions.DataNotUpdateException;
import exceptions.UserAlreadyExistException;
import org.json.JSONObject;
import utilities.LoginResponse;
import utilities.enum_utilities.LoginProblemEnum;
import utilities.factories.JsonFactory;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/users/registration")
public class RegistrationServlet extends HttpServlet {
    @EJB
    Token_EJB token_ejb;

    @EJB
    Users_EJB users_ejb;

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Hueta");
        JSONObject jsonObject = JsonFactory.getJsonFromRequest(req);

        String username;
        String password;

        try {
            username = jsonObject.getString("username");
            password = jsonObject.getString("password");
        } catch (NullPointerException e) {
            resp.sendError(400, "Полученные данные некорректны");
            e.printStackTrace();
            return;
        }

        System.out.println(username);

        try {
            users_ejb.add(username, password);
            resp.getWriter().write(new JSONObject(new LoginResponse(token_ejb.generateToken(username))).toString());
        } catch (DataNotUpdateException e) {
            resp.sendError(500, "При работе с базой данных возникла ошибка, ваши действия не могут быть сохранены");
            e.printStackTrace();
        } catch (UserAlreadyExistException e) {
            resp.getWriter().write(new JSONObject(new LoginResponse(LoginProblemEnum.LOGIN)).toString());
            e.printStackTrace();
        }
    }
}
