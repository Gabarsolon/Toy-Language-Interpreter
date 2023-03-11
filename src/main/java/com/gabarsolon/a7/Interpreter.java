package com.gabarsolon.a7;

import Controller.Controller;
import Controller.IController;
import Model.Expressions.*;
import Model.Statements.*;
import Model.States.*;
import Model.Types.BoolType;
import Model.Types.IntType;
import Model.Types.RefType;
import Model.Types.StringType;
import Model.Values.BoolValue;
import Model.Values.IntValue;
import Model.Values.StringValue;
import Model.Values.Value;
import Repository.IRepository;
import Repository.Repository;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Interpreter extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        try {
            String logFilePath = "log.txt";
            PrgState.setAvailableId(0);

            IStmt ex1 = new CompStmt(new VarDeclStmt("v", new IntType()),
                    new CompStmt(new AssignStmt("v", new ValueExp(new IntValue(2))),
                            new PrintStmt(new VarExp("v"))));

            IStmt ex2 = new CompStmt(new VarDeclStmt("a", new IntType()),
                    new CompStmt(new VarDeclStmt("b", new IntType()),
                            new CompStmt(new AssignStmt("a", new ArithExp(1, new ValueExp(new IntValue(2)),
                                    new ArithExp(3, new ValueExp(new IntValue(3)),
                                            new ValueExp(new IntValue(5))))),
                                    new CompStmt(new AssignStmt("b", new ArithExp(1, new VarExp("a"),
                                            new ValueExp(new IntValue(1)))), new PrintStmt(new VarExp("b"))))));

            IStmt ex3 = new CompStmt(new VarDeclStmt("a", new BoolType()),
                    new CompStmt(new VarDeclStmt("v", new IntType()),
                            new CompStmt(new AssignStmt("a", new ValueExp(new BoolValue(true))),
                                    new CompStmt(new IfStmt(new VarExp("a"), new AssignStmt("v",
                                            new ValueExp(new IntValue(2))), new AssignStmt("v", new ValueExp(
                                            new IntValue(3)))), new PrintStmt(new VarExp("v"))))));

            IStmt fileOperationsEx = new CompStmt(new VarDeclStmt("varf", new StringType()),
                    new CompStmt(new AssignStmt("varf", new ValueExp(new StringValue("test.in"))),
                            new CompStmt(new openRFile(new VarExp("varf")),
                                    new CompStmt(new VarDeclStmt("varc", new IntType()),
                                            new CompStmt(new readFile(new VarExp("varf"), "varc"),
                                                    new CompStmt(new PrintStmt(new VarExp("varc")),
                                                            new CompStmt(new readFile(new VarExp("varf"), "varc"),
                                                                    new CompStmt(new PrintStmt(new VarExp("varc")),
                                                                            new closeRFile(new VarExp("varf"))))))))));

            IStmt heapAllocationEx = new CompStmt(new VarDeclStmt("v", new RefType(new IntType())),
                    new CompStmt(new allocHeap("v", new ValueExp(new IntValue(20))),
                            new CompStmt(new VarDeclStmt("a", new RefType(new RefType(new IntType()))),
                                    new CompStmt(new allocHeap("a", new VarExp("v")),
                                            new CompStmt(new PrintStmt(new VarExp("v")),
                                                    new PrintStmt(new VarExp("a")))))));

            /*
            Example:
            Ref int v;new(v,20);Ref Ref int a; new(a,v);print(rH(v));print(rH(rH(a))+5)
            At the end of execution: Heap={1->20, 2->(1,int)}, SymTable={v->(1,int), a->(2,Ref int)} and
            Out={20, 25}
            */

            IStmt heapReadingEx = new CompStmt(new VarDeclStmt("v", new RefType(new IntType())),
                    new CompStmt(new allocHeap("v", new ValueExp(new IntValue(20))),
                            new CompStmt(new VarDeclStmt("a", new RefType(new RefType(new IntType()))),
                                    new CompStmt(new allocHeap("a", new VarExp("v")),
                                            new CompStmt(new PrintStmt(new readHeap(new VarExp("v"))),
                                                    new PrintStmt(new ArithExp(1, new readHeap(new readHeap(new VarExp("a"))), new ValueExp(new IntValue(5)))))))));

            /*
            Example: Ref int v;new(v,20);print(rH(v)); wH(v,30);print(rH(v)+5);
            At the end of execution: Heap={1->30}, SymTable={v->(1,int)} and Out={20, 35}
             */
            IStmt heapWritingEx = new CompStmt(new VarDeclStmt("v", new RefType(new IntType())),
                    new CompStmt(new allocHeap("v", new ValueExp(new IntValue(20))),
                            new CompStmt(new PrintStmt(new readHeap(new VarExp("v"))),
                                    new CompStmt(new writeHeap("v", new ValueExp(new IntValue(30))),
                                            new PrintStmt(new ArithExp(1, new readHeap(new VarExp("v")), new ValueExp(new IntValue(5))))))));

            //Ref int v;new(v,20);Ref Ref int a; new(a,v); new(v,30);print(rH(rH(a)))

            IStmt garbageCollectorEx = new CompStmt(new VarDeclStmt("v", new RefType(new IntType())),
                    new CompStmt(new allocHeap("v", new ValueExp(new IntValue(20))),
                            new CompStmt(new VarDeclStmt("a", new RefType(new RefType(new IntType()))),
                                    new CompStmt(new allocHeap("a", new VarExp("v")),
                                            new CompStmt(new allocHeap("v", new ValueExp(new IntValue(30))),
                                                    new PrintStmt(new readHeap(new readHeap(new VarExp("a")))))))));

            IStmt whileStmtEx = new CompStmt(new VarDeclStmt("v", new IntType()),
                    new CompStmt(new AssignStmt("v", new ValueExp(new IntValue(4))),
                            new CompStmt(new WhileStmt(new RelExp(">", new VarExp("v"), new ValueExp(new IntValue(0))),
                                    new CompStmt(new PrintStmt(new VarExp("v")), new AssignStmt("v",
                                            new ArithExp(2, new VarExp("v"), new ValueExp(new IntValue(1)))))),
                                    new PrintStmt(new VarExp("v")))));

            IStmt concurrentEx = new CompStmt(new VarDeclStmt("v", new IntType()),
                    new CompStmt(new VarDeclStmt("a", new RefType(new IntType())),
                            new CompStmt(new AssignStmt("v", new ValueExp(new IntValue(10))),
                                    new CompStmt(new allocHeap("a", new ValueExp(new IntValue(22))),
                                            new CompStmt(new forkStmt(new CompStmt(new writeHeap("a", new ValueExp(new IntValue(30))),
                                                    new CompStmt(new AssignStmt("v", new ValueExp(new IntValue(32))),
                                                            new CompStmt(new PrintStmt(new VarExp("v")),
                                                                    new PrintStmt(new readHeap(new VarExp("a"))))))),
                                                    new CompStmt(new PrintStmt(new VarExp("v")),
                                                            new PrintStmt(new readHeap(new VarExp("a")))))))));



            PrgState prg1, prg2, prg3, prg4, prg5, prg6, prg7, prg8, prg9, prg10;
            try {
                ex1.typecheck(new MyDictionary<>());
                prg1 = new PrgState(new MyStack<IStmt>(), new MyDictionary<String, Value>(),
                        new MyList<Value>(), new MyDictionary<String, BufferedReader>(), new MyHeap<Integer, Value>(), ex1);
            } catch (Exception e) {
                System.out.println("Ex1: " + e);
                prg1 = null;
            }
            try {
                ex2.typecheck(new MyDictionary<>());
                prg2 = new PrgState(new MyStack<IStmt>(), new MyDictionary<String, Value>(),
                        new MyList<Value>(), new MyDictionary<String, BufferedReader>(), new MyHeap<Integer, Value>(), ex2);
            } catch (Exception e) {
                System.out.println("Ex2: " + e);
                prg2 = null;
            }

            try {
                ex3.typecheck(new MyDictionary<>());
                prg3 = new PrgState(new MyStack<IStmt>(), new MyDictionary<String, Value>(),
                        new MyList<Value>(), new MyDictionary<String, BufferedReader>(), new MyHeap<Integer, Value>(), ex3);
            } catch (Exception e) {
                System.out.println("Ex3: " + e);
                prg3 = null;
            }

            try {
                fileOperationsEx.typecheck(new MyDictionary<>());
                prg4 = new PrgState(new MyStack<IStmt>(), new MyDictionary<String, Value>(),
                        new MyList<Value>(), new MyDictionary<String, BufferedReader>(), new MyHeap<Integer, Value>(), fileOperationsEx);
            } catch (Exception e) {
                System.out.println("fileOperationsEx: " + e);
                prg4 = null;
            }

            try {
                heapAllocationEx.typecheck(new MyDictionary<>());
                prg5 = new PrgState(new MyStack<IStmt>(), new MyDictionary<String, Value>(),
                        new MyList<Value>(), new MyDictionary<String, BufferedReader>(), new MyHeap<Integer, Value>(), heapAllocationEx);
            } catch (Exception e) {
                System.out.println("heapAllocationEx: " + e);
                prg5 = null;
            }

            try {
                heapReadingEx.typecheck(new MyDictionary<>());
                prg6 = new PrgState(new MyStack<IStmt>(), new MyDictionary<String, Value>(),
                        new MyList<Value>(), new MyDictionary<String, BufferedReader>(), new MyHeap<Integer, Value>(), heapReadingEx);
            } catch (Exception e) {
                System.out.println("heapReadingEx: " + e);
                prg6 = null;
            }

            try {
                heapWritingEx.typecheck(new MyDictionary<>());
                prg7 = new PrgState(new MyStack<IStmt>(), new MyDictionary<String, Value>(),
                        new MyList<Value>(), new MyDictionary<String, BufferedReader>(), new MyHeap<Integer, Value>(), heapWritingEx);
            } catch (Exception e) {
                System.out.println("heapWritingEx: " + e);
                prg7 = null;
            }

            try {
                garbageCollectorEx.typecheck(new MyDictionary<>());
                prg8 = new PrgState(new MyStack<IStmt>(), new MyDictionary<String, Value>(),
                        new MyList<Value>(), new MyDictionary<String, BufferedReader>(), new MyHeap<Integer, Value>(), garbageCollectorEx);
            } catch (Exception e) {
                System.out.println("garbageCollectorEx: " + e);
                prg8 = null;
            }

            try {
                whileStmtEx.typecheck(new MyDictionary<>());
                prg9 = new PrgState(new MyStack<IStmt>(), new MyDictionary<String, Value>(),
                        new MyList<Value>(), new MyDictionary<String, BufferedReader>(), new MyHeap<Integer, Value>(), whileStmtEx);
            } catch (Exception e) {
                System.out.println("whileStmtEx: " + e);
                prg9 = null;
            }

            try {
                concurrentEx.typecheck(new MyDictionary<>());
                prg10 = new PrgState(new MyStack<IStmt>(), new MyDictionary<String, Value>(),
                        new MyList<Value>(), new MyDictionary<String, BufferedReader>(), new MyHeap<Integer, Value>(), concurrentEx);
            } catch (Exception e) {
                System.out.println("concurrentEx: " + e);
                prg10 = null;
            }

            IRepository rp1 = new Repository(prg1, logFilePath);
            IRepository rp2 = new Repository(prg2, logFilePath);
            IRepository rp3 = new Repository(prg3, logFilePath);
            IRepository rp4 = new Repository(prg4, logFilePath);
            IRepository rp5 = new Repository(prg5, logFilePath);
            IRepository rp6 = new Repository(prg6, logFilePath);
            IRepository rp7 = new Repository(prg7, logFilePath);
            IRepository rp8 = new Repository(prg8, logFilePath);
            IRepository rp9 = new Repository(prg9, logFilePath);
            IRepository rp10 = new Repository(prg10, logFilePath);

            IController ctr1 = new Controller(rp1);
            IController ctr2 = new Controller(rp2);
            IController ctr3 = new Controller(rp3);
            IController ctr4 = new Controller(rp4);
            IController ctr5 = new Controller(rp5);
            IController ctr6 = new Controller(rp6);
            IController ctr7 = new Controller(rp7);
            IController ctr8 = new Controller(rp8);
            IController ctr9 = new Controller(rp9);
            IController ctr10 = new Controller(rp10);

            FXMLLoader fxmlLoader = new FXMLLoader(Interpreter.class.getResource("Interpreter.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);

            List<IStmt> prgList = Arrays.asList(ex1, ex2, ex3, fileOperationsEx, heapAllocationEx, heapReadingEx,
                    heapWritingEx, garbageCollectorEx, whileStmtEx, concurrentEx);
            List<IController> prgControllers = Arrays.asList(ctr1, ctr2, ctr3, ctr4, ctr5, ctr6, ctr7, ctr8, ctr9, ctr10);
            InterpreterController ic = fxmlLoader.getController();
            ic.setStmtList(prgList);
            ic.setPrgControllers(prgControllers);
            ic.setMainStage(stage);
            ic.createProgramListWindow();

            stage.setTitle("Interpreter");
            stage.setScene(scene);
            //stage.show();
        } catch (Exception e) {
            throw e;
        }
    }

    public static void main(String[] args) {
        launch();
    }
}