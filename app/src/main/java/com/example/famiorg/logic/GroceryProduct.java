package com.example.famiorg.logic;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class GroceryProduct implements Serializable {
    private String id = UUID.randomUUID().toString();
    private String name = "";
    private float quantity;
    private boolean isDone;
    private int position;

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

    public int getPosition() {
        return position;
    }

    public GroceryProduct setPosition(int position) {
        this.position = position;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GroceryProduct)) return false;
        GroceryProduct that = (GroceryProduct) o;
        return Float.compare(that.getQuantity(), getQuantity()) == 0 && isDone == that.isDone && getPosition() == that.getPosition() && Objects.equals(getId(), that.getId()) && Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getQuantity(), isDone, getPosition());
    }
}
