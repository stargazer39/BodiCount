package com.bodicount.studentsnotes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bodicount.R;
import com.bodicount.timeslot.TimeslotAdaptor;

import java.util.List;

public class StudentNotesListAdapter extends RecyclerView.Adapter<StudentNotesListAdapter.ViewHolder> {
    public List<StudentNote> notes;

    public StudentNotesListAdapter(List<StudentNote> notes) {
        this.notes = notes;
    }

    @Override
    public void onBindViewHolder(@NonNull StudentNotesListAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.student_note_detail, viewGroup, false);

        return new ViewHolder(view);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
