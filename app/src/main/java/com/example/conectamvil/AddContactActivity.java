package com.example.conectamvil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.conectamvil.Contact;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddContactActivity extends AppCompatActivity {

    private EditText editTextFirstName, editTextLastName, editTextEmail, editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        // Inicializar vistas
        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastName = findViewById(R.id.editTextLastName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        Button buttonBackToList = findViewById(R.id.buttonBackToList);
        buttonBackToList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Agregar aquí la lógica para volver a la lista de contactos
                startActivity(new Intent(AddContactActivity.this, ContactListActivity.class));
            }
        });


        // Botón para registrar un usuario
        Button buttonRegisterUser = findViewById(R.id.buttonRegisterUser);
        buttonRegisterUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        // Botón para guardar contacto
        Button buttonSaveContact = findViewById(R.id.buttonRegisterUser);
        buttonSaveContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveContactToFirebase();
            }
        });
    }

    // Método para registrar un usuario en Firebase Authentication
    private void registerUser() {
        String firstName = editTextFirstName.getText().toString().trim();
        String lastName = editTextLastName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (!TextUtils.isEmpty(firstName)
                && !TextUtils.isEmpty(lastName)
                && !TextUtils.isEmpty(email)
                && !TextUtils.isEmpty(password)) {

            // Registro del usuario en Firebase Authentication
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Registro exitoso
                                Toast.makeText(AddContactActivity.this, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show();

                                // Después de registrar al usuario, guarda el contacto en la base de datos
                                saveContactToFirebase();
                            } else {
                                // Error en el registro
                                Toast.makeText(AddContactActivity.this, "Error al registrar usuario: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
        }
    }

    // Método para guardar un contacto en la base de datos de Firebase
    private void saveContactToFirebase() {
        String name = editTextFirstName.getText().toString().trim() + " " + editTextLastName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        // Verificar que el campo de nombre no esté vacío
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email)) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("contacts");
            Contact newContact = new Contact(name, email);
            databaseReference.push().setValue(newContact)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(AddContactActivity.this, "Contacto guardado con éxito", Toast.LENGTH_SHORT).show();
                                finish(); // Cierra la actividad después de guardar el contacto
                            } else {
                                Toast.makeText(AddContactActivity.this, "Error al guardar el contacto", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(this, "Completa el campo del nombre del contacto", Toast.LENGTH_SHORT).show();
        }
    }
}
