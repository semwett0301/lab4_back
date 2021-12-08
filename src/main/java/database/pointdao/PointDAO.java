package database.pointdao;

import entities.Point;
import exceptions.DataNotUpdateException;
import exceptions.NoDataWasReceivedException;

import java.util.List;
import java.util.Optional;

public interface PointDAO {
    void add(Point point) throws DataNotUpdateException;
    void clear(String username) throws DataNotUpdateException;
    List<Point> findAllPoints(String username) throws NoDataWasReceivedException;
}
