package model;

import java.time.LocalDate;

/** Current account – minimum UGX 200,000, overdraft allowed, no interest. */
public class CurrentAccount extends Account {
    
    public CurrentAccount(String accNo, String fn, String ln, String nin,
                          String email, String phone, String pin,
                          LocalDate dob, String branch, double deposit) {
        super(accNo, fn, ln, nin, email, phone, pin, dob, branch, deposit);
    }

    @Override 
    public double getMinimumOpeningDeposit() { return 200_000.0; }

    @Override 
    public String getAccountType() { return "Current"; }

    @Override 
    public String specialRule() { return "Overdraft allowed, no interest."; }
}