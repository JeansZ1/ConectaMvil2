package com.example.conectamvil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddContactActivity extends AppCompatActivity {

    private EditText editTextContactName;
    private EditText editTextContactPhoneNumber;
    private Button buttonSaveContact;
    Button buttonBackToList;

    private DatabaseReference databaseReference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        buttonBackToList = findViewById(R.id.buttonBackToList);
        buttonBackToList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Esto hace que vuelva al activity anterior (la lista de contactos)
                finish();
            }
        });

        editTextContactName = findViewById(R.id.editTextContactName);
        editTextContactPhoneNumber = findViewById(R.id.editTextContactPhoneNumber);
        buttonSaveContact = findViewById(R.id.buttonSaveContact);

        // Obtener la referencia a la base de datos de Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference().child("contacts");

        buttonSaveContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveContactToFirebase();
            }
        });
    }

    private void saveContactToFirebase() {
        String name = editTextContactName.getText().toString().trim();
        String phoneNumber = editTextContactPhoneNumber.getText().toString().trim();

        // Validar que los campos no estén vacíos
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(phoneNumber)) {
            // Crear un nuevo objeto Contact con los datos ingresados
            Contact newContact = new Contact(name, phoneNumber);

            // Guardar el nuevo contacto en la base de datos Firebase
            databaseReference.push().setValue(newContact)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(AddContactActivity.this, "Contacto guardado con éxito", Toast.LENGTH_SHORT).show();
                                finish(); // Cerrar la actividad después de guardar el contacto
                                Log.e("Firebase", "Error al guardar el contacto: " + task.getException().getMessage());
                            } else {
                                Toast.makeText(AddContactActivity.this, "Error al guardar el contacto", Toast.LENGTH_SHORT).show();
                                // Agrega un mensaje al log en caso de error
                                Log.e("Firebase", "Error al guardar el contacto: " + task.getException().getMessage());
                            }
                        }
                    });
        } else {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
        }
    }

}