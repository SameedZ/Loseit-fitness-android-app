package com.example.gym;

import java.io.Serializable;

public class PaymentClass implements Serializable {
    private String Payment_Amount;
    private String Payment_Date;
    private String Payment_Time;
    private String email;

    public PaymentClass() {
    }

    public PaymentClass(String payment_Amount, String payment_Date, String payment_Time, String email) {
        this.Payment_Amount = payment_Amount;
        this.Payment_Date = payment_Date;
        this.Payment_Time = payment_Time;
        this.email = email;
    }

    public String getPayment_Amount() {
        return Payment_Amount;
    }

    public void setPayment_Amount(String payment_Amount) {
        this.Payment_Amount = payment_Amount;
    }

    public String getPayment_Date() {
        return Payment_Date;
    }

    public void setPayment_Date(String payment_Date) {
        this.Payment_Date = payment_Date;
    }

    public String getPayment_Time() {
        return Payment_Time;
    }

    public void setPayment_Time(String payment_Time) {
        this.Payment_Time = payment_Time;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
