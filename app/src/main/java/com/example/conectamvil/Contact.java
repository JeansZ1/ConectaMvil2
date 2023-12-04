package com.example.conectamvil;

public class Contact {
    private String name;
    private String email;

    public Contact() {
        // Constructor vacío requerido por Firebase
    }

    public Contact(String name, String email) {
        this.name = name;
        this.email = email;
    }

    // Getters y setters para cada campo
    // Name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
