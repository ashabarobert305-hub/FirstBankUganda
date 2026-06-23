package firstbank.model;

/** Fixed Deposit account – minimum UGX 1,000,000, locked term, highest interest. */
public class FixedDepositAccount extends Account {
    public FixedDepositAccount(String accNo, String fn, String ln, String nin,
                               String email, String phone, String pin,
                               String dob, String branch, double deposit) {
        super(accNo, fn, ln, nin, email, phone, pin, dob, branch, deposit);
    }
    @Override public double minimumDeposit() { return 1_000_000; }
    @Override public String accountType()    { return "Fixed Deposit"; }
    @Override public String specialRule()    { return "Locked term, highest interest."; }
}
