module firstbank {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    
    requires ucanaccess; 

    opens ui to javafx.fxml;
    exports ui;
    exports model;
    exports database;
    exports util;
}
