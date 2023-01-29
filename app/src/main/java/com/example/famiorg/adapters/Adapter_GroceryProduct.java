package com.example.famiorg.adapters;

import android.content.Context;
import android.graphics.Color;
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
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.Collections;

public class Adapter_GroceryProduct extends RecyclerView.Adapter<Adapter_GroceryProduct.GroceryProductViewHolder>
        implements RecyclerRowMoveCallback.RecyclerViewRowTouchHelperContract {

    private Context context;
    private ArrayList<GroceryProduct> groceryProducts = new ArrayList<>();
    private User user;
    private DataManager dataManager;

    private boolean isEditing = false;

    public Adapter_GroceryProduct(Context context, User user, DataManager dataManager) {
        this.context = context;
//        this.groceryProducts = groceryProducts;
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
        if(groceryProduct.getIsDone()) {
            holder.grocery_LBL_groceryProduct.setPaintFlags(holder.grocery_LBL_groceryProduct.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.grocery_LBL_groceryQuantity.setPaintFlags(holder.grocery_LBL_groceryQuantity.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        else {
            holder.grocery_LBL_groceryProduct.setPaintFlags(0);
            holder.grocery_LBL_groceryQuantity.setPaintFlags(0);
        }

        holder.grocery_LBL_groceryProduct.setText(groceryProducts.get(position).getName());
        holder.grocery_LBL_groceryQuantity.setText(String.valueOf(groceryProducts.get(position).getQuantity()));

        if(groceryProducts.get(position).getName().equals("")) {
            holder.grocery_LBL_groceryProduct.setVisibility(View.INVISIBLE);
            holder.grocery_LBL_groceryQuantity.setVisibility(View.INVISIBLE);

            holder.grocery_IMG_BTN_edit.setVisibility(View.INVISIBLE);

            holder.grocery_EDITTEXT_editName.setVisibility(View.VISIBLE);
            holder.grocery_EDITTEXT_editQuantity.setVisibility(View.VISIBLE);

            holder.grocery_IMG_BTN_checkEditName.setVisibility(View.VISIBLE);

            holder.grocery_IMG_BTN_delete.setVisibility(View.INVISIBLE);
        } else {
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
        if(from < to) {
            for(int i = from; i < to; i++) {
                Collections.swap(groceryProducts, i, i+1);
            }
        } else {
            for(int i = from; i > to; i--) {
                Collections.swap(groceryProducts, i, i-1);
            }
        }

        notifyItemMoved(from, to);
    }

    @Override
    public void onRowSelected(GroceryProductViewHolder myViewHolder) {
        ((MaterialCardView)myViewHolder.itemView).setCardBackgroundColor(Color.GRAY);
        myViewHolder.grocery_IMG_BTN_edit.setBackgroundColor(Color.GRAY);
        myViewHolder.grocery_IMG_BTN_checkEditName.setBackgroundColor(Color.GRAY);
    }

    @Override
    public void onRowClear(GroceryProductViewHolder myViewHolder) {
        ((MaterialCardView)myViewHolder.itemView).setCardBackgroundColor(Color.parseColor("#F3FEBF"));
        myViewHolder.grocery_IMG_BTN_edit.setBackgroundColor(Color.parseColor("#F3FEBF"));
        myViewHolder.grocery_IMG_BTN_checkEditName.setBackgroundColor(Color.parseColor("#F3FEBF"));
    }

    public void add() {
        if(!isEditing) {
            groceryProducts.add(new GroceryProduct());
            notifyDataSetChanged();
            dataManager.addGrocery(user.getFamilyId(), groceryProducts.get(getItemCount()-1));

            isEditing = true;
        } else {
            Toast.makeText(context, "Finish editing product", Toast.LENGTH_SHORT).show();
        }
    }

    public void add(GroceryProduct groceryProduct) {
        for(GroceryProduct prod : groceryProducts) {
            if (groceryProduct.getId().equals(prod.getId())) {
                return;
            }
        }

        groceryProducts.add(groceryProduct);
        notifyDataSetChanged();
    }

    public void moved(int toPosition, int fromPosition) {
        onRowMoved(fromPosition, toPosition);
    }

    public void remove(String id, boolean saveToDB) {
        int i = 0;
        for(GroceryProduct prod : groceryProducts) {
            if (prod.getId().equals(id)) {
                if(saveToDB) {
                    dataManager.removeGrocery(user.getFamilyId(), groceryProducts.get(i));
                }

                groceryProducts.remove(i);

                notifyDataSetChanged();

                break;
            }

            i++;
        }
    }

    public void editNameAndQuantity(String id, String newName, float newQuantity, boolean saveToDB) {
        int i = 0;
        for(GroceryProduct groceryProduct : groceryProducts) {
            if(groceryProduct.getId().equals(id)) {
                if(saveToDB){
                    dataManager.updateGroceryNameAndQuantity(user.getFamilyId(), id, newName, newQuantity);
                }

                if(!groceryProduct.getName().equals(newName)) {
                    groceryProduct.setName(newName);
//                    notifyItemChanged(i);
                    notifyDataSetChanged();
                }

                if(groceryProduct.getQuantity() != newQuantity) {
                    groceryProduct.setQuantity(newQuantity);
//                    notifyItemChanged(i);
                    notifyDataSetChanged();
                }

                break;
            }

            i++;
        }
    }

    public void editIsDone(String id, boolean newIsDone, boolean saveToDB) {
        int i = 0;
        for(GroceryProduct groceryProduct : groceryProducts) {
            if(saveToDB){
                dataManager.updateGroceryIsDone(user.getFamilyId(), id, newIsDone);
            }

            if (groceryProduct.getId().equals(id)) {
                if(groceryProduct.getIsDone() != newIsDone) {
                    groceryProduct.setIsDone(newIsDone);
//                    notifyItemChanged(i);
                    notifyDataSetChanged();
                }

                break;
            }

            i++;
        }
    }

    public class GroceryProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {//, CompoundButton.OnCheckedChangeListener{

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
            grocery_LBL_groceryProduct.setOnLongClickListener(this);
            grocery_IMG_BTN_delete.setOnClickListener(this);
            grocery_IMG_BTN_edit.setOnClickListener(this);
            grocery_IMG_BTN_checkEditName.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.equals(grocery_IMG_BTN_delete)) {
                remove(groceryProducts.get(getAdapterPosition()).getId(), true);
                if(grocery_IMG_BTN_checkEditName.getVisibility() == View.VISIBLE) {
                    isEditing = false;
                }
            } else if (v.equals(grocery_CHECKBOX_done)) {
                if (grocery_CHECKBOX_done.isChecked()) {
                    grocery_LBL_groceryProduct.setPaintFlags(grocery_LBL_groceryProduct.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }
                else {
                    grocery_LBL_groceryProduct.setPaintFlags(0);
                }

                editIsDone(groceryProducts.get(getAdapterPosition()).getId(),
                            grocery_CHECKBOX_done.isChecked(),
                            true);
            } else if (v.equals(grocery_IMG_BTN_edit)) {
                if(!isEditing) {
                    isEditing = true;

                    grocery_LBL_groceryProduct.setVisibility(View.INVISIBLE);
                    grocery_LBL_groceryQuantity.setVisibility(View.INVISIBLE);

                    grocery_IMG_BTN_edit.setVisibility(View.INVISIBLE);

                    grocery_EDITTEXT_editName.setText(grocery_LBL_groceryProduct.getText().toString());
                    grocery_EDITTEXT_editQuantity.setText(grocery_LBL_groceryQuantity.getText().toString());

                    grocery_EDITTEXT_editName.setVisibility(View.VISIBLE);
                    grocery_EDITTEXT_editQuantity.setVisibility(View.VISIBLE);

                    grocery_IMG_BTN_checkEditName.setVisibility(View.VISIBLE);

                    grocery_IMG_BTN_delete.setVisibility(View.INVISIBLE);
                } else {
                    Toast.makeText(context, "Finish editing product", Toast.LENGTH_SHORT).show();
                }
            } else if (v.equals(grocery_IMG_BTN_checkEditName)) {
                if(!grocery_EDITTEXT_editName.getText().toString().isBlank() &&
                    !grocery_EDITTEXT_editQuantity.getText().toString().isBlank()) {

                    isEditing = false;

                    grocery_EDITTEXT_editName.setVisibility(View.INVISIBLE);
                    grocery_IMG_BTN_checkEditName.setVisibility(View.INVISIBLE);

                    grocery_LBL_groceryProduct.setText(grocery_EDITTEXT_editName.getText().toString());
                    editNameAndQuantity(groceryProducts.get(getAdapterPosition()).getId(),
                            grocery_EDITTEXT_editName.getText().toString(),
                            Float.parseFloat(grocery_EDITTEXT_editQuantity.getText().toString()),
                            true);

                    grocery_LBL_groceryProduct.setVisibility(View.VISIBLE);

                    grocery_IMG_BTN_edit.setVisibility(View.VISIBLE);

                    grocery_IMG_BTN_delete.setVisibility(View.VISIBLE);
                }
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (v.equals(grocery_LBL_groceryProduct)) {
                grocery_LBL_groceryProduct.setVisibility(View.INVISIBLE);

                grocery_IMG_BTN_edit.setVisibility(View.INVISIBLE);

                grocery_EDITTEXT_editName.setText(grocery_LBL_groceryProduct.getText().toString());
                grocery_EDITTEXT_editName.setVisibility(View.VISIBLE);

                grocery_IMG_BTN_checkEditName.setVisibility(View.VISIBLE);

                return true;
            }

            return false;
        }
    }
}
