package com.bodicount.studentsnotes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bodicount.R;
import com.bodicount.student.Student;
import com.bodicount.timeslot.TimeslotAdaptor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.List;

public class StudentNotesListAdapter extends RecyclerView.Adapter<StudentNotesListAdapter.ViewHolder> {
    public List<StudentNote> notes;
    public static OnDelete onDelete;

    public StudentNotesListAdapter(List<StudentNote> notes, OnDelete onDelete) {
        this.notes = notes;
        onDelete = onDelete;
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
        private FirebaseFirestore db;
        private FirebaseAuth auth;
        private Button delete;

        public ViewHolder(@NonNull View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.noteTitileView);
            desc = (TextView) view.findViewById(R.id.noteContent);
            delete = (Button) view.findViewById(R.id.deleteNote);

            db = FirebaseFirestore.getInstance();
            auth = FirebaseAuth.getInstance();
        }

        public void setStudentNote(StudentNote note){
            this.note = note;

            title.setText(note.getTitle());
            desc.setText(note.getTitle());

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removeStudentNote(view.getContext());
                }
            });
        }

        public void removeStudentNote(Context ctx) {
            db.collection("user")
                    .document(auth.getCurrentUser().getUid())
                    .collection("notes")
                    .document(note.getId())
                    .delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(ctx, "Delete success", Toast.LENGTH_SHORT).show();
                                onDelete.onDelete();
                            }else {
                                Toast.makeText(ctx, "Delete failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}
