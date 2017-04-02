package com.company;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
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
    String css = this.getClass().getResource("calculator.css").toExternalForm();
    TextField calcDisplay = new TextField();
    TextField decDisplay = new TextField();
    TextField binDisplay = new TextField();
    TextField hexDisplay = new TextField();
    TextField octDisplay = new TextField();
    private Button btnCalc, btnConv;
    private Label lblCalc, lblConv;
    private BorderPane calcPane, convPane;
    private FlowPane calculator, converter;
    private Scene calcScene, convScene;
    private Stage currentStage;

    int textfieldWidth = (buttons.size()/4) * 85;

    @Override
    public void start(Stage primarystage) throws Exception {
        primarystage.setResizable(false);
        currentStage = primarystage;

        //Calculator Setup
        btnCalc = new Button("Converter");
        btnCalc.getStyleClass().add("scene-button"); //CSS
        btnCalc.setOnAction(e-> ButtonClicked(e));

        lblCalc = new Label("This is the Calculator");
        calcPane = new BorderPane();
        calcPane.getStyleClass().add("pane");
        calcPane.setBottom(btnCalc);
        calculator = new FlowPane();
        calculator.setAlignment(Pos.CENTER);
        //calculator.setPadding(new Insets(30,20,20,20));
        calculator.setHgap(5);
        calculator.setVgap(5);
        calcDisplay.getStyleClass().add("display");
        calcDisplay.setEditable(false);
        calcDisplay.setMinSize(textfieldWidth, 70);
        calcPane.setTop(calcDisplay);

        //Calculator Buttons


        for (String button: buttons) {
            Button b = new Button(button);
            b.getStyleClass().add("label");
            b.setMinSize(85,78);
            b.getStyleClass().add("calc-button");
            calculator.getChildren().addAll(b);
            //b.setOnAction(e -> doSomething(b.getText()));
        }

        calcPane.setCenter(calculator);


        //Converter Setup
        convPane = new BorderPane();
        convPane.getStyleClass().add("pane");
        converter = new FlowPane();
        converter.setAlignment(Pos.CENTER);
        //converter.setPadding(new Insets(30,20,20,20));
        converter.setHgap(5);
        converter.setVgap(5);
        lblConv = new Label("This is the Converter");

        btnConv = new Button("Calculator");
        btnConv.getStyleClass().add("scene-button");
        btnConv.setOnAction(e-> ButtonClicked(e));
        convPane.setBottom(btnConv);

//        Label decLabel = new Label("Decimal");
//        //decLabel.getStyleClass().add("label");

        decDisplay.setMinSize(580,40);
        decDisplay.getStyleClass().add("display");
        converter.getChildren().addAll(new Label("Decimal"), decDisplay);
        binDisplay.setMinSize(580,40);
        binDisplay.getStyleClass().add("display");
        converter.getChildren().addAll(new Label("Binary"), binDisplay);
        hexDisplay.setMinSize(580,40);
        hexDisplay.getStyleClass().add("display");
        converter.getChildren().addAll(new Label("Hexadecimal"), hexDisplay);
        octDisplay.setMinSize(580,40);
        octDisplay.getStyleClass().add("display");
        converter.getChildren().addAll(new Label("Octal"), octDisplay);

        convPane.setCenter(converter);

        //Draw
        calcScene = new Scene(calcPane, 600, 500);
        convScene = new Scene(convPane, 600, 500);
        calcScene.getStylesheets().add(css);
        convScene.getStylesheets().add(css);
        primarystage.setScene(calcScene);
        primarystage.setTitle("JavaFX Calculator");
        primarystage.show();

        //Conversion Listeners & Handlers
        decDisplay.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    decDisplay.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        binDisplay.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("01")) {
                    binDisplay.setText(newValue.replaceAll("[^01]", ""));
                }
            }
        });

        hexDisplay.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("0123456789abcdef")) {
                    hexDisplay.setText(newValue.replaceAll("[^0123456789abcdef]", ""));
                }
            }
        });

        octDisplay.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("01234567")) {
                    octDisplay.setText(newValue.replaceAll("[^01234567]", ""));
                }
            }
        });


        decDisplay.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                int decimal = Integer.parseInt(decDisplay.getText());
                String decToBin = Integer.toBinaryString(decimal);
                String decToHex = Integer.toHexString(decimal);
                String decToOct = Integer.toOctalString(decimal);

                binDisplay.setText(decToBin);
                hexDisplay.setText(decToHex);
                octDisplay.setText(decToOct);
            }
        });

        binDisplay.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                int binary = Integer.parseInt(binDisplay.getText(),2);
                String binToDec = Integer.toString(binary);
                String binToHex = Integer.toHexString(binary);
                String binToOct = Integer.toOctalString(binary);

                decDisplay.setText(binToDec);
                hexDisplay.setText(binToHex);
                octDisplay.setText(binToOct);
            }
        });

        hexDisplay.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
            int hex = Integer.parseInt(hexDisplay.getText(),16);
            String hexToDec = Integer.toString(hex);
            String hextoBin = Integer.toBinaryString(hex);
            String hexToOct = Integer.toOctalString(hex);

            decDisplay.setText(hexToDec);
            binDisplay.setText(hextoBin);
            octDisplay.setText(hexToOct);
            }
        });

        octDisplay.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                int oct = Integer.parseInt(octDisplay.getText(), 8);
                String octToDec = Integer.toString(oct);
                String octToBin = Integer.toBinaryString(oct);
                String octToHex = Integer.toHexString(oct);

                decDisplay.setText(octToDec);
                binDisplay.setText(octToDec);
                hexDisplay.setText(octToHex);
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
