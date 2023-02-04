package com.example.famiorg.logic;

import com.example.famiorg.assets.iconsList;

import java.io.Serializable;
import java.util.Random;

public class User implements Serializable {
    private String email;
    private String familyId;// = "123";
    private String name = "";
    private int icon;

    public User(){
        int rndIcon = new Random().nextInt(iconsList.icons.length);
        icon = iconsList.icons[rndIcon];
    }

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getFamilyId() {
        return familyId;
    }

    public User setFamilyId(String familyId) {
        this.familyId = familyId;
        return this;
    }

    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public int getIcon() {
        return icon;
    }

    public User setIcon(int icon) {
        this.icon = icon;
        return this;
    }
}
