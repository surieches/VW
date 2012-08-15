/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.androidhive.androidsqlite;

/**
 *
 * @author CPP-lap
 */
public class Usuario {
    private String username;
    private String password;
    private String idusu;

    public String getIdusu() {
        return idusu;
    }

    public void setIdusu(String idusu) {
        this.idusu = idusu;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
