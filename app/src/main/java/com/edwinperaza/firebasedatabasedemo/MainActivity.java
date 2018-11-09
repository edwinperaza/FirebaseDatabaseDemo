package com.edwinperaza.firebasedatabasedemo;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    EditText etName, etLastName, etEmail;
    Button btnSave, btnUpdate, btnDelete;
    RecyclerView recyclerView;

    FirebaseDatabase database;
    DatabaseReference databaseReference;
    FirebaseRecyclerOptions<User> users;
    FirebaseRecyclerAdapter<User, MyRecyclerViewHolder> adapter;

    User userSelected;
    String selectedKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etName = findViewById(R.id.et_name);
        etLastName = findViewById(R.id.et_last_name);
        etEmail = findViewById(R.id.et_email);
        btnSave = findViewById(R.id.btn_save);
        btnUpdate = findViewById(R.id.btn_update);
        btnDelete = findViewById(R.id.btn_delete);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("PROFILE");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postComment();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference
                        .child(selectedKey)
                        .setValue(new User(etName.getText().toString(), etLastName.getText().toString(), etEmail.getText().toString()))
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(MainActivity.this, "onSuccess Update!!!", Toast.LENGTH_LONG).show();
                                clearData();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this, "onFailure Update!!!" + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference
                        .child(selectedKey)
                        .removeValue()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(MainActivity.this, "onSuccess Delete!!!", Toast.LENGTH_LONG).show();
                                clearData();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this, "onFailure Delete!!!" + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });

        displayComment();
    }

    private void postComment() {
        String name = etName.getText().toString();
        String lastName = etLastName.getText().toString();
        String email = etEmail.getText().toString();

        User user = new User(name, lastName, email);
        databaseReference.push().setValue(user);
        clearData();
        adapter.notifyDataSetChanged();
    }

    private void displayComment() {
        users = new FirebaseRecyclerOptions.Builder<User>()
                .setQuery(databaseReference, User.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<User, MyRecyclerViewHolder>(users) {
            @Override
            protected void onBindViewHolder(@NonNull MyRecyclerViewHolder holder, int position, @NonNull final User model) {
                holder.name.setText(model.getName());
                holder.lastName.setText(model.getLastName());
                holder.email.setText(model.getEmail());

                holder.setiItemClickListener(new IItemClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        userSelected = model;
                        selectedKey = getSnapshots().getSnapshot(position).getKey();

                        etName.setText(model.getName());
                        etLastName.setText(model.getLastName());
                        etEmail.setText(model.getEmail());
                    }
                });
            }

            @NonNull
            @Override
            public MyRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(getBaseContext()).inflate(R.layout.user_item, parent, false);
                return new MyRecyclerViewHolder(itemView);
            }
        };

        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    private void clearData() {
        etName.setText("");
        etLastName.setText("");
        etEmail.setText("");
    }

    @Override
    protected void onStop() {
        if (adapter != null) {
            adapter.stopListening();
        }
        super.onStop();
    }
}
