package model;

import java.time.LocalDate;

/** Student account – minimum UGX 10,000, applicant must be aged 18–25. */
public class StudentAccount extends Account {
    
    public StudentAccount(String accNo, String fn, String ln, String nin,
                          String email, String phone, String pin,
                          LocalDate dob, String branch, double deposit) {
        super(accNo, fn, ln, nin, email, phone, pin, dob, branch, deposit);
    }

    @Override 
    public double getMinimumOpeningDeposit() { return 10_000.0; }

    @Override 
    public String getAccountType() { return "Student"; }

    @Override 
    public String specialRule() { return "Applicant age must be 18–25."; }

    // Override the base class age validation for the specific student rule
    
    @Override
    public boolean isValidAge(int age) {
        return age >= 18 && age <= 25;
    }
}