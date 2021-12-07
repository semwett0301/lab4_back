package ejb_beans;

import database.PointDAO;
import database.PointDataBaseManager;
import entities.Point;
import exceptions.DataNotUpdateException;
import exceptions.NoDataWasReceivedException;
import org.json.JSONObject;
import utilities.AreaChecker;

import javax.ejb.Stateless;
import javax.validation.ValidationException;
import java.util.List;

@Stateless
public class Points_EJB {
    PointDAO pointDAO = new PointDataBaseManager();

    public void addPoint(double x, double y, double r, String username) throws DataNotUpdateException {
        Point point = new Point();
        point.setX(x);
        point.setY(y);
        point.setR(r);
        point.setUsername(username);

        boolean hit = AreaChecker.areaCheck(point.getX(),point.getY(),point.getR());
        point.setHit(hit);
        pointDAO.add(point);
    }

    public List<Point> getPoints(String username) throws NoDataWasReceivedException {
        return pointDAO.findAllPoints(username);
    }

    public void clear(String username) throws DataNotUpdateException {
        pointDAO.clear(username);
    }
}
