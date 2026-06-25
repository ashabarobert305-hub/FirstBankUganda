package model;

import java.time.LocalDate;

public abstract class Account {

    // ── Common state ──────────────────────────────────────────────────────────
    private String accountNumber;
    private String firstName;
    private String lastName;
    private String nin;
    private String email;
    private String phone;
    private String pin;
    private LocalDate dateOfBirth;
    private String branch;
    private double openingDeposit;

    // ── Constructor ───────────────────────────────────────────────────────────
    public Account(String accountNumber, String firstName, String lastName,
                   String nin, String email, String phone, String pin,
                   LocalDate dateOfBirth, String branch, double openingDeposit) {
        this.accountNumber = accountNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.nin = nin;
        this.email = email;
        this.phone = phone;
        this.pin = pin;
        this.dateOfBirth = dateOfBirth;
        this.branch = branch;
        this.openingDeposit = openingDeposit;
    }

    // ── Abstract methods for Polymorphism ─────────────────────────────────────
    
    /** Returns the minimum opening deposit required for this account type. */
    public abstract double getMinimumOpeningDeposit();

    /** Returns the account type label (e.g., "Savings"). */
    public abstract String getAccountType();

    /** Returns any special rule description for this account type. */
    public abstract String specialRule();

    // ── Validation logic ──────────────────────────────────────────────────────
    
    /** Checks if the deposit meets the minimum requirement for this specific account. */
    public boolean isDepositValid() {
        return this.openingDeposit >= getMinimumOpeningDeposit();
    }

    /** Default age validation; subclasses can override for specific rules (e.g., Student). */
    public boolean isValidAge(int age) {
        return age >= 18 && age <= 75;
    }

    // ── Getters and Setters ───────────────────────────────────────────────────
    
    public String getAccountNumber()  { return accountNumber; }
    public String getFirstName()      { return firstName; }
    public String getLastName()       { return lastName; }
    public String getNin()            { return nin; }
    public String getEmail()          { return email; }
    public String getPhone()          { return phone; }
    public String getPin()            { return pin; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public String getBranch()         { return branch; }
    public double getOpeningDeposit() { return openingDeposit; }

    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    public void setOpeningDeposit(double openingDeposit) { this.openingDeposit = openingDeposit; }

    // ── Formatted summary ─────────────────────────────────────────────────────
    
    @Override
    public String toString() {
        return String.format("ACC: %s | %s %s | %s | %s | DOB %s | %s | Deposit %,.0f | %s",
                accountNumber, firstName, lastName, getAccountType(),
                branch, dateOfBirth.toString(), phone, openingDeposit, email);
    }
}