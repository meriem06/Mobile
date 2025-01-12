package com.example.usermanagmentecotracker.JihedPackage.emailVerification;

public class EmailRequest {
    private String email;
    private String message;

    public EmailRequest(String email, String message) {
        this.email = email;
        this.message = message;
    }

    // Getters and setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
