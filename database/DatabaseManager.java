package database;

import model.Account;
import model.JointAccount;
import java.sql.*;

/**
 * Manages all database operations for First Bank Uganda.
 * Uses SQLite by default, easily swappable to MS Access via JDBC URL.
 */
public class DatabaseManager {

    
    private static final String DB_URL = "jdbc:ucanaccess://" + System.getProperty("user.dir") + "/FirstBankUganda.accdb";

    private static DatabaseManager instance;
    private Connection connection;

    private DatabaseManager() throws SQLException {
        connection = DriverManager.getConnection(DB_URL);
        createTables();
    }

    public static synchronized DatabaseManager getInstance() throws SQLException {
        if (instance == null || instance.connection.isClosed()) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    private void createTables() throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS accounts (
                id              INTEGER PRIMARY KEY AUTOINCREMENT,
                account_number  TEXT    NOT NULL UNIQUE,
                first_name      TEXT    NOT NULL,
                last_name       TEXT    NOT NULL,
                nin             TEXT    NOT NULL,
                email           TEXT    NOT NULL,
                phone           TEXT    NOT NULL,
                date_of_birth   TEXT    NOT NULL,
                account_type    TEXT    NOT NULL,
                branch          TEXT    NOT NULL,
                opening_deposit REAL    NOT NULL,
                second_nin      TEXT,
                created_at      DATETIME DEFAULT CURRENT_TIMESTAMP
            )
            """;
        try (Statement st = connection.createStatement()) {
            st.execute(sql);
        }
    }

    /** Saves an account record to the database. */
    public void saveAccount(Account account) throws SQLException {
        String sql = """
            INSERT INTO accounts
              (account_number, first_name, last_name, nin, email, phone,
               date_of_birth, account_type, branch, opening_deposit, second_nin)
            VALUES (?,?,?,?,?,?,?,?,?,?,?)
            """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1,  account.getAccountNumber());
            ps.setString(2,  account.getFirstName());
            ps.setString(3,  account.getLastName());
            ps.setString(4,  account.getNin());
            ps.setString(5,  account.getEmail());
            ps.setString(6,  account.getPhone());
            ps.setString(7,  account.getDateOfBirth().toString()); // Converted to String
            ps.setString(8,  account.getAccountType());           // Updated method name
            ps.setString(9,  account.getBranch());
            ps.setDouble(10, account.getOpeningDeposit());
            ps.setString(11, account instanceof JointAccount 
                             ? ((JointAccount) account).getSecondNin() : null);
            ps.executeUpdate();
        }
    }

    /** Returns the next sequential number for a specific branch and year. */
    public int nextSequence(String branchCode, int year) throws SQLException {
        String selectSql = "SELECT MAX(CAST(SUBSTR(account_number, -6) AS INTEGER)) FROM accounts " +
                           "WHERE account_number LIKE ?";
        try (PreparedStatement ps = connection.prepareStatement(selectSql)) {
            ps.setString(1, branchCode + "-" + year + "-%");
            ResultSet rs = ps.executeQuery();
            if (rs.next() && rs.getObject(1) != null) {
                return rs.getInt(1) + 1;
            }
        }
        return 1; // Start at 000001 if no records exist
    }

    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) connection.close();
    }
}