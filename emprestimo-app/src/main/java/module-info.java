/**
 * Módulo principal da aplicação de empréstimos.
 * <p>
 * Requisitos:
 * - Pacotes JavaFX (graphics, base, controls, fxml) para interface e interação.
 * - org.kordamp.ikonli.javafx e org.kordamp.ikonli.fontawesome5 para ícones.
 * - java.sql e com.zaxxer.hikari para operações com o banco de dados MySQL.
 * </p>
 * <p>
 * Os pacotes exportados são:
 * - emp
 * - emp.controllers
 * - emp.models
 * </p>
 * <p>
 * Os pacotes abertos para acesso do JavaFX são:
 * - emp
 * - emp.controllers
 * - emp.models
 * </p>
 */
module emp {
    requires transitive javafx.graphics;
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fontawesome5;
    requires java.sql;
    requires com.zaxxer.hikari;

    exports emp;
    exports emp.controllers;
    exports emp.models;

    opens emp to javafx.fxml;
    opens emp.controllers to javafx.fxml;
    opens emp.models to javafx.base;
}