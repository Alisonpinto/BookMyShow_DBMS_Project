module app.moviesystem {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires java.sql;
    requires java.desktop;

    opens app.moviesystem to javafx.fxml;
    exports app.moviesystem;
}