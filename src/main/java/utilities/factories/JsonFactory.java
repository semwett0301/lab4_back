package utilities.factories;

import oracle.security.crypto.cert.ValidationException;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

public class JsonFactory {
    public static JSONObject getJsonFromRequest(HttpServletRequest req) throws IOException {
        String json = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        JSONObject jsonObject = new JSONObject(json);

        return jsonObject;
    }
}
