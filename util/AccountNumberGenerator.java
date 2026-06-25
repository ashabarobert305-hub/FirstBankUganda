package util;

import database.DatabaseManager; // Make sure to add this!
import java.sql.SQLException;
import java.time.Year;
import java.util.HashMap;
import java.util.Map;

//Generates account numbers in the format BRANCHCODE-YYYY-xxxxxx.

public class AccountNumberGenerator {

    private static final Map<String, String> BRANCH_CODES = new HashMap<>();
    static {
        BRANCH_CODES.put("Kampala", "KLA");
        BRANCH_CODES.put("Gulu",    "GUL");
        BRANCH_CODES.put("Mbarara", "MBR");
        BRANCH_CODES.put("Jinja",   "JNJ");
        BRANCH_CODES.put("Mbale",   "MBL");
    }

    public static String generate(String branch) {
        String code = BRANCH_CODES.getOrDefault(branch, "UNK");
        int year = Year.now().getValue();

        try {
            int seq = DatabaseManager.getInstance().nextSequence(code, year);
            return String.format("%s-%d-%06d", code, year, seq);
        } catch (SQLException e) {
            // Log the error clearly
            System.err.println("Database error while generating account number: " + e.getMessage());
            return "ERROR-000000";
        }
    }

}
