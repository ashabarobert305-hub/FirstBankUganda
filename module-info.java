module firstbank {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens firstbank.ui to javafx.fxml;
    exports firstbank.ui;
    exports firstbank.model;
    exports firstbank.db;
    exports firstbank.util;
}
