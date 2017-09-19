package tech.mihmandar.ui.presentation.common;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Murat on 9/14/2017.
 */
public class CookieDto implements Serializable {
    private String username;
    private String password;
    private String token;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}