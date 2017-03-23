package com.company;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.List;

/**
 * Created by ToubeauShawnD on 3/16/2017.
 */
public class Main extends Application{

    private List<String> buttons = Arrays.asList("7","8","9","DEL","CLEAR","4","5","6","^","Sqrt","1","2","3","+","-",".","0","=","X","/");
    private TextField textField = new TextField();

    private Button btnCalc, btnConv;
    private Label lblCalc, lblConv;
    private BorderPane calcPane, convPane;
    private FlowPane calculator;
    private Scene calcScene, convScene;
    private Stage currentStage;


    @Override
    public void start(Stage primarystage) throws Exception {

        currentStage = primarystage;

        btnCalc = new Button("Converter");
        btnConv = new Button("Calculator");
        btnCalc.setOnAction(e-> ButtonClicked(e));
        btnConv.setOnAction(e-> ButtonClicked(e));

        lblCalc = new Label("This is the Calculator");
        lblConv = new Label("This is the Converter");

        calcPane = new BorderPane();
        convPane = new BorderPane();

        calcPane.setStyle("-fx-background-color: white; -fx-padding: 10px;");
        convPane.setStyle("-fx-background-color: white; -fx-padding: 10px;");

        calcPane.setTop(btnCalc);
        convPane.setTop(btnConv);
        calculator = new FlowPane();

        calculator.setAlignment(Pos.CENTER);
        calculator.setPadding(new Insets(30,20,30,20));
        calculator.setHgap(5);
        calculator.setVgap(5);

        textField.setEditable(false);
        textField.setAlignment(Pos.CENTER);
        textField.setMinSize(420, 40);

        calculator.getChildren().addAll(textField);



        for (String button: buttons) {
            Button b = new Button(button);
            b.setMinSize(80,80);
            calculator.getChildren().addAll(b);
            //b.setOnAction(e -> doSomething(b.getText()));
        }

        calcPane.setCenter(calculator);
        calcScene = new Scene(calcPane, 556, 500);
        convScene = new Scene(convPane, 556, 500);
        primarystage.setScene(calcScene);
        primarystage.show();
    }

    public void ButtonClicked(ActionEvent e) {
        if (e.getSource() == btnCalc) {
            currentStage.setScene(convScene);
        } else {
            currentStage.setScene(calcScene);
        }
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
