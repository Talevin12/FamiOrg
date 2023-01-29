package com.example.famiorg.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.famiorg.R;
import com.example.famiorg.logic.DayCalendar;

import java.time.LocalDate;
import java.util.ArrayList;

public class AdapterCalendar extends RecyclerView.Adapter<AdapterCalendar.CalendarViewHolder> {

    private Context context;
    private final ArrayList<DayCalendar> daysOfMonth;
    private final OnItemListener onItemListener;
    private int selectedDayOfMonth;

    public AdapterCalendar(Context context, ArrayList<DayCalendar> daysOfMonth, OnItemListener onItemListener, int selectedDayOfMonth)
    {
        this.context = context;
        this.daysOfMonth = daysOfMonth;
        this.onItemListener = onItemListener;

        this.selectedDayOfMonth = selectedDayOfMonth;
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calendar_cell, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();

        if(daysOfMonth.size() > 15) //month view
            layoutParams.height = (int) (parent.getHeight() * 0.166666666);
        else // week view
            layoutParams.height = parent.getHeight();

        return new CalendarViewHolder(view, onItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position)
    {
        DayCalendar day = daysOfMonth.get(position);

        if(day.getDate() != null) {
            holder.dayOfMonth.setText(""+ day.getDate().getDayOfMonth());

            if (day.getDate().getDayOfMonth() == selectedDayOfMonth) {
                holder.cellDayLayout.setBackgroundTintList(context.getColorStateList(R.color.gray));
            } else if (LocalDate.now().getYear() == day.getDate().getYear() &&
                    LocalDate.now().getMonth().getValue() == day.getDate().getMonth().getValue() &&
                        LocalDate.now().getDayOfMonth() == day.getDate().getDayOfMonth()) {
                holder.cellDayLayout.setBackgroundTintList(context.getColorStateList(R.color.blue));
            } else {
                holder.cellDayLayout.setBackgroundTintList(context.getColorStateList(R.color.white));
            }
        } else {
            holder.dayOfMonth.setText("");
            holder.cellDayLayout.setBackgroundTintList(context.getColorStateList(R.color.white));
        }

        if(day.getEvents().size() > 0) {
            if(day.getEvents().size() == 1) {
                holder.cellDayDot.setVisibility(View.VISIBLE);
                holder.cellDay3Dots.setVisibility(View.INVISIBLE);
            } else {
                holder.cellDayDot.setVisibility(View.INVISIBLE);
                holder.cellDay3Dots.setVisibility(View.VISIBLE);
            }
        } else {
            holder.cellDayDot.setVisibility(View.INVISIBLE);
            holder.cellDay3Dots.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount()
    {
        return daysOfMonth.size();
    }

    public interface OnItemListener
    {
        void onItemClick(int position, DayCalendar dayCalendar);
    }

    class CalendarViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public final TextView dayOfMonth;
        public final RelativeLayout cellDayLayout;
        public final AppCompatImageView cellDayDot;
        public final AppCompatImageView cellDay3Dots;
        private final AdapterCalendar.OnItemListener onItemListener;
        public CalendarViewHolder(@NonNull View itemView, AdapterCalendar.OnItemListener onItemListener)
        {
            super(itemView);

            dayOfMonth = itemView.findViewById(R.id.cellDayText);
            cellDayLayout = itemView.findViewById(R.id.cellDayLayout);
            cellDayDot = itemView.findViewById(R.id.cellDayDot);
            cellDay3Dots = itemView.findViewById(R.id.cellDay3Dots);

            this.onItemListener = onItemListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view)
        {
            selectedDayOfMonth = getAdapterPosition()+1;
            onItemListener.onItemClick(getAdapterPosition(), daysOfMonth.get(getAdapterPosition()));
            notifyDataSetChanged();
        }
    }
}
