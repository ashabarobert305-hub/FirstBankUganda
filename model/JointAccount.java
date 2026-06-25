package model;

import java.time.LocalDate;

/** Joint account – minimum UGX 100,000, requires a second NIN. */
public class JointAccount extends Account {
    private String secondNin;

    public JointAccount(String accNo, String fn, String ln, String nin,
                        String email, String phone, String pin,
                        LocalDate dob, String branch, double deposit, String secondNin) {
        super(accNo, fn, ln, nin, email, phone, pin, dob, branch, deposit);
        this.secondNin = secondNin;
    }

    public String getSecondNin() { return secondNin; }

    @Override 
    public double getMinimumOpeningDeposit() { return 100_000.0; }

    @Override 
    public String getAccountType() { return "Joint"; }

    @Override 
    public String specialRule() { return "Requires a second NIN."; }
}
