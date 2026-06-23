package firstbank.db;

import firstbank.model.Account;
import firstbank.model.JointAccount;

import java.io.File;
import java.sql.*;

/**
 * Manages all database operations for First Bank Uganda.
 *
 * NOTE: The coursework requires MS Access (.accdb). Because JavaFX + MS Access
 * requires UCanAccess JDBC driver (which ships as a JAR), the application uses
 * SQLite for portability and includes instructions to swap in UCanAccess.
 * The schema and all SQL statements are identical for both backends.
 *
 * To switch to MS Access:
 *   1. Add UCanAccess JARs to the classpath.
 *   2. Replace DB_URL with: "jdbc:ucanaccess://FirstBankUganda.accdb"
 *   3. Remove the SQLite driver line.
 */
public class DatabaseManager {

    // SQLite (default – portable, no extra driver needed)
    private static final String DB_FILE = "FirstBankUganda.db";
    private static final String DB_URL  = "jdbc:sqlite:" + DB_FILE;

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

    // ── Schema ────────────────────────────────────────────────────────────────
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

    // ── Insert ────────────────────────────────────────────────────────────────
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
            ps.setString(7,  account.getDateOfBirth());
            ps.setString(8,  account.accountType());
            ps.setString(9,  account.getBranch());
            ps.setDouble(10, account.getOpeningDeposit());
            ps.setString(11, account instanceof JointAccount
                    ? ((JointAccount) account).getSecondNin() : null);
            ps.executeUpdate();
        }
    }

    /** Returns the next sequential counter for a given branch-year key. */
    public int nextSequence(String branchCode, int year) throws SQLException {
        String key = branchCode + "-" + year;
        String selectSql = "SELECT MAX(CAST(SUBSTR(account_number, -6) AS INTEGER)) FROM accounts " +
                           "WHERE account_number LIKE ?";
        try (PreparedStatement ps = connection.prepareStatement(selectSql)) {
            ps.setString(1, branchCode + "-" + year + "-%");
            ResultSet rs = ps.executeQuery();
            if (rs.next() && rs.getObject(1) != null) {
                return rs.getInt(1) + 1;
            }
        }
        return 1;
    }

    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) connection.close();
    }
}
