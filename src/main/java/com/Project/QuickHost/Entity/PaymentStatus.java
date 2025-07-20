package com.Project.QuickHost.Entity;

public enum PaymentStatus {
    PENDING, // Payment is initiated but not completed
    CONFORMED, // Payment has been successfully completed
    FAILED, // Payment failed due to an error or insufficient funds
    REFUNDED, // Payment has been refunded
    CANCELLED // Payment was cancelled by the user or system
}
