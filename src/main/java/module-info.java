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
    requires javafx.swing;
    requires javafx.media;
    requires org.apache.commons.lang3;

    opens com.mirohaap.towerofhanoitutor to javafx.fxml;
    exports com.mirohaap.towerofhanoitutor;
}