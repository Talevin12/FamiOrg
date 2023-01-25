package com.example.famiorg.logic;

import com.example.famiorg.iconsList;

import java.io.Serializable;
import java.util.Random;

public class User implements Serializable {
    private String email;
    private String familyId;// = "123";
    private String name = "";
    private int icon;

//    private final int[] icons = {R.drawable.ic_bunny, R.drawable.ic_basketballl, R.drawable.ic_dino,
//            R.drawable.ic_cat1, R.drawable.ic_cat2, R.drawable.ic_dog,
//            R.drawable.ic_fairy, R.drawable.ic_glasses, R.drawable.ic_kid,
//            R.drawable.ic_man, R.drawable.ic_monster1, R.drawable.ic_monster2,
//            R.drawable.ic_monster3, R.drawable.ic_woman, R.drawable.ic_batman,
//            R.drawable.ic_deadpool, R.drawable.ic_captain_america, R.drawable.ic_spiderman,
//            R.drawable.ic_superman, R.drawable.ic_wonder_woman};

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
