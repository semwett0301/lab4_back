package utilities;

import lombok.Data;
import utilities.enum_utilities.LoginProblemEnum;

@Data
public class LoginResponse {
    private boolean login = false;
    private boolean passwordProblem = false;
    private boolean loginProblem = false;
    private boolean jwsProblem = false;
    private String jws = "";

    public LoginResponse(String jws) {
        this.jws = jws;
        this.login = true;
        this.passwordProblem = false;
        this.loginProblem = false;
    }

    public LoginResponse(LoginProblemEnum loginProblemEnum) {
        switch (loginProblemEnum) {
            case LOGIN:
                this.loginProblem = true;
                break;
            case PASSWORD:
                this.passwordProblem = true;
                break;
            case JWS:
                this.jwsProblem = true;
                break;
        }
    }
}
