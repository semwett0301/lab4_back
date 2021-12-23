package servlets;

import ejb_beans.Token_EJB;
import org.json.JSONObject;
import utilities.validators.ValidateToken;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

@WebServlet("/users/checking")
public class CheckLoginServlet extends HttpServlet {
    @EJB
    Token_EJB token_ejb;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String jws;
        String json = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        JSONObject jsonObject = new JSONObject(json);
        try {
            jws = jsonObject.getString("jws");
        } catch (NullPointerException e) {
            jws = "";
        }


        ValidateToken.validate(resp, jws, token_ejb);
        System.out.println("Зашли");
    }
}
