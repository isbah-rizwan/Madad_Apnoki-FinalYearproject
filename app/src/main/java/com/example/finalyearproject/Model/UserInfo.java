package com.example.finalyearproject.Model;

public class UserInfo {

    private String name, email, contact, account, cnic,amount;

    public UserInfo() {}

    public UserInfo(String name, String email, String contact, String account, String cnic, String amount) {
        this.name = name;
        this.email = email;
        this.contact = contact;
        this.account = account;
        this.cnic = cnic;
        this.amount = amount;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }
}
