module com.example.proiectjavafx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens com.example.proiectjavafx to javafx.fxml;
    exports com.example.proiectjavafx;
}