package com.example.famiorg.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.famiorg.R;
import com.example.famiorg.logic.User;

import java.util.ArrayList;

public class Adapter_MyfamilyIcons extends RecyclerView.Adapter<Adapter_MyfamilyIcons.UserIconViewHolder> {

    private Context context;
    private ArrayList<User> familyMembers;

    public Adapter_MyfamilyIcons(Context context, ArrayList<User> familyMembers) {
        this.context = context;
        this.familyMembers = familyMembers;
    }

    @NonNull
    @Override
    public UserIconViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_icon, parent, false);
        Adapter_MyfamilyIcons.UserIconViewHolder myFamilyIconViewHolder = new Adapter_MyfamilyIcons.UserIconViewHolder(view);
        return myFamilyIconViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UserIconViewHolder holder, int position) {
        User member = familyMembers.get(position);

        holder.userIcon_IMG_icon.setImageResource(member.getIcon());

        holder.userIcon_IMG_icon.setOnClickListener(v -> {
            Toast.makeText(context,member.getName(),Toast.LENGTH_SHORT).show();

            RotateAnimation anim = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            anim.setInterpolator(new LinearInterpolator());
            anim.setRepeatCount(Animation.ABSOLUTE);
            anim.setDuration(700);

            holder.userIcon_IMG_icon.startAnimation(anim);
        });
    }

    @Override
    public int getItemCount() {
        return familyMembers == null ? 0 : familyMembers.size();
    }

    public void addOrUpdate(User user) {
        int i = 0;
        for(User member : familyMembers) {
            if(member.getEmail().equals(user.getEmail())) {
                familyMembers.remove(member);
                notifyItemRemoved(i);
                break;
            }

            i++;
        }

        familyMembers.add(i, user);
        notifyItemInserted(i);
    }

    public void remove(User user) {
        int pos = familyMembers.indexOf(user);
        familyMembers.remove(user);
        notifyItemRemoved(pos);
    }

    class UserIconViewHolder extends RecyclerView.ViewHolder {

        private AppCompatImageView userIcon_IMG_icon;

        public UserIconViewHolder(View itemView) {
            super(itemView);

            userIcon_IMG_icon = itemView.findViewById(R.id.userIcon_IMG_icon);
            int a = R.drawable.ic_bunny;

            System.out.println(a);
        }
    }
}
