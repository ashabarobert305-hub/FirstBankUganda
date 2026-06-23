package firstbank.model;

/**
 * Factory that creates the correct Account subclass based on the selected type.
 * Enables polymorphic deposit validation in the form.
 */
public class AccountFactory {

    public static Account create(String type, String accNo,
                                 String fn, String ln, String nin,
                                 String email, String phone, String pin,
                                 String dob, String branch, double deposit,
                                 String secondNin) {
        switch (type) {
            case "Savings":       return new SavingsAccount(accNo, fn, ln, nin, email, phone, pin, dob, branch, deposit);
            case "Current":       return new CurrentAccount(accNo, fn, ln, nin, email, phone, pin, dob, branch, deposit);
            case "Fixed Deposit": return new FixedDepositAccount(accNo, fn, ln, nin, email, phone, pin, dob, branch, deposit);
            case "Student":       return new StudentAccount(accNo, fn, ln, nin, email, phone, pin, dob, branch, deposit);
            case "Joint":         return new JointAccount(accNo, fn, ln, nin, email, phone, pin, dob, branch, deposit, secondNin);
            default: throw new IllegalArgumentException("Unknown account type: " + type);
        }
    }

    /** Returns the minimum deposit for a given account type string (for UI preview). */
    public static double minimumFor(String type) {
        switch (type) {
            case "Savings":       return 50_000;
            case "Current":       return 200_000;
            case "Fixed Deposit": return 1_000_000;
            case "Student":       return 10_000;
            case "Joint":         return 100_000;
            default:              return 0;
        }
    }
}
