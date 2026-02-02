module by.bal.jcube {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;


    opens by.bal.jcube to javafx.fxml;
    exports by.bal.jcube;
    exports by.bal.jcube.geometry;
    opens by.bal.jcube.geometry to javafx.fxml;
    exports by.bal.jcube.geometry.vector;
    opens by.bal.jcube.geometry.vector to javafx.fxml;
    exports by.bal.jcube.obj;
    opens by.bal.jcube.obj to javafx.fxml;
}
