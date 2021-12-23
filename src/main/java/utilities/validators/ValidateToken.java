package utilities.validators;

import ejb_beans.Token_EJB;
import exceptions.NoDataWasReceivedException;
import org.json.JSONObject;
import utilities.LoginResponse;
import utilities.enum_utilities.LoginProblemEnum;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ValidateToken {
    public static boolean validate(HttpServletResponse resp, String jws, Token_EJB token_ejb) throws IOException {
        try {
            if (token_ejb.checkToken(jws)) {
                resp.getWriter().write(new JSONObject(new LoginResponse(token_ejb.generateToken(token_ejb.getUsernameFromJws(jws)))).toString());
            } else {
                resp.getWriter().write(new JSONObject(new LoginResponse(LoginProblemEnum.JWS)).toString());
                return false;
            }
        } catch (NoDataWasReceivedException e) {
            resp.sendError(500, "При работе с базой данных возникла ошибка, данные не были получены");
            return false;
        }
        return true;
    }
}
