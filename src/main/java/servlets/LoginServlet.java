package servlets;

import ejb_beans.Token_EJB;
import ejb_beans.Users_EJB;
import exceptions.NoDataWasReceivedException;
import org.json.JSONObject;
import utilities.LoginResponse;
import utilities.enum_utilities.LoginProblemEnum;
import utilities.factories.JsonFactory;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
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
            if (!users_ejb.checkUserExist(username)) {
                resp.getWriter().write(new JSONObject(new LoginResponse(LoginProblemEnum.LOGIN)).toString());
                return;
            } else {
                if (!users_ejb.checkUserPassword(username, password)) {
                    resp.getWriter().write(new JSONObject(new LoginResponse(LoginProblemEnum.PASSWORD)).toString());
                    return;
                }
            }

            resp.getWriter().write(new JSONObject(new LoginResponse(token_ejb.generateToken(username))).toString());

        } catch (NoDataWasReceivedException e) {
            resp.sendError(500, "При работе с базой данных возникла ошибка, данные не были получены");
        }
    }
}
