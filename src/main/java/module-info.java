module com.gabarsolon.a7 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.gabarsolon.a7 to javafx.fxml;
    exports com.gabarsolon.a7;

}