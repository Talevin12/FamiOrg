package com.example.famiorg.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.famiorg.R;
import com.example.famiorg.assets.CalendarUtils;
import com.example.famiorg.logic.DailyEvent;
import com.example.famiorg.logic.User;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AdapterEvent extends ArrayAdapter<DailyEvent> {
    private ArrayList<User> familyMembers;
    public AdapterEvent(@NonNull Context context, List<DailyEvent> events, ArrayList<User> familyMembers)
    {
        super(context, 0, events);
        this.sort(Comparator.comparing(DailyEvent::getLocalDateTimeStart));
        this.familyMembers = familyMembers;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        DailyEvent event = getItem(position);

        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.event_cell, parent, false);

        TextView eventCell_LBL_name = convertView.findViewById(R.id.eventCell_LBL_name);
        TextView eventCell_LBL_description = convertView.findViewById(R.id.eventCell_LBL_description);
        TextView eventCell_LBL_startTime = convertView.findViewById(R.id.eventCell_LBL_startTime);
        TextView eventCell_LBL_endTime = convertView.findViewById(R.id.eventCell_LBL_endTime);
        RecyclerView eventCell_LST_MembersIcons = convertView.findViewById(R.id.eventCell_LST_MembersIcons);

        eventCell_LBL_name.setText(event.getTitle());
        eventCell_LBL_description.setText(event.getDescription());
        eventCell_LBL_startTime.setText(CalendarUtils.formattedTime(event.getLocalDateTimeStart().toLocalTime()));
        eventCell_LBL_endTime.setText(CalendarUtils.formattedTime(event.getLocalDateTimeEnd().toLocalTime()));

        ArrayList<User> eventRelatedMembers = new ArrayList<>();
        for(String email : event.getFamilyMembersParticipatingEmails()) {
            for(User member : familyMembers) {
                if(email.equals(member.getEmail())) {
                    eventRelatedMembers.add(member);
                    break;
                }
            }
        }

        Adapter_MyfamilyIcons adapter_myfamilyIcons = new Adapter_MyfamilyIcons(getContext(), eventRelatedMembers);
        eventCell_LST_MembersIcons.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        eventCell_LST_MembersIcons.setAdapter(adapter_myfamilyIcons);

        return convertView;
    }
}
