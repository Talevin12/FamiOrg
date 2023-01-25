package com.example.famiorg.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.famiorg.DataManager;
import com.example.famiorg.R;
import com.example.famiorg.iconsList;
import com.example.famiorg.logic.User;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;

public class Adapter_ChooseIcon extends RecyclerView.Adapter<Adapter_ChooseIcon.ChooseUserIconViewHolder> {

    private Context context;

    private User user;

    ChooseUserIconViewHolder currIcon;

    DataManager dataManager = new DataManager();

    public Adapter_ChooseIcon(Context context, User user) {
        this.context = context;
        this.user = user;
    }

    @NonNull
    @Override
    public ChooseUserIconViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_icon, parent, false);
        Adapter_ChooseIcon.ChooseUserIconViewHolder chooseUserIconViewHolder = new Adapter_ChooseIcon.ChooseUserIconViewHolder(view);
        return chooseUserIconViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChooseUserIconViewHolder holder, int position) {
        if(user.getIcon() == iconsList.icons[position]) {
            currIcon = holder;
            holder.chooseIcon();
        }

        holder.userIcon_IMG_icon.setImageResource(iconsList.icons[position]);

        holder.userIcon_IMG_icon.setOnClickListener(v -> {
//            if(currIcon != null)
            currIcon.unchooseIcon();

            holder.chooseIcon();
            currIcon = holder;

            updateUserIcon(iconsList.icons[position]);
        });
    }

    @Override
    public int getItemCount() {
        return iconsList.icons.length;
    }

    public void updateUserIcon(int icon) {
        dataManager.updateUserIcon(FirebaseAuth.getInstance().getCurrentUser().getUid(), icon);
    }

    public class ChooseUserIconViewHolder extends RecyclerView.ViewHolder {

        private AppCompatImageView userIcon_IMG_icon;
        private MaterialCardView userIcon_CARD_card;

        public ChooseUserIconViewHolder(@NonNull View itemView) {
            super(itemView);

            userIcon_CARD_card = itemView.findViewById(R.id.userIcon_CARD_card);

            userIcon_IMG_icon = itemView.findViewById(R.id.userIcon_IMG_icon);
        }

        private void chooseIcon() {
            userIcon_IMG_icon.setBackgroundResource(R.drawable.choose_icon);
        }

        private void unchooseIcon() {
            userIcon_IMG_icon.setBackgroundResource(0);
        }
    }
}
