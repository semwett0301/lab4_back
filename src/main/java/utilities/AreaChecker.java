package utilities;

public class AreaChecker {
    public static boolean areaCheck(double x, double y, double r) {
        if (x <= r && y >= -r / 2 && x >= 0 && y <= 0) return true;
        if (x >= -r && x <= 0 && y <= 0 && y >= -r / 2) {
            if (y >= -x /
                    2 - r / 2) return true;
        }
        if (y >= 0 && y <= r && x <= 0 && x >= -r) {
            if (Math.pow(x,2) + Math.pow(y, 2) <= Math.pow(r, 2)) return true;
        }
        return false;
    }
}
