package firstbank.ui;

import firstbank.db.DatabaseManager;
import firstbank.model.*;
import firstbank.util.AccountNumberGenerator;
import firstbank.util.Validator;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.Year;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * First Bank Uganda – Account Opening Application.
 * JavaFX desktop form with full validation, OOP design, and database persistence.
 */
public class BankFormApp extends Application {

    // ── Form fields ───────────────────────────────────────────────────────────
    private TextField tfFirstName, tfLastName, tfNin, tfEmail, tfConfirmEmail;
    private TextField tfPhone, tfPin, tfConfirmPin, tfDeposit, tfSecondNin;
    private ComboBox<Integer> cbYear, cbMonth, cbDay;
    private ComboBox<String>  cbAccountType, cbBranch;
    private TextArea  taSummary;
    private Button    btnSubmit, btnReset;

    // Error label map: fieldName → Label
    private final Map<String, Label> errorLabels = new HashMap<>();

    // ── Colour constants ──────────────────────────────────────────────────────
    private static final String BANK_BLUE  = "#1A3A5C";
    private static final String BANK_GOLD  = "#C8A951";
    private static final String BG_LIGHT   = "#F4F6FA";
    private static final String ERROR_RED  = "#D32F2F";
    private static final String SUCCESS    = "#2E7D32";

    @Override
    public void start(Stage stage) {
        stage.setTitle("First Bank Uganda – New Account Opening Form");

        VBox root = new VBox();
        root.setStyle("-fx-background-color: " + BG_LIGHT + ";");

        // Header banner
        root.getChildren().add(buildHeader());

        // Scrollable form body
        ScrollPane scroll = new ScrollPane(buildForm());
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        VBox.setVgrow(scroll, Priority.ALWAYS);
        root.getChildren().add(scroll);

        Scene scene = new Scene(root, 860, 780);
        stage.setScene(scene);
        stage.setMinWidth(720);
        stage.setMinHeight(620);
        stage.show();
    }

    // ── Header ────────────────────────────────────────────────────────────────
    private HBox buildHeader() {
        HBox header = new HBox();
        header.setStyle("-fx-background-color: " + BANK_BLUE + "; -fx-padding: 18 30;");
        header.setAlignment(Pos.CENTER_LEFT);
        header.setSpacing(16);

        // Bank logo placeholder (coloured box)
        Label logo = new Label("FB");
        logo.setStyle("-fx-background-color: " + BANK_GOLD + "; -fx-text-fill: white; " +
                      "-fx-font-size: 22; -fx-font-weight: bold; -fx-padding: 8 12; " +
                      "-fx-border-radius: 4; -fx-background-radius: 4;");

        VBox titleBox = new VBox(2);
        Label bankName = new Label("First Bank Uganda");
        bankName.setStyle("-fx-text-fill: white; -fx-font-size: 20; -fx-font-weight: bold;");
        Label subTitle = new Label("New Bank Account Opening Application");
        subTitle.setStyle("-fx-text-fill: " + BANK_GOLD + "; -fx-font-size: 12;");
        titleBox.getChildren().addAll(bankName, subTitle);

        header.getChildren().addAll(logo, titleBox);
        return header;
    }

    // ── Form body ─────────────────────────────────────────────────────────────
    private VBox buildForm() {
        VBox form = new VBox(14);
        form.setPadding(new Insets(20, 30, 20, 30));

        form.getChildren().addAll(
            sectionTitle("Personal Information"),
            buildPersonalSection(),
            sectionTitle("Contact Details"),
            buildContactSection(),
            sectionTitle("Security"),
            buildSecuritySection(),
            sectionTitle("Account Details"),
            buildAccountSection(),
            buildButtons(),
            sectionTitle("Account Summary"),
            buildSummaryArea()
        );
        return form;
    }

    // ── Personal section ──────────────────────────────────────────────────────
    private GridPane buildPersonalSection() {
        GridPane g = grid();

        tfFirstName = field("e.g. Alice");
        tfLastName  = field("e.g. Nakato");
        tfNin       = field("14 uppercase alphanumeric");
        tfNin.textProperty().addListener((o, old, nv) ->
            tfNin.setText(nv.toUpperCase()));

        // Date of Birth
        cbYear  = new ComboBox<>();
        cbMonth = new ComboBox<>();
        cbDay   = new ComboBox<>();
        int curYear = Year.now().getValue();
        for (int y = curYear - 75; y <= curYear - 18; y++) cbYear.getItems().add(y);
        for (int m = 1; m <= 12; m++) cbMonth.getItems().add(m);
        cbYear.setPromptText("Year");
        cbMonth.setPromptText("Month");
        cbDay.setPromptText("Day");
        cbYear.setPrefWidth(100);
        cbMonth.setPrefWidth(90);
        cbDay.setPrefWidth(80);

        // Auto-update days when year or month changes
        cbYear.setOnAction(e  -> updateDays());
        cbMonth.setOnAction(e -> updateDays());

        HBox dobBox = new HBox(8, cbYear, cbMonth, cbDay);

        addRow(g, 0, "First Name *",   tfFirstName, "firstName");
        addRow(g, 1, "Last Name *",    tfLastName,  "lastName");
        addRow(g, 2, "National ID *",  tfNin,       "nin");
        addRow(g, 3, "Date of Birth *", dobBox,     "dob");

        return g;
    }

    private void updateDays() {
        Integer year  = cbYear.getValue();
        Integer month = cbMonth.getValue();
        if (year == null || month == null) return;
        int selected = cbDay.getValue() != null ? cbDay.getValue() : 0;
        int maxDay = LocalDate.of(year, month, 1).lengthOfMonth();
        cbDay.getItems().clear();
        for (int d = 1; d <= maxDay; d++) cbDay.getItems().add(d);
        if (selected >= 1 && selected <= maxDay) cbDay.setValue(selected);
    }

    // ── Contact section ───────────────────────────────────────────────────────
    private GridPane buildContactSection() {
        GridPane g = grid();
        tfEmail        = field("e.g. alice@example.com");
        tfConfirmEmail = field("Re-enter email");
        tfPhone        = field("+256XXXXXXXXX");
        addRow(g, 0, "Email *",         tfEmail,        "email");
        addRow(g, 1, "Confirm Email *", tfConfirmEmail, "confirmEmail");
        addRow(g, 2, "Phone Number *",  tfPhone,        "phone");
        return g;
    }

    // ── Security section ──────────────────────────────────────────────────────
    private GridPane buildSecuritySection() {
        GridPane g = grid();
        tfPin        = new PasswordField(); tfPin.setPromptText("4–6 digits");
        tfConfirmPin = new PasswordField(); tfConfirmPin.setPromptText("Re-enter PIN");
        styleField(tfPin);
        styleField(tfConfirmPin);
        addRow(g, 0, "PIN *",         tfPin,        "pin");
        addRow(g, 1, "Confirm PIN *", tfConfirmPin, "confirmPin");
        return g;
    }

    // ── Account details section ───────────────────────────────────────────────
    private GridPane buildAccountSection() {
        GridPane g = grid();

        cbAccountType = new ComboBox<>();
        cbAccountType.getItems().addAll("Savings","Current","Fixed Deposit","Student","Joint");
        cbAccountType.setPromptText("Select account type");
        cbAccountType.setPrefWidth(240);
        styleCombo(cbAccountType);

        cbBranch = new ComboBox<>();
        cbBranch.getItems().addAll("Kampala","Gulu","Mbarara","Jinja","Mbale");
        cbBranch.setPromptText("Select branch");
        cbBranch.setPrefWidth(240);
        styleCombo(cbBranch);

        tfDeposit   = field("Minimum depends on account type");
        tfSecondNin = field("Required for Joint accounts only");

        // Show/hide second NIN and update deposit hint based on account type
        Label depositHint = new Label();
        depositHint.setStyle("-fx-text-fill: #555; -fx-font-size: 11;");
        cbAccountType.setOnAction(e -> {
            String type = cbAccountType.getValue();
            if (type != null) {
                double min = firstbank.model.AccountFactory.minimumFor(type);
                depositHint.setText(String.format("Minimum: UGX %,.0f", min));
                tfSecondNin.setDisable(!"Joint".equals(type));
                if (!"Joint".equals(type)) tfSecondNin.clear();
            }
        });
        tfSecondNin.setDisable(true);

        VBox depositBox = new VBox(2, tfDeposit, depositHint);

        addRow(g, 0, "Account Type *",    cbAccountType, "accountType");
        addRow(g, 1, "Branch *",          cbBranch,      "branch");
        addRow(g, 2, "Opening Deposit *", depositBox,    "deposit");
        addRow(g, 3, "Second NIN",        tfSecondNin,   "secondNin");

        return g;
    }

    // ── Buttons ───────────────────────────────────────────────────────────────
    private HBox buildButtons() {
        btnSubmit = new Button("Submit Application");
        btnSubmit.setStyle("-fx-background-color: " + BANK_BLUE + "; -fx-text-fill: white; " +
                           "-fx-font-size: 13; -fx-font-weight: bold; -fx-padding: 10 28; " +
                           "-fx-background-radius: 4; -fx-cursor: hand;");
        btnSubmit.setOnAction(e -> handleSubmit());

        btnReset = new Button("Reset Form");
        btnReset.setStyle("-fx-background-color: #B0BEC5; -fx-text-fill: #1A3A5C; " +
                          "-fx-font-size: 13; -fx-font-weight: bold; -fx-padding: 10 28; " +
                          "-fx-background-radius: 4; -fx-cursor: hand;");
        btnReset.setOnAction(e -> handleReset());

        HBox box = new HBox(14, btnSubmit, btnReset);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setPadding(new Insets(10, 0, 0, 0));
        return box;
    }

    // ── Summary area ──────────────────────────────────────────────────────────
    private VBox buildSummaryArea() {
        Label lbl = new Label("Account Summary is Below:");
        lbl.setStyle("-fx-font-weight: bold; -fx-text-fill: " + BANK_BLUE + ";");
        taSummary = new TextArea();
        taSummary.setEditable(false);
        taSummary.setPrefRowCount(6);
        taSummary.setStyle("-fx-font-family: Monospace; -fx-font-size: 12; " +
                           "-fx-background-color: #E8F5E9; -fx-border-color: #A5D6A7;");
        taSummary.setWrapText(true);
        return new VBox(4, lbl, taSummary);
    }

    // ── Submit logic ──────────────────────────────────────────────────────────
    private void handleSubmit() {
        clearErrors();

        String firstName    = tfFirstName.getText();
        String lastName     = tfLastName.getText();
        String nin          = tfNin.getText();
        String email        = tfEmail.getText();
        String confirmEmail = tfConfirmEmail.getText();
        String phone        = tfPhone.getText();
        String pin          = tfPin.getText();
        String confirmPin   = tfConfirmPin.getText();
        String accountType  = cbAccountType.getValue();
        String branch       = cbBranch.getValue();
        String deposit      = tfDeposit.getText();
        String secondNin    = tfSecondNin.getText();

        Integer year  = cbYear.getValue();
        Integer month = cbMonth.getValue();
        Integer day   = cbDay.getValue();
        int dobYear  = year  != null ? year  : 0;
        int dobMonth = month != null ? month : 0;
        int dobDay   = day   != null ? day   : 0;

        List<String> errors = Validator.validateAll(
                firstName, lastName, nin, email, confirmEmail,
                phone, pin, confirmPin,
                dobYear, dobMonth, dobDay,
                accountType, branch, deposit, secondNin);

        if (!errors.isEmpty()) {
            showInlineErrors(errors);
            showErrorDialog(errors);
            return;
        }

        // Build account via polymorphic factory
        String dob     = String.format("%04d-%02d-%02d", dobYear, dobMonth, dobDay);
        double amount  = Double.parseDouble(deposit.trim().replace(",", ""));
        String accNo   = AccountNumberGenerator.generate(branch);

        Account account = AccountFactory.create(
                accountType, accNo,
                firstName.trim(), lastName.trim(),
                nin.trim(), email.trim().toLowerCase(),
                phone.trim(), pin.trim(),
                dob, branch, amount,
                secondNin != null ? secondNin.trim() : "");

        // Polymorphic deposit validation (double-check via subclass)
        if (amount < account.minimumDeposit()) {
            showError("deposit", String.format("Minimum deposit for %s is UGX %,.0f",
                    accountType, account.minimumDeposit()));
            return;
        }

        // Persist to database
        try {
            DatabaseManager.getInstance().saveAccount(account);
        } catch (Exception ex) {
            showWarning("Database Warning", "Account created but could not be saved:\n" + ex.getMessage());
        }

        // Display summary
        String summary = account.toString() + "\n\n"
                + "Account Type  : " + account.accountType() + "\n"
                + "Special Rule  : " + account.specialRule() + "\n"
                + "Min. Deposit  : UGX " + String.format("%,.0f", account.minimumDeposit()) + "\n"
                + "Deposit Made  : UGX " + String.format("%,.0f", amount) + "\n"
                + "Branch        : " + branch + "\n"
                + "Generated No. : " + accNo;

        taSummary.setText(summary);
        taSummary.setStyle("-fx-font-family: Monospace; -fx-font-size: 12; " +
                           "-fx-background-color: #E8F5E9; -fx-border-color: #4CAF50;");

        showSuccess("Account Created!", "Account " + accNo + " created successfully.\n" +
                "Welcome to First Bank Uganda, " + firstName.trim() + "!");
    }

    // ── Reset logic ───────────────────────────────────────────────────────────
    private void handleReset() {
        tfFirstName.clear(); tfLastName.clear(); tfNin.clear();
        tfEmail.clear(); tfConfirmEmail.clear();
        tfPhone.clear(); tfPin.clear(); tfConfirmPin.clear();
        tfDeposit.clear(); tfSecondNin.clear();
        cbYear.setValue(null); cbMonth.setValue(null); cbDay.setValue(null);
        cbAccountType.setValue(null); cbBranch.setValue(null);
        taSummary.clear();
        taSummary.setStyle("-fx-font-family: Monospace; -fx-font-size: 12; " +
                           "-fx-background-color: #E8F5E9; -fx-border-color: #A5D6A7;");
        tfSecondNin.setDisable(true);
        clearErrors();
    }

    // ── Error display ─────────────────────────────────────────────────────────
    private void clearErrors() {
        errorLabels.values().forEach(l -> l.setText(""));
    }

    private void showError(String field, String msg) {
        Label lbl = errorLabels.get(field);
        if (lbl != null) lbl.setText("⚠ " + msg);
    }

    private void showInlineErrors(List<String> errors) {
        // Map error keywords to field names
        for (String err : errors) {
            String low = err.toLowerCase();
            if (low.contains("first name"))    showError("firstName",    err);
            else if (low.contains("last name")) showError("lastName",    err);
            else if (low.contains("nin") && !low.contains("second")) showError("nin", err);
            else if (low.contains("email") && low.contains("confirm")) showError("confirmEmail", err);
            else if (low.contains("email"))    showError("email",       err);
            else if (low.contains("phone"))    showError("phone",       err);
            else if (low.contains("pin") && low.contains("confirm")) showError("confirmPin", err);
            else if (low.contains("pin"))      showError("pin",         err);
            else if (low.contains("date") || low.contains("age") || low.contains("dob")) showError("dob", err);
            else if (low.contains("account type")) showError("accountType", err);
            else if (low.contains("branch"))   showError("branch",      err);
            else if (low.contains("deposit"))  showError("deposit",     err);
            else if (low.contains("second"))   showError("secondNin",   err);
        }
    }

    private void showErrorDialog(List<String> errors) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Validation Errors");
        alert.setHeaderText("Please fix the following issues:");
        StringBuilder sb = new StringBuilder();
        errors.forEach(e -> sb.append("• ").append(e).append("\n"));
        alert.setContentText(sb.toString());
        alert.showAndWait();
    }

    private void showSuccess(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void showWarning(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    // ── UI helper builders ────────────────────────────────────────────────────
    private Label sectionTitle(String text) {
        Label lbl = new Label(text);
        lbl.setStyle("-fx-font-size: 14; -fx-font-weight: bold; -fx-text-fill: " + BANK_BLUE +
                     "; -fx-border-color: " + BANK_GOLD + "; -fx-border-width: 0 0 2 0; " +
                     "-fx-padding: 6 0 4 0;");
        lbl.setMaxWidth(Double.MAX_VALUE);
        return lbl;
    }

    private GridPane grid() {
        GridPane g = new GridPane();
        g.setHgap(12); g.setVgap(6);
        g.setPadding(new Insets(4, 0, 4, 0));
        ColumnConstraints c1 = new ColumnConstraints(160);
        ColumnConstraints c2 = new ColumnConstraints(300);
        ColumnConstraints c3 = new ColumnConstraints();
        c3.setHgrow(Priority.ALWAYS);
        g.getColumnConstraints().addAll(c1, c2, c3);
        return g;
    }

    private void addRow(GridPane g, int row, String labelText, javafx.scene.Node control, String fieldKey) {
        Label lbl = new Label(labelText);
        lbl.setStyle("-fx-font-size: 12; -fx-text-fill: #333;");
        lbl.setAlignment(Pos.CENTER_RIGHT);
        lbl.setMaxWidth(Double.MAX_VALUE);

        Label err = new Label();
        err.setStyle("-fx-text-fill: " + ERROR_RED + "; -fx-font-size: 10;");
        err.setWrapText(true);
        errorLabels.put(fieldKey, err);

        VBox controlBox = new VBox(2, control, err);

        g.add(lbl, 0, row);
        g.add(controlBox, 1, row);
    }

    private TextField field(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        styleField(tf);
        return tf;
    }

    private void styleField(Control c) {
        c.setStyle("-fx-background-color: white; -fx-border-color: #BDBDBD; " +
                   "-fx-border-radius: 3; -fx-background-radius: 3; -fx-padding: 5 8;");
        c.setPrefWidth(280);
    }

    private void styleCombo(ComboBox<?> cb) {
        cb.setStyle("-fx-background-color: white; -fx-border-color: #BDBDBD; " +
                    "-fx-border-radius: 3; -fx-background-radius: 3;");
    }

    // ── Entry point ───────────────────────────────────────────────────────────
    public static void main(String[] args) {
        launch(args);
    }
}
