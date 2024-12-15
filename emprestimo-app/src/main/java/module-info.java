module emp {
    requires transitive javafx.graphics;
    
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fontawesome5;

    exports emp;

    opens emp to javafx.fxml;
    opens emp.controllers to javafx.fxml;
    opens emp.models to javafx.base;
    //opens emp.utils to javafx.base;
}