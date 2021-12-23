package ejb_beans;

import database.pointdao.PointDAO;
import database.pointdao.PointDataBaseManager;
import entities.Point;
import exceptions.DataNotUpdateException;
import exceptions.NoDataWasReceivedException;
import org.json.JSONObject;
import utilities.AreaChecker;

import javax.ejb.Stateless;
import javax.validation.ValidationException;
import java.io.Serializable;
import java.util.List;

@Stateless
public class Points_EJB implements Serializable {
    PointDAO pointDAO = new PointDataBaseManager();

    public void addPoint(double x, double y, double r, String username) throws DataNotUpdateException {
        Point point = new Point(x, y, r,false, username);

        boolean hit = AreaChecker.areaCheck(x, y, r);
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
