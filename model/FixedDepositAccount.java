package model;

import java.time.LocalDate;

/** Fixed Deposit account – minimum UGX 1,000,000, locked term, highest interest. */
public class FixedDepositAccount extends Account {
    
    public FixedDepositAccount(String accNo, String fn, String ln, String nin,
                               String email, String phone, String pin,
                               LocalDate dob, String branch, double deposit) {
        super(accNo, fn, ln, nin, email, phone, pin, dob, branch, deposit);
    }

    @Override 
    public double getMinimumOpeningDeposit() { return 1_000_000.0; }

    @Override 
    public String getAccountType() { return "Fixed Deposit"; }

    @Override 
    public String specialRule() { return "Locked term, highest interest."; }
}