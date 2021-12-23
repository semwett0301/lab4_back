package servlets;

import ejb_beans.Points_EJB;
import ejb_beans.Token_EJB;
import exceptions.DataNotUpdateException;
import exceptions.NoDataWasReceivedException;
import org.json.JSONObject;
import utilities.LoginResponse;
import utilities.enum_utilities.LoginProblemEnum;
import utilities.validators.ValidateToken;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

@WebServlet("/points/adding")
public class AddPointServlet extends HttpServlet {
    @EJB
    private Token_EJB token_ejb;

    @EJB
    private Points_EJB points_ejb;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Началось");
        String json = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        JSONObject jsonObject = new JSONObject(json);

        String jws = "";
        double x;
        double y;
        double r;

        try {
            try {
                jws = jsonObject.getString("jws");
                if (!ValidateToken.validate(resp, jws, token_ejb)) return;

                x = Double.parseDouble(jsonObject.getString("x"));
                y = Double.parseDouble(jsonObject.getString("y"));
                r = Double.parseDouble(jsonObject.getString("r"));
            } catch ( Exception e) {
                resp.sendError(400, "Полученные данные некорректны");
                e.printStackTrace();
                return;
            }
            points_ejb.addPoint(x,y,r,token_ejb.getUsernameFromJws(jws));
        } catch (DataNotUpdateException e) {
            resp.sendError(500, "При работе с базой данных возникла ошибка, ваши действия не могут быть сохранены");
            e.printStackTrace();
        }
    }
}
