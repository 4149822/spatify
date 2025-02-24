module simo.lopez.spatify {
    requires javafx.controls;
    requires javafx.fxml;
    requires log4j;
    requires jasperreports;
    requires org.xerial.sqlitejdbc;
    requires java.sql;
    requires java.desktop;

    opens simo.lopez.spatify to javafx.fxml;
    exports simo.lopez.spatify;
}