package firstbank.model;

/**
 * Abstract base class representing a bank account.
 * Subclasses must define the minimum opening deposit and any special rules.
 */
public abstract class Account {

    // ── Common state ──────────────────────────────────────────────────────────
    private String accountNumber;
    private String firstName;
    private String lastName;
    private String nin;
    private String email;
    private String phone;
    private String pin;
    private String dateOfBirth;
    private String branch;
    private double openingDeposit;

    // ── Constructor ───────────────────────────────────────────────────────────
    public Account(String accountNumber, String firstName, String lastName,
                   String nin, String email, String phone, String pin,
                   String dateOfBirth, String branch, double openingDeposit) {
        this.accountNumber  = accountNumber;
        this.firstName      = firstName;
        this.lastName       = lastName;
        this.nin            = nin;
        this.email          = email;
        this.phone          = phone;
        this.pin            = pin;
        this.dateOfBirth    = dateOfBirth;
        this.branch         = branch;
        this.openingDeposit = openingDeposit;
    }

    // ── Abstract methods ──────────────────────────────────────────────────────
    /** Returns the minimum opening deposit required for this account type. */
    public abstract double minimumDeposit();

    /** Returns the account type label (e.g. "Savings"). */
    public abstract String accountType();

    /** Returns any special rule description for this account type. */
    public abstract String specialRule();

    // ── Getters / setters ─────────────────────────────────────────────────────
    public String getAccountNumber()  { return accountNumber;  }
    public String getFirstName()      { return firstName;      }
    public String getLastName()       { return lastName;       }
    public String getNin()            { return nin;            }
    public String getEmail()          { return email;          }
    public String getPhone()          { return phone;          }
    public String getPin()            { return pin;            }
    public String getDateOfBirth()    { return dateOfBirth;    }
    public String getBranch()         { return branch;         }
    public double getOpeningDeposit() { return openingDeposit; }

    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

    // ── Formatted summary ─────────────────────────────────────────────────────
    @Override
    public String toString() {
        return String.format("ACC: %s | %s %s | %s | %s | DOB %s | %s | Deposit %,.0f | %s",
                accountNumber, firstName, lastName, accountType(),
                branch, dateOfBirth, phone, openingDeposit, email);
    }
}
