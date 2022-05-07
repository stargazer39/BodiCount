package com.bodicount.timetable;

import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.content.Context;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bodicount.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Locale;

public class TimetableAdaptor extends RecyclerView.Adapter<TimetableAdaptor.ViewHolder> {
    private List<Timetable> localDataSet;
    private static FirebaseFirestore db;
    private static FirebaseAuth mAuth;
    private static OnEventHandler eventRefreshHandler;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private Timetable timetable = null;


        public ViewHolder(View view) {
            super(view);
            Context ctx = view.getContext();

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    eventRefreshHandler.editTimeslots(timetable.getTableName());
                }
            });

            textView = (TextView) view.findViewById(R.id.student_row_name);
            TextView editButton = (TextView) view.findViewById(R.id.edit_time_slot);

            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(ctx, view);

                    // Add menu item click listener
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()){
                                case R.id.removeTimetable:
                                    Log.d("REMOVE", "remove clicked");
                                    removeTimetable(ctx);
                            }
                            return true;
                        }
                    });
                    popupMenu.inflate(R.menu.timetable_options_menu);
                    popupMenu.show();
                }
            });
        }

        public TextView getTextView() {
            return textView;
        }

        public void setTimetable(Timetable t){
            this.timetable = t;
        }

        public void removeTimetable(Context ctx) {
            try{
                new AlertDialog.Builder(ctx)
                        .setTitle(timetable.getTableName().toUpperCase(Locale.ROOT))
                        .setMessage("Do you really want to delete " + timetable.getTableName() + "?.\nUsers associated with this table will get disassociated.")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                db.collection("user")
                                    .document(mAuth.getCurrentUser().getUid())
                                    .collection("timetables")
                                    .document(timetable.getTableName())
                                    .delete()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    Toast.makeText(ctx, "Delete success", Toast.LENGTH_SHORT).show();
                                                    eventRefreshHandler.onEvent();
                                                }else{
                                                    Toast.makeText(ctx, "Delete faild. Have internet?", Toast.LENGTH_SHORT).show();
                                                    task.getException().printStackTrace();
                                                }
                                            }
                                        });

                            }})
                        .setNegativeButton(android.R.string.no, null).show();
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    public TimetableAdaptor(List<Timetable> dataSet, FirebaseAuth auth, FirebaseFirestore db, OnEventHandler h) {
        localDataSet = dataSet;
        this.db = db;
        this.mAuth = auth;
        eventRefreshHandler = h;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.time_table, viewGroup, false);

        return new ViewHolder(view);
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getTextView().setText(localDataSet.get(position).getTableName());
        viewHolder.setTimetable(localDataSet.get(position));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
