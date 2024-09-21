module com.example.flappybirdfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;
    requires org.xerial.sqlitejdbc;


    opens com.example.flappybirdfx to javafx.fxml;
    exports com.example.flappybirdfx;
}