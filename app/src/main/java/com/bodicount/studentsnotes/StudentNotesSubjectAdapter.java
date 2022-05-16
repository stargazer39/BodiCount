package com.bodicount.studentsnotes;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bodicount.R;
import com.bodicount.timeslot.Timeslot;
import com.bodicount.timeslot.TimeslotAdaptor;
import com.bodicount.timetable.Timetable;

import java.sql.Time;
import java.util.List;

public class StudentNotesSubjectAdapter extends RecyclerView.Adapter<StudentNotesSubjectAdapter.ViewHolder>{
    private List<Timeslot> list;

    public StudentNotesSubjectAdapter(List<Timeslot> list){
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.student_note, parent, false);

        return new StudentNotesSubjectAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setTimetable(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private Timeslot timeslot;
        private TextView name;

        public ViewHolder(@NonNull View view) {
            super(view);

            Context ctx = view.getContext();
            name = (TextView)view.findViewById(R.id.studentNoteName);
        }

        public void setTimetable(Timeslot t){
            this.timeslot = t;
            name.setText(timeslot.getSlotName());

            if(timeslot.getId() == "NOTES"){
                name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        view.getContext().startActivity(new Intent(view.getContext(), StudentsNotesAdd.class));
                    }
                });
            }

        }
    }
}
