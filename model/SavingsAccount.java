package model;

import java.time.LocalDate;

/** Savings account – minimum UGX 50,000, earns interest, no overdraft. */
public class SavingsAccount extends Account {
    
    public SavingsAccount(String accNo, String fn, String ln, String nin,
                          String email, String phone, String pin,
                          LocalDate dob, String branch, double deposit) {
        super(accNo, fn, ln, nin, email, phone, pin, dob, branch, deposit);
    }

    @Override 
    public double getMinimumOpeningDeposit() { return 50_000.0; }

    @Override 
    public String getAccountType() { return "Savings"; }

    @Override 
    public String specialRule() { return "Earns interest, no overdraft."; }
}