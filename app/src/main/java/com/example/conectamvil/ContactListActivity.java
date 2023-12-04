package com.example.conectamvil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ContactListActivity extends AppCompatActivity {

    private RecyclerView recyclerViewContacts;
    private ContactsAdapter contactsAdapter;
    private List<Contact> contactList = new ArrayList<>();

    // Referencia a la base de datos de Firebase
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        recyclerViewContacts = findViewById(R.id.recyclerViewContacts);
        recyclerViewContacts.setLayoutManager(new LinearLayoutManager(this));

        contactsAdapter = new ContactsAdapter(contactList);
        recyclerViewContacts.setAdapter(contactsAdapter);

        // Inicializar la referencia a la base de datos de Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference().child("contacts");

        // Leer los contactos desde Firebase Realtime Database
        readContactsFromFirebase();
    }

    private void readContactsFromFirebase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                contactList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Contact contact = snapshot.getValue(Contact.class);
                    contactList.add(contact);
                }

                contactsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Manejar errores de lectura desde Firebase
                Log.e("Firebase", "Error al leer los contactos: " + databaseError.getMessage());
                Toast.makeText(ContactListActivity.this, "Error al obtener los contactos", Toast.LENGTH_SHORT).show();
            }
        });
    }
}