package ejb_beans;

import database.userdao.UserDAO;
import database.userdao.UserDataBaseManager;
import exceptions.NoDataWasReceivedException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;

import javax.ejb.Stateless;
import java.io.*;
import java.security.*;
import java.util.Date;

@Data
@Stateless
@NoArgsConstructor
public class Token_EJB implements Serializable{
    private UserDAO userDAO = new UserDataBaseManager();
    private long limit = 1800000;
    private PublicKey publicKey = readPublic();
    private PrivateKey privateKey = readPrivate();


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
        String username;
        try {
            username = getUsernameFromJws(jws);
        } catch (Exception e) {
            return false;
        }
        System.out.println(username);
        return userDAO.checkUserExist(username);
    }

    public String getUsernameFromJws(String jws) {
        return (String) Jwts.parserBuilder().setSigningKey(publicKey)
                .build()
                .parseClaimsJws(jws)
                .getBody()
                .get("username");
    }

    private PublicKey readPublic() {
        try {
            File file = new File("public.pem");

            try (FileReader keyReader = new FileReader(file)) {
                PEMParser pemParser = new PEMParser(keyReader);
                JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
                SubjectPublicKeyInfo publicKeyInfo = SubjectPublicKeyInfo.getInstance(pemParser.readObject());
                return converter.getPublicKey(publicKeyInfo);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

        private PrivateKey readPrivate() {
            try {
                Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());


                File file = new File("key.pem");
                PEMParser pemParser = new PEMParser(new FileReader(file));

                JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");
                Object object = pemParser.readObject();
                KeyPair kp = converter.getKeyPair((PEMKeyPair) object);
                return kp.getPrivate();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }


    }
