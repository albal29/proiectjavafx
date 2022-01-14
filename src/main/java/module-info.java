module com.example.proiectjavafx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires org.apache.pdfbox;

    opens com.example.proiectjavafx to javafx.fxml, javafx.base, javafx.graphics;
    exports com.example.proiectjavafx;
    opens domain;
}