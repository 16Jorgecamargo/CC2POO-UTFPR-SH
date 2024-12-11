module emp {
    requires transitive javafx.graphics;
    

    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;

    exports emp;
    exports emp.controllers;
    exports emp.models;
    exports emp.utils;

    opens emp to javafx.fxml;
    opens emp.controllers to javafx.fxml;
    opens emp.models to javafx.base;
    opens emp.utils to javafx.base;
}