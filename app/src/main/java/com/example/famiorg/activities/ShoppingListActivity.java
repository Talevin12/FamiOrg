package com.example.famiorg.activities;

import android.os.Bundle;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.famiorg.DataManager;
import com.example.famiorg.GoogleLoginAssets;
import com.example.famiorg.R;
import com.example.famiorg.adapters.Adapter_GroceryProduct;
import com.example.famiorg.callbacks.Callback_DataManager;
import com.example.famiorg.callbacks.RecyclerRowMoveCallback;
import com.example.famiorg.logic.GroceryProduct;
import com.example.famiorg.logic.User;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

public class ShoppingListActivity extends AppCompatActivity {

    private RecyclerView shoppingList_LST_groceries;
    private MaterialButton shoppingList_BTN_add;
    private Adapter_GroceryProduct adapter_groceries;

    DataManager dataManager = new DataManager();

    GoogleLoginAssets googleLoginAssets = new GoogleLoginAssets(dataManager, this);

    private User user;

    Callback_DataManager<User> callback_setUser;
    Callback_DataManager<GroceryProduct> callback_addGroceryProduct;
    Callback_DataManager<GroceryProduct> callback_updateGroceryProduct;
    Callback_DataManager<GroceryProduct> callback_moveGroceryProduct;
    Callback_DataManager<int[]> callback_removeGroceryProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_shopping_list);


//        String background ="https://cdn.apartmenttherapy.info/image/upload/f_auto,q_auto:eco,c_fill,g_center,w_730,h_913/k%2Farchive%2Fe71b581632b91695a3fe9f9b1a324675190cb921";
////        String background ="https://thumbs.dreamstime.com/b/seamless-pattern-fast-food-items-black-background-hand-drawn-doodle-set-vector-illustration-junk-collection-cartoon-snack-200598354.jpg";
////        String background ="https://t3.ftcdn.net/jpg/03/11/27/96/360_F_311279691_wfNvKEXKX3cyYFsZ7OttC2C5xkf8Y2BL.jpg";
////        String background ="https://as1.ftcdn.net/v2/jpg/01/17/32/94/1000_F_117329446_L644DgH2EPifhMF6rfZIvjJ2cXR7vu46.jpg";
////        String background ="https://img.freepik.com/premium-vector/hand-painted-background-violet-orange-colours_23-2148427578.jpg?w=2000";
//        Glide.with(this)
//                .load(background)
//                .centerCrop()
//                .into((AppCompatImageView) findViewById(R.id.shoppingList_IMG_background));

//        Blurry.with(this)
//                .sampling(3)
//                .async()
//                .from(BitmapFactory.decodeResource(getResources(), R.drawable.shopping_list_background))
//                .into(findViewById(R.id.shoppingList_IMG_background));

        findViews();

        initCallbacks();
        setCallbacks();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!googleLoginAssets.checkSignIn()) {
            getParent().finish();
            finish();
        }
        else {
            dataManager.getGroceries(FirebaseAuth.getInstance().getCurrentUser().getUid());
        }
    }

    private void findViews() {
        shoppingList_LST_groceries = findViewById(R.id.shoppingList_LST_groceries);
        shoppingList_BTN_add = findViewById(R.id.shoppingList_BTN_add);
    }

    private void initRecyclerView() {
        adapter_groceries = new Adapter_GroceryProduct(this, user, dataManager);
        shoppingList_LST_groceries.setLayoutManager(new LinearLayoutManager(this));

        ItemTouchHelper.Callback callback = new RecyclerRowMoveCallback(adapter_groceries);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(shoppingList_LST_groceries);

        shoppingList_LST_groceries.setAdapter(adapter_groceries);
    }

    private void initViews() {
        shoppingList_BTN_add.setOnClickListener(v -> {
            adapter_groceries.add();
            shoppingList_LST_groceries.smoothScrollToPosition(adapter_groceries.getItemCount());
        });
    }

    private void initCallbacks() {
        callback_setUser = (Callback_DataManager) object -> {
            user = (User) object;

            dataManager.getGroceries(((User) object).getFamilyId());
            initRecyclerView();
            initViews();
        };

        callback_addGroceryProduct = (Callback_DataManager) object -> adapter_groceries.add((GroceryProduct) object);

        callback_updateGroceryProduct = (Callback_DataManager) object -> {
            adapter_groceries.editNameAndQuantity(((GroceryProduct) object).getId(), ((GroceryProduct) object).getName(), ((GroceryProduct) object).getQuantity(), false);
            adapter_groceries.editIsDone(((GroceryProduct) object).getId(), ((GroceryProduct) object).getIsDone(), true);
        };

        callback_moveGroceryProduct = (Callback_DataManager) object -> adapter_groceries.moved(((int[]) object)[0], ((int[]) object)[1]);

        callback_removeGroceryProduct = (Callback_DataManager) object -> adapter_groceries.remove(((GroceryProduct) object).getId(), false);
    }

    private void setCallbacks() {
        dataManager.setCallBack_setUserProtocol(callback_setUser);
        dataManager.setCallback_addGroceryProduct(callback_addGroceryProduct);
        dataManager.setCallback_updateGroceryProduct(callback_updateGroceryProduct);
        dataManager.setCallback_moveGroceryProduct(callback_moveGroceryProduct);
        dataManager.setCallback_removeGroceryProduct(callback_removeGroceryProduct);
    }
}