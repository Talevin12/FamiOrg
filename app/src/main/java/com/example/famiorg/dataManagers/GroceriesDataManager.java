package com.example.famiorg.dataManagers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.famiorg.callbacks.Callback_DataManager;
import com.example.famiorg.logic.GroceryProduct;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GroceriesDataManager {
    private Callback_DataManager callback_addGroceryProduct;
    private Callback_DataManager callback_updateGroceryProduct;
    private Callback_DataManager callback_moveGroceryProduct;
    private Callback_DataManager callback_removeGroceryProduct;

    public void setCallback_addGroceryProduct(Callback_DataManager callback_addGroceryProduct) {
        this.callback_addGroceryProduct = callback_addGroceryProduct;
    }

    public void setCallback_updateGroceryProduct(Callback_DataManager callback_updateGroceryProduct) {
        this.callback_updateGroceryProduct = callback_updateGroceryProduct;
    }

    public void setCallback_moveGroceryProduct(Callback_DataManager callback_moveGroceryProduct) {
        this.callback_moveGroceryProduct = callback_moveGroceryProduct;
    }

    public void setCallback_removeGroceryProduct(Callback_DataManager callback_removeGroceryProduct) {
        this.callback_removeGroceryProduct = callback_removeGroceryProduct;
    }

    public void getGroceries(String famId) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference refShoppingList = db.getReference("Families").child(famId).child("shoppingList");

        refShoppingList.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                callback_addGroceryProduct.getObject(snapshot.getValue(GroceryProduct.class));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                callback_updateGroceryProduct.getObject(snapshot.getValue(GroceryProduct.class));
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                callback_removeGroceryProduct.getObject(snapshot.getValue(GroceryProduct.class));
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                callback_moveGroceryProduct.getObject(snapshot.getValue(GroceryProduct.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void addGrocery(String famId, GroceryProduct groceryProduct) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference refShoppingList = db.getReference("Families").child(famId).child("shoppingList");

        refShoppingList.child(String.valueOf(groceryProduct.getId())).setValue(groceryProduct);
    }

    public void removeGrocery(String famId, GroceryProduct groceryProduct) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference refShoppingList = db.getReference("Families").child(famId).child("shoppingList");

        refShoppingList.child(groceryProduct.getId()).removeValue();
    }

    public void updateGroceryNameAndQuantity(String famId, String groceryId, String name, float quantity) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference refProduct = db.getReference("Families").child(famId).child("shoppingList").child(groceryId);

        refProduct.child("name").setValue(name);
        refProduct.child("quantity").setValue(quantity);
    }

    public void updateGroceryIsDone(String famId, String groceryId, boolean isDone) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference refProduct = db.getReference("Families").child(famId).child("shoppingList").child(groceryId);

        refProduct.child("isDone").setValue(isDone);
    }

    public void moveGrocery(String famId, String groceryId, int to) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference refProduct = db.getReference("Families").child(famId).child("shoppingList").child(groceryId);

        refProduct.child("position").setValue(to);
    }
}
