module org.example.eurovision_manager {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires com.fasterxml.jackson.databind;

    requires com.fasterxml.jackson.dataformat.xml;

    requires java.sql;
    requires org.postgresql.jdbc;

    opens org.example.eurovision_manager to javafx.fxml;
    exports org.example.eurovision_manager;

    opens org.example.eurovision_manager.model.entity to javafx.base, com.fasterxml.jackson.databind;

    opens org.example.eurovision_manager.model.repository to java.sql;
    exports org.example.eurovision_manager.model.repository;

    opens org.example.eurovision_manager.controller to javafx.fxml;
    exports org.example.eurovision_manager.controller;

    exports org.example.eurovision_manager.model.service;

    opens org.example.eurovision_manager.connection to java.sql;
    exports org.example.eurovision_manager.connection;

}