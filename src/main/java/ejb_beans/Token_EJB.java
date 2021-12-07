package ejb_beans;

import database.UserDAO;
import database.UserDataBaseManager;
import exceptions.NoDataWasReceivedException;
import io.jsonwebtoken.Jwts;
import lombok.Data;
import org.json.JSONObject;
import utilities.LoginResponse;
import utilities.enum_utilities.CheckUserExistEnum;
import utilities.enum_utilities.LoginProblemEnum;

import javax.ejb.Stateless;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;

@Data
@Stateless
public class Token_EJB {
    private UserDAO userDAO = new UserDataBaseManager();
    private long limit = 1800000;
    private PublicKey publicKey;

    {
        try {
            publicKey = readPublic("./resources/public.pem");
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private PrivateKey privateKey;

    {
        try {
            privateKey = readPrivate(readKey("./resources/key.pem"));
        } catch (InvalidKeySpecException | NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        }
    }

    public String generateToken(String username) {
        Date date = new Date();
        return Jwts.builder()
                .setIssuedAt(date)
                .setExpiration(new Date(date.getTime() + limit))
                .setSubject(username)
                .claim("username", username)
                .signWith(privateKey)
                .compact();
    }

    public String refreshToken(String jws) {
        return generateToken(getUsernameFromJws(jws));
    }

    public boolean checkToken(String jws) throws NoDataWasReceivedException {
        String username = getUsernameFromJws(jws);

        CheckUserExistEnum success = userDAO.checkUserExist(username);
        if (success == CheckUserExistEnum.CANNOT_CHECK) throw new NoDataWasReceivedException();
        return success == CheckUserExistEnum.USER_EXIST;
    }

    public String getUsernameFromJws(String jws) {
        return (String) Jwts.parserBuilder().setSigningKey(publicKey)
                .build()
                .parseClaimsJws(jws)
                .getBody()
                .get("username");
    }

    private PublicKey readPublic(String key) throws InvalidKeySpecException, NoSuchAlgorithmException {
        byte[] encoded = Base64.getDecoder().decode(key);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PublicKey pubKey =  kf.generatePublic(new X509EncodedKeySpec(encoded));
        return pubKey;
    };
    private PrivateKey readPrivate(String key) throws InvalidKeySpecException, NoSuchAlgorithmException {
        byte[] encoded = Base64.getDecoder().decode(key);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
        return kf.generatePrivate(keySpec);
    };

    private static String readKey(String filename) throws IOException {
        // Read key from file
        String strKeyPEM = "";
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = br.readLine()) != null) {
            strKeyPEM += line + "\n";
        }
        br.close();
        strKeyPEM = strKeyPEM.replace("-----BEGIN PRIVATE KEY-----\n", "");
        strKeyPEM = strKeyPEM.replace("-----END PRIVATE KEY-----", "");
        return strKeyPEM;
    }
}
