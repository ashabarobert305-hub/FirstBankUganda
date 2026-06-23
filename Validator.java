package firstbank.util;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Centralised validation utility for the account opening form.
 * Each method returns an error message string, or null if valid.
 */
public class Validator {

    private static final Pattern NAME_PATTERN  = Pattern.compile("[A-Za-z]{2,30}");
    private static final Pattern NIN_PATTERN   = Pattern.compile("[A-Z0-9]{14}");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w._%+\\-]+@[\\w.\\-]+\\.[a-zA-Z]{2,}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+256\\d{9}$");
    private static final Pattern PIN_PATTERN   = Pattern.compile("\\d{4,6}");

    public static String validateName(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) return fieldName + " is required.";
        String t = value.trim();
        if (!NAME_PATTERN.matcher(t).matches())
            return fieldName + " must be letters only, 2–30 characters.";
        return null;
    }

    public static String validateNin(String value) {
        if (value == null || value.trim().isEmpty()) return "National ID (NIN) is required.";
        String t = value.trim();
        if (!NIN_PATTERN.matcher(t).matches())
            return "NIN must be exactly 14 uppercase alphanumeric characters.";
        return null;
    }

    public static String validateEmail(String value) {
        if (value == null || value.trim().isEmpty()) return "Email is required.";
        if (!EMAIL_PATTERN.matcher(value.trim()).matches()) return "Email format is invalid.";
        return null;
    }

    public static String validateEmailMatch(String email, String confirm) {
        String e1 = validateEmail(email);
        if (e1 != null) return e1;
        if (confirm == null || confirm.trim().isEmpty()) return "Confirm Email is required.";
        if (!email.trim().equalsIgnoreCase(confirm.trim())) return "Emails do not match.";
        return null;
    }

    public static String validatePhone(String value) {
        if (value == null || value.trim().isEmpty()) return "Phone Number is required.";
        if (!PHONE_PATTERN.matcher(value.trim()).matches())
            return "Phone must be in format +256XXXXXXXXX (9 digits after +256).";
        return null;
    }

    public static String validatePin(String value) {
        if (value == null || value.trim().isEmpty()) return "PIN is required.";
        String t = value.trim();
        if (!PIN_PATTERN.matcher(t).matches()) return "PIN must be 4–6 numeric digits.";
        // all-identical digits check
        if (t.chars().distinct().count() == 1) return "PIN must not be all identical digits (e.g., 0000).";
        return null;
    }

    public static String validatePinMatch(String pin, String confirm) {
        String p = validatePin(pin);
        if (p != null) return p;
        if (confirm == null || confirm.trim().isEmpty()) return "Confirm PIN is required.";
        if (!pin.trim().equals(confirm.trim())) return "PINs do not match.";
        return null;
    }

    public static String validateDob(int year, int month, int day) {
        if (year <= 0 || month <= 0 || day <= 0) return "Date of Birth is required.";
        try {
            LocalDate.of(year, month, day); // throws if invalid
        } catch (Exception e) {
            return "Date of Birth is invalid.";
        }
        return null;
    }

    public static String validateAge(int year, int month, int day, String accountType) {
        String dobErr = validateDob(year, month, day);
        if (dobErr != null) return dobErr;
        LocalDate dob  = LocalDate.of(year, month, day);
        LocalDate today = LocalDate.now();
        int age = Period.between(dob, today).getYears();
        if (age < 18 || age > 75) return "Age must be between 18 and 75. Computed age: " + age;
        if ("Student".equals(accountType) && (age < 18 || age > 25))
            return "Student account requires age 18–25. Computed age: " + age;
        return null;
    }

    public static String validateDeposit(String value, String accountType) {
        if (value == null || value.trim().isEmpty()) return "Opening Deposit is required.";
        double amount;
        try {
            amount = Double.parseDouble(value.trim().replace(",", ""));
        } catch (NumberFormatException e) {
            return "Opening Deposit must be a numeric value.";
        }
        double min = firstbank.model.AccountFactory.minimumFor(accountType);
        if (amount < min)
            return String.format("Minimum deposit for %s is UGX %,.0f. You entered %,.0f.", accountType, min, amount);
        return null;
    }

    public static String validateSecondNin(String value, String accountType) {
        if (!"Joint".equals(accountType)) return null;
        return validateNin(value) == null ? null : "Second NIN: " + validateNin(value);
    }

    /** Run all validations and return a list of all error messages. */
    public static List<String> validateAll(
            String firstName, String lastName, String nin,
            String email, String confirmEmail,
            String phone, String pin, String confirmPin,
            int dobYear, int dobMonth, int dobDay,
            String accountType, String branch,
            String deposit, String secondNin) {

        List<String> errors = new ArrayList<>();
        addIfNotNull(errors, validateName(firstName, "First Name"));
        addIfNotNull(errors, validateName(lastName,  "Last Name"));
        addIfNotNull(errors, validateNin(nin));
        addIfNotNull(errors, validateEmailMatch(email, confirmEmail));
        addIfNotNull(errors, validatePhone(phone));
        addIfNotNull(errors, validatePinMatch(pin, confirmPin));
        addIfNotNull(errors, validateAge(dobYear, dobMonth, dobDay, accountType));
        if (accountType == null || accountType.trim().isEmpty()) errors.add("Account Type must be selected.");
        if (branch == null || branch.trim().isEmpty()) errors.add("Branch must be selected.");
        addIfNotNull(errors, validateDeposit(deposit, accountType));
        addIfNotNull(errors, validateSecondNin(secondNin, accountType));
        return errors;
    }

    private static void addIfNotNull(List<String> list, String msg) {
        if (msg != null) list.add(msg);
    }
}
