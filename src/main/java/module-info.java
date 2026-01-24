module by.bal.jcube {
    requires javafx.controls;
    requires javafx.fxml;


    opens by.bal.jcube to javafx.fxml;
    exports by.bal.jcube;
}
