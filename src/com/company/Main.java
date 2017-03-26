package com.company;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.List;

/**
 * Created by ToubeauShawnD on 3/16/2017.
 */
public class Main extends Application{

    private List<String> buttons = Arrays.asList("7","8","9","DEL","CLEAR","NEG","4","5","6","^","Sqrt","SIN","1","2","3","+","-","COS",".","0","=","X","/","TAN");
    //private List<String> covnLabels = Arrays.asList("Decimal", "Binary", "Octal", "Hexadecimal");
    TextField textField = new TextField();
    TextField tfDecimal = new TextField();
    TextField tfBinary = new TextField();
    TextField tfHex = new TextField();
    TextField tfOct = new TextField();

    private Button btnCalc, btnConv;
    private Label lblCalc, lblConv;
    private BorderPane calcPane, convPane;
    private FlowPane calculator, converter;
    private Scene calcScene, convScene;
    private Stage currentStage;

    int textfieldWidth = (buttons.size()/4) * 85;

    @Override
    public void start(Stage primarystage) throws Exception {
        //Setup
        primarystage.setResizable(false);
        currentStage = primarystage;

        btnCalc = new Button("Converter");
        btnConv = new Button("Calculator");
        btnCalc.setOnAction(e-> ButtonClicked(e));
        btnConv.setOnAction(e-> ButtonClicked(e));

        lblCalc = new Label("This is the Calculator");
        lblConv = new Label("This is the Converter");

        calcPane = new BorderPane();
        calcPane.setMinWidth(600);
        calcPane.setMaxWidth(600);
        convPane = new BorderPane();

        calcPane.setStyle("-fx-background-color: white; -fx-padding: 10px;");
        convPane.setStyle("-fx-background-color: white; -fx-padding: 10px;");

        calcPane.setBottom(btnCalc);
        convPane.setBottom(btnConv);
        calculator = new FlowPane();
        converter = new FlowPane();

        calculator.setAlignment(Pos.CENTER);
        calculator.setPadding(new Insets(30,20,30,20));
        calculator.setHgap(5);
        calculator.setVgap(5);

        converter.setAlignment(Pos.CENTER);
        converter.setPadding(new Insets(30,20,30,20));
        converter.setHgap(5);
        converter.setVgap(5);

        //Conversion
        tfDecimal.setMinSize(580,40);
        converter.getChildren().addAll(new Label("Decimal"), tfDecimal);
        tfBinary.setMinSize(580,40);
        converter.getChildren().addAll(new Label("Binary"), tfBinary);
        tfHex.setMinSize(580,40);
        converter.getChildren().addAll(new Label("Hexadecimal"), tfHex);
        tfOct.setMinSize(580,40);
        converter.getChildren().addAll(new Label("Octal"), tfOct);

        convPane.setCenter(converter);

        //Calculator
        for (String button: buttons) {
            Button b = new Button(button);
            b.setMinSize(80,80);
            calculator.getChildren().addAll(b);
            //b.setOnAction(e -> doSomething(b.getText()));
        }

        textField.setEditable(false);
        textField.setMinSize(textfieldWidth, 70);
        calcPane.setTop(textField);
        calcPane.setCenter(calculator);

        calcScene = new Scene(calcPane, 600, 500);
        convScene = new Scene(convPane, 600, 500);
        primarystage.setScene(calcScene);
        primarystage.show();


        tfDecimal.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                int decimal = Integer.parseInt(tfDecimal.getText());
                String decToBin = Integer.toBinaryString(decimal);
                String decToHex = Integer.toHexString(decimal);
                String decToOct = Integer.toOctalString(decimal);

                tfBinary.setText(decToBin);
                tfHex.setText(decToHex);
                tfOct.setText(decToOct);
            }
        });

        tfBinary.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                int binary = Integer.parseInt(tfBinary.getText(),2);
                String binToDec = Integer.toString(binary);
                String binToHex = Integer.toHexString(binary);
                String binToOct = Integer.toOctalString(binary);

                tfDecimal.setText(binToDec);
                tfHex.setText(binToHex);
                tfOct.setText(binToOct);
            }
        });

        tfHex.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
            int hex = Integer.parseInt(tfHex.getText(),16);
            String hexToDec = Integer.toString(hex);
            String hextoBin = Integer.toBinaryString(hex);
            String hexToOct = Integer.toOctalString(hex);

            tfDecimal.setText(hexToDec);
            tfBinary.setText(hextoBin);
            tfOct.setText(hexToOct);
            }
        });

        tfOct.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                int oct = Integer.parseInt(tfOct.getText(), 8);
                String octToDec = Integer.toString(oct);
                String octToBin = Integer.toBinaryString(oct);
                String octToHex = Integer.toHexString(oct);

                tfDecimal.setText(octToDec);
                tfBinary.setText(octToDec);
                tfHex.setText(octToHex);
            }
        });
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
