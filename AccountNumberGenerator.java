package firstbank.util;

import java.time.Year;
import java.util.HashMap;
import java.util.Map;

/**
 * Generates account numbers in the format BRANCHCODE-YYYY-xxxxxx.
 * Uses an in-memory per-year per-branch sequential counter.
 * In production this counter would be persisted in the database.
 */
public class AccountNumberGenerator {

    private static final Map<String, Integer> COUNTERS = new HashMap<>();

    private static final Map<String, String> BRANCH_CODES = new HashMap<>();
    static {
        BRANCH_CODES.put("Kampala", "KLA");
        BRANCH_CODES.put("Gulu",    "GUL");
        BRANCH_CODES.put("Mbarara", "MBR");
        BRANCH_CODES.put("Jinja",   "JNJ");
        BRANCH_CODES.put("Mbale",   "MBL");
    }

    public static synchronized String generate(String branch) {
        String code = BRANCH_CODES.getOrDefault(branch, "UNK");
        int year = Year.now().getValue();
        String key = code + "-" + year;
        int seq = COUNTERS.getOrDefault(key, 0) + 1;
        COUNTERS.put(key, seq);
        return String.format("%s-%d-%06d", code, year, seq);
    }
}
