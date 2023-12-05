package com.example.conectamvil;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfileActivity extends AppCompatActivity {

    private EditText editTextUserName;
    private EditText editTextEmail;
    private Button buttonEdit;
    private Button buttonSave;
    private String currentUserId; // Variable para almacenar el ID del usuario logueado

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        editTextUserName = findViewById(R.id.editTextUserName);
        editTextEmail = findViewById(R.id.editTextEmail);
        buttonEdit = findViewById(R.id.buttonEdit);
        buttonSave = findViewById(R.id.buttonSave);

        // Obtener el usuario actualmente autenticado
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            currentUserId = currentUser.getUid(); // Obtiene el ID del usuario autenticado
        }

        // Obtener los datos del usuario desde Firebase Realtime Database
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserId);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Verificar si los datos existen en la base de datos
                if (dataSnapshot.exists()) {
                    // Obtener los valores de nombre de usuario y correo electrónico
                    String username = dataSnapshot.child("username").getValue(String.class);
                    String email = dataSnapshot.child("email").getValue(String.class);

                    // Mostrar los valores en los EditText
                    editTextUserName.setText(username);
                    editTextEmail.setText(email);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Manejar errores de lectura desde Firebase
                Log.e("Firebase", "Error al obtener los datos del usuario: " + databaseError.getMessage());
            }
        });

        // Botón para editar los datos del usuario
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ocultar el botón de Editar y mostrar el botón de Guardar
                buttonEdit.setVisibility(View.GONE);
                buttonSave.setVisibility(View.VISIBLE);

                // Habilitar la edición en los EditText
                editTextUserName.setEnabled(true);
                editTextEmail.setEnabled(true);
            }
        });

        // Botón para guardar los datos editados del usuario
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Guardar los datos editados en Firebase Realtime Database
                String editedUsername = editTextUserName.getText().toString().trim();
                String editedEmail = editTextEmail.getText().toString().trim();

                DatabaseReference currentUserRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserId);
                currentUserRef.child("username").setValue(editedUsername);
                currentUserRef.child("email").setValue(editedEmail);

                // Ocultar el botón de Guardar y mostrar el botón de Editar
                buttonSave.setVisibility(View.GONE);
                buttonEdit.setVisibility(View.VISIBLE);

                // Deshabilitar la edición en los EditText
                editTextUserName.setEnabled(false);
                editTextEmail.setEnabled(false);
            }
        });

        Button buttonChangeImage = findViewById(R.id.buttonChangeImage);
        buttonChangeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abre la galería para seleccionar una imagen
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            // Obtiene la imagen seleccionada desde la galería
            Uri imageUri = data.getData();

            // Actualiza la imagen de perfil en la interfaz y en Firebase
            updateProfileImage(imageUri);
        }
    }

    private void updateProfileImage(Uri imageUri) {
        DatabaseReference currentUserRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserId);
        currentUserRef.child("profileImageUrl").setValue(imageUri.toString());

        // Muestra la imagen en el ImageView usando Glide o tu biblioteca de imágenes preferida
        ImageView imageViewProfile = findViewById(R.id.imageViewProfile);
        Glide.with(this).load(imageUri).into(imageViewProfile);
    }
}
