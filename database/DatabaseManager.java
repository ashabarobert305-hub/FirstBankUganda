package database;

import model.Account;
import model.JointAccount;
import java.sql.*;

/**
 * Manages all database operations for First Bank Uganda.
 * Uses SQLite by default, easily swappable to MS Access via JDBC URL.
 */
public class DatabaseManager {

    
    private static final String DB_URL = "jdbc:ucanaccess://" + System.getProperty("user.dir") + "/FirstBankUganda1.accdb";

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
        DatabaseMetaData dbm = connection.getMetaData();
        // Check if "accounts" table already exists
        ResultSet tables = dbm.getTables(null, null, "accounts", null);
        
        if (!tables.next()) {
            // Table does not exist, so create it without "IF NOT EXISTS"
            String sql = """
                CREATE TABLE accounts (
                    id              COUNTER PRIMARY KEY,
                    account_number  TEXT(50) NOT NULL UNIQUE,
                    first_name      TEXT(50) NOT NULL,
                    last_name       TEXT(50) NOT NULL,
                    nin             TEXT(50) NOT NULL,
                    email           TEXT(100) NOT NULL,
                    phone           TEXT(20) NOT NULL,
                    date_of_birth   TEXT(20) NOT NULL,
                    account_type    TEXT(50) NOT NULL,
                    branch          TEXT(50) NOT NULL,
                    opening_deposit DOUBLE   NOT NULL,
                    second_nin      TEXT(50),
                    created_at      DATETIME DEFAULT NOW()
                )
                """;
                try (Statement st = connection.createStatement()) {
                    st.execute(sql);
                }
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