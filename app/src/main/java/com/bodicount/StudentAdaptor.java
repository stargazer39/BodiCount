package com.bodicount;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bodicount.student.Student;
import com.bodicount.timeslot.OnEventHandler;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Locale;

public class StudentAdaptor extends RecyclerView.Adapter<StudentAdaptor.ViewHolder> {
    private List<Student> localDataSet;
    private static FirebaseFirestore db;
    private static FirebaseAuth mAuth;
    private static OnEventHandler eventRefreshHandler;

    public StudentAdaptor(List<Student> dataSet, FirebaseAuth auth, FirebaseFirestore db, OnEventHandler h) {
        localDataSet = dataSet;
        this.db = db;
        this.mAuth = auth;
        eventRefreshHandler = h;
        Log.d("student", "adaptorset" + dataSet.size());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private Student student;
        private View view;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            Context ctx = view.getContext();
            // Define click listener for the ViewHolder's View

            textView = (TextView) view.findViewById(R.id.student_row_name);
            TextView editButton = (TextView) view.findViewById(R.id.edit_student_details);

            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(ctx, view);

                    // Add menu item click listener
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()) {
                                case R.id.removeStudent:
                                    Log.d("REMOVE", "remove clicked");
                                    removeStudent(ctx);
                                    break;
                                case R.id.editSudent:
                                    editStudent();
                                    break;
                            }
                            return true;
                        }
                    });
                    popupMenu.inflate(R.menu.student_manage_options_menu);
                    popupMenu.show();
                }
            });
        }

        public TextView getTextView() {
            return textView;
        }

        public void setStudent(Student t){
            this.student = t;
        }

        public void editStudent() {
            Intent intent = new Intent(view.getContext(), StudentManagerEditStudentDetails.class);
            intent.putExtra("studentID", student.getId());
            view.getContext().startActivity(intent);
        }

        public void removeStudent(Context ctx) {
            try{
                new AlertDialog.Builder(ctx)
                        .setTitle(student.getFullName().toUpperCase(Locale.ROOT))
                        .setMessage("Do you really want to delete " + student.getFullName() + "?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                db.collection("user")
                                        .document(mAuth.getCurrentUser().getUid())
                                        .update("studentList", FieldValue.arrayRemove(student.getId()));

                            }})
                        .setNegativeButton(android.R.string.no, null).show();
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.student_manager_row, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(StudentAdaptor.ViewHolder viewHolder, final int position) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getTextView().setText(localDataSet.get(position).getFullName());
        viewHolder.setStudent(localDataSet.get(position));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}