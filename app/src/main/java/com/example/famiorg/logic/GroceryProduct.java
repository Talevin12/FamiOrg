package com.example.famiorg.logic;

import java.io.Serializable;
import java.util.UUID;

public class GroceryProduct implements Serializable {
    private String id = UUID.randomUUID().toString();
    private String name = "";
    private float quantity;
    private boolean isDone;
//    private int position;

    public String getId() {
        return id.toString();
    }

    public GroceryProduct setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public GroceryProduct setName(String name) {
        this.name = name;
        return this;
    }

    public float getQuantity() {
        return quantity;
    }

    public GroceryProduct setQuantity(float quantity) {
        this.quantity = quantity;
        return this;
    }

    public boolean getIsDone() {
        return isDone;
    }

    public GroceryProduct setIsDone(boolean done) {
        isDone = done;
        return this;
    }

//    public int getPosition() {
//        return position;
//    }
//
//    public GroceryProduct setPosition(int position) {
//        this.position = position;
//        return this;
//    }
}
