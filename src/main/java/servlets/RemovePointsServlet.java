package servlets;


import ejb_beans.Points_EJB;
import ejb_beans.Token_EJB;
import exceptions.DataNotUpdateException;
import org.json.JSONObject;
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

@WebServlet("/points/removing")
public class RemovePointsServlet extends HttpServlet {
    @EJB
    Token_EJB token_ejb;

    @EJB
    private Points_EJB points_ejb;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String json = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        JSONObject jsonObject = new JSONObject(json);

        String jws = "";
        try {
            jws = jsonObject.getString("jws");
        } catch (NullPointerException e) {
            resp.sendError(400, "Полученные данные некорректны");
            return;
        }

        if (!ValidateToken.validate(resp, jws, token_ejb)) return;

        try {
            points_ejb.clear(token_ejb.getUsernameFromJws(jws));
        } catch (DataNotUpdateException e) {
            resp.sendError(500, "При работе с базой данных возникла ошибка, ваши действия не могут быть сохранены");
        }
    }
}
