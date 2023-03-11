package com.gabarsolon.a7;

import Model.Statements.IStmt;
import Controller.IController;
import Model.States.PrgState;
import Model.Values.Value;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;


import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class InterpreterController {
    @FXML
    private TableColumn<Map.Entry<Integer, Value>, String> addressColumn;
    @FXML
    private TableColumn<Map.Entry<Integer, Value>, String> valueColumnHeapTable;

    @FXML
    private TableColumn<Map.Entry<String, Value>, String> variableNameColumn;
    @FXML
    private TableColumn<Map.Entry<String, Value>, String> valueColumnSymTable;

    @FXML
    private ListView<String> exeStackListView;

    @FXML
    private ListView<String> fileTableListView;

    @FXML
    private TableView<Map.Entry<Integer, Value>> heapTableView;

    @FXML
    private TextField noOfPrgStatesTextField;

    @FXML
    private ListView<String> outListView;

    @FXML
    private ListView<Integer> prgStatesListView;

    @FXML
    private Button runOneStepButton;

    @FXML
    private TableView<Map.Entry<String,Value>> symTableView;

    private Integer oldPrgListSize;
    private Stage programListWindow;
    private BooleanProperty changedPrgState;
    private IController currentPrgController;
    private List<IStmt> stmtList;
    private List<IController> prgControllers;
    private List<PrgState> prgList;
    private Stage mainStage;
    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }
    public void createProgramListWindow(){
        try {
            programListWindow = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ProgramList.fxml"));
            StackPane root = loader.load();

            ProgramListController plc = loader.getController();
            plc.setProgramListView(stmtList);
            plc.getProgramListView().getSelectionModel().
                    selectedItemProperty().addListener(new ChangeListener<IStmt>() {
                        @Override
                        public void changed(ObservableValue<? extends IStmt> observable, IStmt oldValue, IStmt newValue) {
                            currentPrgController = prgControllers.get(plc.getProgramListView().getSelectionModel().getSelectedIndex());
                            prgList = currentPrgController.getPrgList();
                            if(prgList.get(0)==null){
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("Error");
                                alert.setHeaderText(null);
                                alert.setContentText("The selected program didn't pass the typechecker");
                                alert.showAndWait();
                            }
                            else{
                                currentPrgController.prepareExecution();
                                oldPrgListSize = 1;
                                updateProgramListView();
                                prgStatesListView.getSelectionModel().select(0);
                                programListWindow.hide();
                                mainStage.show();
                            }
                        }
                    });

            programListWindow.initModality(Modality.WINDOW_MODAL);
            programListWindow.initOwner(mainStage);

            Scene scene = new Scene(root, 600, 400);
            programListWindow.setTitle("Program List");
            programListWindow.setScene(scene);
            programListWindow.show();
        }catch (Exception e){
            System.out.println(e);
        }
    }
    private void updateProgramsStatus(){
        heapTableView.getItems().clear();
        outListView.getItems().clear();
        fileTableListView.getItems().clear();

        heapTableView.getItems().addAll(prgList.get(0).getHeapTable().getData().entrySet());
        prgList.get(0).getOut().getData().stream().forEach(e->outListView.getItems().add(e.toString()));
        prgList.get(0).getFileTable().getData().entrySet().stream().forEach(e->fileTableListView.getItems().add(e.getKey()));

        changedPrgState.set(!changedPrgState.get());
    }
    private void updateProgramListView(){
        Integer lastSelectedIndex = prgStatesListView.getSelectionModel().getSelectedIndex();
        prgStatesListView.getItems().clear();
        noOfPrgStatesTextField.setText(Integer.toString(prgList.size()));
        prgList.stream().forEach(prg->prgStatesListView.getItems().add(prg.getPrgId()));
        if(lastSelectedIndex!=-1)
            prgStatesListView.getSelectionModel().select(lastSelectedIndex);
        oldPrgListSize=prgList.size();
    }
    @FXML
    void runOneStep(ActionEvent event) {
        try{
            prgList = currentPrgController.removeCompletedPrg(prgList);
            if(this.oldPrgListSize != prgList.size()) {
                if (prgList.size() < 1) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Finished");
                    alert.setHeaderText(null);
                    alert.setContentText("All of the programs finished the execution");
                    alert.showAndWait();

                    currentPrgController.endExecution();

                    prgStatesListView.getItems().clear();
                    heapTableView.getItems().clear();
                    outListView.getItems().clear();
                    fileTableListView.getItems().clear();
                    mainStage.hide();

                    programListWindow.show();
                    return;
                } else {
                    updateProgramListView();
                }
            }
            currentPrgController.oneStepForAllPrg(prgList);
	    if(oldPrgListSize!=prgList.size())
                updateProgramListView();
            updateProgramsStatus();
        }catch (Exception e){
            System.out.println(e);
        }
    }
    public void setStmtList(List<IStmt> stmtList) {
        this.stmtList = stmtList;
    }

    public void setPrgControllers(List<IController> prgControllers) {
        this.prgControllers = prgControllers;
    }

    @FXML
    public void initialize(){
        heapTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        symTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        variableNameColumn.setCellValueFactory(param->new ReadOnlyStringWrapper(param.getValue().getKey()));
        valueColumnSymTable.setCellValueFactory(param->new ReadOnlyStringWrapper(param.getValue().getValue().toString()));

        addressColumn.setCellValueFactory(param-> new ReadOnlyStringWrapper(param.getValue().getKey().toString()));
        valueColumnHeapTable.setCellValueFactory(param->new ReadOnlyStringWrapper(param.getValue().getValue().toString()));

        changedPrgState= new SimpleBooleanProperty(true);
        changedPrgState.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                symTableView.getItems().clear();
                exeStackListView.getItems().clear();
                Integer prgIndex = prgStatesListView.getSelectionModel().getSelectedIndex();
                if(prgIndex >= 0 &&  prgIndex < prgList.size()){
                    PrgState prg = prgList.get(prgIndex);

                    symTableView.getItems().addAll(prg.getSymTable().getData().entrySet());

                    StringTokenizer st = new StringTokenizer(prg.getExeStack().toString(),";");
                    while(st.hasMoreTokens()){
                        exeStackListView.getItems().add(st.nextToken());
                    }
                }
            }
        });
        prgStatesListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                if(newValue != null)
                    changedPrgState.set(!changedPrgState.get());
            }
        });
    }
}