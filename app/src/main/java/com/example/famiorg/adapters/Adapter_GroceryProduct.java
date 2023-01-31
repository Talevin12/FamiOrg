package com.example.famiorg.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.famiorg.DataManager;
import com.example.famiorg.R;
import com.example.famiorg.callbacks.RecyclerRowMoveCallback;
import com.example.famiorg.logic.GroceryProduct;
import com.example.famiorg.logic.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

public class Adapter_GroceryProduct extends RecyclerView.Adapter<Adapter_GroceryProduct.GroceryProductViewHolder>
        implements RecyclerRowMoveCallback.RecyclerViewRowTouchHelperContract {

    private Context context;
    private ArrayList<GroceryProduct> groceryProducts = new ArrayList<>();
    private User user;
    private DataManager dataManager;

    private boolean isEditing = false;

    public Adapter_GroceryProduct(Context context, User user, DataManager dataManager) {
        this.context = context;
        this.user = user;
        this.dataManager = dataManager;
    }

    @NonNull
    @Override
    public GroceryProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_grocery, parent, false);
        GroceryProductViewHolder myGroceryProductViewHolder = new GroceryProductViewHolder(view);

        return myGroceryProductViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull GroceryProductViewHolder holder, int position) {
        GroceryProduct groceryProduct = groceryProducts.get(position);

        holder.grocery_CHECKBOX_done.setChecked(groceryProduct.getIsDone());
        if (groceryProduct.getIsDone()) {
            holder.grocery_LBL_groceryProduct.setPaintFlags(holder.grocery_LBL_groceryProduct.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.grocery_LBL_groceryQuantity.setPaintFlags(holder.grocery_LBL_groceryQuantity.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        } else {
            holder.grocery_LBL_groceryProduct.setPaintFlags(0);
            holder.grocery_LBL_groceryQuantity.setPaintFlags(0);
        }

        holder.grocery_LBL_groceryProduct.setText(groceryProduct.getName());
        holder.grocery_LBL_groceryQuantity.setText(String.valueOf(groceryProduct.getQuantity()));

        if (groceryProduct.getName().isBlank()) {
            holder.grocery_CHECKBOX_done.setEnabled(false);

            holder.grocery_EDITTEXT_editName.setText("");
            holder.grocery_EDITTEXT_editQuantity.setText("");

            holder.grocery_LBL_groceryProduct.setVisibility(View.INVISIBLE);
            holder.grocery_LBL_groceryQuantity.setVisibility(View.INVISIBLE);

            holder.grocery_IMG_BTN_edit.setVisibility(View.INVISIBLE);

            holder.grocery_EDITTEXT_editName.setVisibility(View.VISIBLE);
            holder.grocery_EDITTEXT_editQuantity.setVisibility(View.VISIBLE);

            holder.grocery_IMG_BTN_checkEditName.setVisibility(View.VISIBLE);

            holder.grocery_IMG_BTN_delete.setVisibility(View.INVISIBLE);

            holder.grocery_EDITTEXT_editQuantity.performClick();
        } else {
            holder.grocery_CHECKBOX_done.setEnabled(true);

            holder.grocery_LBL_groceryProduct.setVisibility(View.VISIBLE);
            holder.grocery_LBL_groceryQuantity.setVisibility(View.VISIBLE);

            holder.grocery_IMG_BTN_edit.setVisibility(View.VISIBLE);

            holder.grocery_EDITTEXT_editName.setVisibility(View.INVISIBLE);
            holder.grocery_EDITTEXT_editQuantity.setVisibility(View.INVISIBLE);

            holder.grocery_IMG_BTN_checkEditName.setVisibility(View.INVISIBLE);

            holder.grocery_IMG_BTN_delete.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return groceryProducts == null ? 0 : groceryProducts.size();
    }

    @Override
    public void onRowMoved(int from, int to) {
        if (from < to) {
            for (int i = from; i < to; i++) {
                Collections.swap(groceryProducts, i, i + 1);

                groceryProducts.get(i).setPosition(i);
                groceryProducts.get(i + 1).setPosition(i + 1);

                dataManager.moveGrocery(user.getFamilyId(), groceryProducts.get(i).getId(), i);
            }
        } else {
            for (int i = from; i > to; i--) {
                Collections.swap(groceryProducts, i, i - 1);

                groceryProducts.get(i).setPosition(i);
                groceryProducts.get(i - 1).setPosition(i - 1);

                dataManager.moveGrocery(user.getFamilyId(), groceryProducts.get(i).getId(), i);
            }
        }
        notifyItemMoved(from, to);

        dataManager.moveGrocery(user.getFamilyId(), groceryProducts.get(to).getId(), to);
    }

    @Override
    public void onRowSelected(GroceryProductViewHolder myViewHolder) {}

    @Override
    public void onRowClear(GroceryProductViewHolder myViewHolder) {}

    public void add() {
        if (!isEditing) {
            GroceryProduct groceryProduct = new GroceryProduct()
                    .setPosition(getItemCount());
            groceryProducts.add(groceryProduct);
            notifyDataSetChanged();
            dataManager.addGrocery(user.getFamilyId(), groceryProduct);

            isEditing = true;
        } else {
            Toast.makeText(context, "Finish editing product", Toast.LENGTH_SHORT).show();
        }
    }

    public void add(GroceryProduct groceryProduct) {
        for (GroceryProduct prod : groceryProducts) {
            if (groceryProduct.getId().equals(prod.getId())) {
                return;
            }
        }

        groceryProducts.add(groceryProduct);

        if (groceryProduct.getName().isBlank()) {
            isEditing = true;
        }

        notifyDataSetChanged();

        groceryProducts.sort(Comparator.comparingInt(GroceryProduct::getPosition));
    }

    public void moved(GroceryProduct groceryProduct) {
        onRowMoved(groceryProducts.indexOf(groceryProduct), groceryProduct.getPosition());
    }

    public void remove(String id, boolean saveToDB) {
        int i = 0;
        boolean flag = false;
        Iterator<GroceryProduct> prods = groceryProducts.iterator();
        while (prods.hasNext()) {
            GroceryProduct prod = prods.next();

            if (flag) {
                prod.setPosition(i - 1);
                dataManager.moveGrocery(user.getFamilyId(), prod.getId(), i - 1);
            } else if (prod.getId().equals(id)) {
                prods.remove();
                notifyDataSetChanged();

                if (saveToDB) {
                    dataManager.removeGrocery(user.getFamilyId(), prod);
                }

                flag = true;
            }

            i++;
        }
    }

    public void editNameAndQuantity(String id, String newName, float newQuantity, boolean saveToDB) {
        int i = 0;
        for (GroceryProduct groceryProduct : groceryProducts) {
            if (groceryProduct.getId().equals(id)) {
                if (saveToDB) {
                    dataManager.updateGroceryNameAndQuantity(user.getFamilyId(), id, newName, newQuantity);
                }

                if (!groceryProduct.getName().equals(newName)) {
                    groceryProduct.setName(newName);
                    notifyDataSetChanged();
                }

                if (groceryProduct.getQuantity() != newQuantity) {
                    groceryProduct.setQuantity(newQuantity);
                    notifyDataSetChanged();
                }

                break;
            }

            i++;
        }
    }

    public void editIsDone(String id, boolean newIsDone, boolean saveToDB) {
        int i = 0;
        for (GroceryProduct groceryProduct : groceryProducts) {
            if (saveToDB) {
                dataManager.updateGroceryIsDone(user.getFamilyId(), id, newIsDone);
            }

            if (groceryProduct.getId().equals(id)) {
                if (groceryProduct.getIsDone() != newIsDone) {
                    groceryProduct.setIsDone(newIsDone);
                    notifyDataSetChanged();
                }

                break;
            }

            i++;
        }
    }

    public class GroceryProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {//, CompoundButton.OnCheckedChangeListener{

        private CheckBox grocery_CHECKBOX_done;
        private AppCompatTextView grocery_LBL_groceryProduct;
        private AppCompatTextView grocery_LBL_groceryQuantity;
        private AppCompatEditText grocery_EDITTEXT_editName;
        private AppCompatEditText grocery_EDITTEXT_editQuantity;
        private AppCompatImageButton grocery_IMG_BTN_delete;
        private AppCompatImageButton grocery_IMG_BTN_edit;
        private AppCompatImageButton grocery_IMG_BTN_checkEditName;

        public GroceryProductViewHolder(View itemView) {
            super(itemView);
            grocery_CHECKBOX_done = itemView.findViewById(R.id.grocery_CHECKBOX_done);

            grocery_LBL_groceryProduct = itemView.findViewById(R.id.grocery_LBL_groceryProduct);
            grocery_LBL_groceryQuantity = itemView.findViewById(R.id.grocery_LBL_groceryQuantity);
            grocery_EDITTEXT_editName = itemView.findViewById(R.id.grocery_EDITTEXT_editName);
            grocery_EDITTEXT_editQuantity = itemView.findViewById(R.id.grocery_EDITTEXT_editQuantity);
            grocery_IMG_BTN_delete = itemView.findViewById(R.id.grocery_IMG_BTN_delete);
            grocery_IMG_BTN_edit = itemView.findViewById(R.id.grocery_IMG_BTN_edit);
            grocery_IMG_BTN_checkEditName = itemView.findViewById(R.id.grocery_IMG_BTN_checkEditName);

            grocery_CHECKBOX_done.setOnClickListener(this);
            grocery_IMG_BTN_delete.setOnClickListener(this);
            grocery_IMG_BTN_edit.setOnClickListener(this);
            grocery_IMG_BTN_checkEditName.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.equals(grocery_IMG_BTN_delete)) {
                remove(groceryProducts.get(getAdapterPosition()).getId(), true);
                if (grocery_IMG_BTN_checkEditName.getVisibility() == View.VISIBLE) {
                    isEditing = false;
                }
            } else if (v.equals(grocery_CHECKBOX_done)) {
                if (grocery_CHECKBOX_done.isChecked()) {
                    grocery_LBL_groceryProduct.setPaintFlags(grocery_LBL_groceryProduct.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                } else {
                    grocery_LBL_groceryProduct.setPaintFlags(0);
                }

                editIsDone(groceryProducts.get(getAdapterPosition()).getId(),
                        grocery_CHECKBOX_done.isChecked(),
                        true);
            } else if (v.equals(grocery_IMG_BTN_edit)) {
                if (!isEditing) {
                    isEditing = true;

                    grocery_CHECKBOX_done.setEnabled(false);

                    grocery_LBL_groceryProduct.setVisibility(View.INVISIBLE);
                    grocery_LBL_groceryQuantity.setVisibility(View.INVISIBLE);

                    grocery_IMG_BTN_edit.setVisibility(View.INVISIBLE);

                    grocery_EDITTEXT_editName.setText(grocery_LBL_groceryProduct.getText().toString());
                    grocery_EDITTEXT_editQuantity.setText(grocery_LBL_groceryQuantity.getText().toString());

                    grocery_EDITTEXT_editName.setVisibility(View.VISIBLE);
                    grocery_EDITTEXT_editQuantity.setVisibility(View.VISIBLE);

                    grocery_IMG_BTN_checkEditName.setVisibility(View.VISIBLE);

                    grocery_IMG_BTN_delete.setVisibility(View.INVISIBLE);

                    grocery_EDITTEXT_editQuantity.performClick();
                } else {
                    Toast.makeText(context, "Finish editing product", Toast.LENGTH_SHORT).show();
                }
            } else if (v.equals(grocery_IMG_BTN_checkEditName)) {
                if (!grocery_EDITTEXT_editName.getText().toString().isBlank() &&
                        !grocery_EDITTEXT_editQuantity.getText().toString().isBlank()) {

                    isEditing = false;

                    grocery_CHECKBOX_done.setEnabled(true);

                    grocery_EDITTEXT_editQuantity.setVisibility(View.INVISIBLE);
                    grocery_EDITTEXT_editName.setVisibility(View.INVISIBLE);
                    grocery_IMG_BTN_checkEditName.setVisibility(View.INVISIBLE);

                    grocery_LBL_groceryQuantity.setText(grocery_EDITTEXT_editQuantity.getText().toString());
                    grocery_LBL_groceryProduct.setText(grocery_EDITTEXT_editName.getText().toString());
                    editNameAndQuantity(groceryProducts.get(getAdapterPosition()).getId(),
                            grocery_EDITTEXT_editName.getText().toString(),
                            Float.parseFloat(grocery_EDITTEXT_editQuantity.getText().toString()),
                            true);

                    grocery_LBL_groceryQuantity.setVisibility(View.VISIBLE);
                    grocery_LBL_groceryProduct.setVisibility(View.VISIBLE);

                    grocery_IMG_BTN_edit.setVisibility(View.VISIBLE);

                    grocery_IMG_BTN_delete.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}
