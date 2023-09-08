package com.java.project.tournamenttracker.entities;

import com.java.project.tournamenttracker.main.MainController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class User implements Hash, Serializable {
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);
    private String username;
    private String password;
    private Role role;

    public User(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }
    public static class UserBuilder{
        private String username;
        private String password;
        private Role role;

        public UserBuilder addUsername(String username) {
            this.username = username;
            return this;
        }

        public UserBuilder addPassword(String password) {
            this.password = password;
            return this;
        }

        public UserBuilder addRole(Role role) {
            this.role = role;
            return this;
        }

        public User build(){
            return new User(username, password, role);
        }
    }

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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String hashPassword(String password){
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] array = md.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            String msg = "Error hashing password!";
            logger.error(msg, e);
        }
        return null;
    }
}
