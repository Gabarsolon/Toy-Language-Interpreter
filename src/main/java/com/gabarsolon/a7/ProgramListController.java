package com.gabarsolon.a7;

import Model.Statements.IStmt;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.util.List;


public class ProgramListController {
    @FXML
    private ListView<IStmt> programListView;
    private Stage mainStage;

    public ListView<IStmt> getProgramListView() {
        return programListView;
    }

    public void setProgramListView(List<IStmt> programList) {
        ObservableList<IStmt> programs = FXCollections.observableArrayList(programList);
        programListView.setItems(programs);
    }

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    @FXML
    public void initialize(){

    }

}
