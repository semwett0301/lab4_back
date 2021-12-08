package servlets;

import ejb_beans.Token_EJB;
import exceptions.NoDataWasReceivedException;
import org.json.JSONObject;
import utilities.LoginResponse;
import utilities.enum_utilities.LoginProblemEnum;
import utilities.factories.JsonFactory;
import utilities.validators.ValidateToken;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/check/login")
public class CheckLoginServlet extends HttpServlet {
    @EJB
    Token_EJB token_ejb;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String jws = "";
        Cookie[] cookies = req.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("jws")) jws = cookie.getValue();
        }

        ValidateToken.validate(resp, jws, token_ejb);
    }
}
