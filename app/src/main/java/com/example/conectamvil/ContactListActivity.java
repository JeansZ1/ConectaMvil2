package com.example.conectamvil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
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
        Button buttonAddContact = findViewById(R.id.buttonAddContact);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        buttonAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactListActivity.this, AddContactActivity.class);
                startActivity(intent);
            }
        });

        recyclerViewContacts = findViewById(R.id.recyclerViewContacts);
        recyclerViewContacts.setLayoutManager(new LinearLayoutManager(this));

        contactsAdapter = new ContactsAdapter(contactList);
        recyclerViewContacts.setAdapter(contactsAdapter);

        // Inicializar la referencia a la base de datos de Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference().child("contacts");

        // Leer los contactos desde Firebase Realtime Database
        readContactsFromFirebase();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_contact_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_view_user) {
            // Lógica para mostrar la vista de usuario y subir foto
            showUserProfile();
            return true;
        } else if (id == R.id.action_disconnect) {
            // Lógica para desconectar al usuario
            disconnectUser();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void showUserProfile() {
        // Implementa aquí la lógica para mostrar la vista de usuario y permitir subir una foto
        // Por ejemplo, abre una nueva actividad con la interfaz de usuario del perfil y la opción de subir una foto
        Intent intent = new Intent(this, UserProfileActivity.class);
        startActivity(intent);
    }

    private void disconnectUser() {
        // Implementa aquí la lógica para desconectar al usuario
        // Por ejemplo, cerrar sesión, borrar datos locales, etc.
        FirebaseAuth.getInstance().signOut();
        // Redirige al usuario a la actividad de inicio de sesión o a la pantalla principal de tu app
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish(); // Termina esta actividad para que el usuario no pueda volver atrás después de cerrar sesión
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