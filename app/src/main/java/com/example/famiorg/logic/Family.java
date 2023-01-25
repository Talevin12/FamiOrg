package com.example.famiorg.logic;

import java.util.ArrayList;
import java.util.UUID;

public class Family {
    private String familyId = String.valueOf(UUID.randomUUID());
    //    private String familyId = "123";
    private String familyName;
    private ArrayList<String> familyMembersIDs = new ArrayList<>();
    private ArrayList<GroceryProduct> shoppingList;

    public String getFamilyId() {
        return familyId;
    }

    public Family setFamilyId(String familyId) {
        this.familyId = familyId;
        return this;
    }

    public String getFamilyName() {
        return familyName;
    }

    public Family setFamilyName(String familyName) {
        this.familyName = familyName;
        return this;
    }

    public ArrayList<String> getFamilyMembersIDs() {
        return familyMembersIDs;
    }

    public Family setFamilyMembersIDs(ArrayList<String> familyMembersIDs) {
        this.familyMembersIDs = familyMembersIDs;
        return this;
    }

    public void addMember(String userId) {
        familyMembersIDs.add(userId);
    }

    public ArrayList<GroceryProduct> getShoppingList() {
        return shoppingList;
    }

    public Family setShoppingList(ArrayList<GroceryProduct> shoppingList) {
        this.shoppingList = shoppingList;
        return this;
    }
}
