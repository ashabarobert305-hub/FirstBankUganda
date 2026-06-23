package firstbank.model;

/** Savings account – minimum UGX 50,000, earns interest, no overdraft. */
public class SavingsAccount extends Account {
    public SavingsAccount(String accNo, String fn, String ln, String nin,
                          String email, String phone, String pin,
                          String dob, String branch, double deposit) {
        super(accNo, fn, ln, nin, email, phone, pin, dob, branch, deposit);
    }
    @Override public double minimumDeposit() { return 50_000; }
    @Override public String accountType()    { return "Savings"; }
    @Override public String specialRule()    { return "Earns interest, no overdraft."; }
}
