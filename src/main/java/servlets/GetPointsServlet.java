package servlets;

import ejb_beans.Points_EJB;
import ejb_beans.Token_EJB;
import entities.Point;
import exceptions.NoDataWasReceivedException;
import org.json.JSONArray;
import org.json.JSONObject;
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
import java.util.List;

@WebServlet("/points")
public class GetPointsServlet extends HttpServlet {
    @EJB
    private Token_EJB token_ejb;

    @EJB
    private Points_EJB points_ejb;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String jws = "";
        Cookie[] cookies = req.getCookies();
        try {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("jws")) jws = cookie.getValue();
            }
        } catch (NullPointerException e) {
            resp.sendError(400, "Полученные данные некорректны");
            return;
        }

        if (!ValidateToken.validate(resp, jws, token_ejb)) return;

        try {
            List<Point> points = points_ejb.getPoints(token_ejb.getUsernameFromJws(jws));
            resp.getWriter().write(new JSONArray(points).toString());
        } catch (NoDataWasReceivedException e) {
            resp.sendError(500, "При работе с базой данных возникла ошибка, данные не были получены");
        }
    }
}
