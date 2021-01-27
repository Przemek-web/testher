package pl.lodz.p.it.gornik.pomocnikseniora.dtos;

import java.util.List;

public class JWTLoginSuccessPojo {
    boolean success;
    String token;
    List<String> roles;

    public JWTLoginSuccessPojo(boolean success, String token, List<String> roles) {
        this.success = success;
        this.token = token;
        this.roles = roles;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
