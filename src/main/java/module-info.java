module com.mirohaap.towerofhanoitutor {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.desktop;
    requires freetts;

    opens com.mirohaap.towerofhanoitutor to javafx.fxml;
    exports com.mirohaap.towerofhanoitutor;
}