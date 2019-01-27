package app.demo.user.service;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;


public class PasswordHasher {
    private final String salt;
    private final String password;

    public PasswordHasher(String salt, String password) {
        this.salt = salt;
        this.password = password;
    }

    public String hash(int iteration) {
        String hashedPassword = password;
        for (int i = 0; i < iteration; i++) {
            hashedPassword = Hashing.sha256().hashString(salt + ":" + hashedPassword, Charsets.UTF_8).toString();
        }
        return hashedPassword;
    }
}

