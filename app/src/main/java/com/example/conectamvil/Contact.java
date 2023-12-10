package com.example.conectamvil;

public class Contact {
    private String username;
    private String email;

    // Constructor
    public Contact(String username, String email) {
        this.username = username;
        this.email = email;
    }

    // Métodos getter y setter para el nombre de usuario y el email
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
