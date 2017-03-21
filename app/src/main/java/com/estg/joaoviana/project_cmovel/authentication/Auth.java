package com.estg.joaoviana.project_cmovel.authentication;

/**
 * Created by PJ on 18/03/2017.
 */

public class Auth {
    private static String id = "";
    private static String username = "";
    private static String email = "";

    public static String getId() {
        return id;
    }

    public static void setId(String id) {
        Auth.id = id;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        Auth.username = username;
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        Auth.email = email;
    }

    public static Boolean isAuthenticated(){
        if(getId() != "" || getUsername() != "" || getEmail() != ""){
            return true;
        }else{
            return false;
        }
    }
}
