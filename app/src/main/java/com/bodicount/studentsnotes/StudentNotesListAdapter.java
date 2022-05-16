package com.bodicount.studentsnotes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bodicount.R;
import com.bodicount.student.Student;
import com.bodicount.timeslot.TimeslotAdaptor;

import org.w3c.dom.Text;

import java.util.List;

public class StudentNotesListAdapter extends RecyclerView.Adapter<StudentNotesListAdapter.ViewHolder> {
    public List<StudentNote> notes;

    public StudentNotesListAdapter(List<StudentNote> notes) {
        this.notes = notes;
    }

    @Override
    public void onBindViewHolder(@NonNull StudentNotesListAdapter.ViewHolder holder, int position) {
        holder.setStudentNote(notes.get(position));
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
        private TextView title;
        private TextView desc;
        private StudentNote note;
        public ViewHolder(@NonNull View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.noteTitileView);
            desc = (TextView) view.findViewById(R.id.noteContent);
        }

        public void setStudentNote(StudentNote note){
            this.note = note;

            title.setText(note.getTitle());
            desc.setText(note.getTitle());
        }
    }
}
