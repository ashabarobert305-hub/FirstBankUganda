package firstbank.model;

/** Student account – minimum UGX 10,000, applicant must be aged 18–25. */
public class StudentAccount extends Account {
    public StudentAccount(String accNo, String fn, String ln, String nin,
                          String email, String phone, String pin,
                          String dob, String branch, double deposit) {
        super(accNo, fn, ln, nin, email, phone, pin, dob, branch, deposit);
    }
    @Override public double minimumDeposit() { return 10_000; }
    @Override public String accountType()    { return "Student"; }
    @Override public String specialRule()    { return "Applicant age must be 18–25."; }
}
