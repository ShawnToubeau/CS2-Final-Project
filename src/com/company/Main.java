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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

/**
 * Created by Shawn Toubeau and Harry Gordenstein on 3/16/2017.
 */
public class Main extends Application{

    List<String> buttons = Arrays.asList("7","8","9","DEL","CLEAR","Sin","4","5","6","^","Sqrt","Cos","1","2","3","+","-","Tan",".","0","=","X","/","Log","!");
    String css = this.getClass().getResource("calculator.css").toExternalForm();

    TextField calcDisplay = new TextField();
    int height = 550;
    int width = 600;
    int textfieldWidth = width-20;
    private Button btnCalc, btnNumber, btnLength;
    private Label lblCalc, lblConv, lblLength;
    private BorderPane bPaneCalc, bPaneNumber, bPaneLength;
    private FlowPane fPaneCalc, fPaneNumber, fPanelength;
    private Scene sceneCalc, sceneNumber, sceneLength;
    private Stage currentStage;
    private ArrayList<Integer> nums = new ArrayList<>(); //individual button presses
    private ArrayList<Double> fullNums = new ArrayList<>(); //after user input is put together
    private ArrayList<String> operations = new ArrayList<>();//operations
    private Exp expression = new Exp();
    private Stack<Integer> lastPress =new Stack<>();

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primarystage) throws Exception {
        primarystage.setResizable(false);
        currentStage = primarystage;

        btnCalc = new Button("Calculator");
        btnCalc.getStyleClass().add("scene-button");
        btnCalc.setOnAction(e-> ButtonClicked(e));

        btnNumber = new Button("Number");
        btnNumber.getStyleClass().add("scene-button");
        btnNumber.setOnAction(e-> ButtonClicked(e));

        btnLength = new Button("Length");
        btnLength.getStyleClass().add("scene-button");
        btnLength.setOnAction(e-> ButtonClicked(e));

        calcPage();
        numberPage();
        //lengthPage();

        primarystage.setScene(sceneCalc);
        primarystage.setTitle("JavaFX Calculator");
        primarystage.show();
    }

    public void ButtonClicked(ActionEvent e) {
        if (e.getSource() == btnCalc) {
            currentStage.setScene(sceneCalc);
        } else if (e.getSource() == btnNumber) {
            currentStage.setScene(sceneNumber);
        } else {
            currentStage.setScene(sceneLength);
        }
    }

    //handles the button presses
    public void doSomething(String s){

        if(s.equals("0")||s.equals("1")||s.equals("2")||s.equals("3")||s.equals("4")||s.equals("5")||s.equals("6")||s.equals("7")||
                s.equals("8")||s.equals("9")){
            nums.add(Integer.parseInt(s));
            calcDisplay.setText(calcDisplay.getText()+s);
            lastPress.add((Integer) 0);
        }
        else if(s.equals("+")||s.equals("-")||s.equals("/")||s.equals("X")||s.equals("^")){
            if(nums.size()!=0){
                fullNums.add(retNums(nums));
            }
            nums.clear();
            operations.add(s);
            calcDisplay.setText(calcDisplay.getText()+s);
            lastPress.add(1);
        }
        else if(s.equals("=")){

            fullNums.add(retNums(nums));
            Exp ans = calculate(fullNums,operations);
            double temp= ans.getNums().get(0);
            calcDisplay.setText(Double.toString(temp));
            nums.clear();
            fullNums.clear();
            operations.clear();
            fullNums.add(temp);
            lastPress.add(2);
        }
        else if(s.equals("CLEAR")){
            nums.clear();
            fullNums.clear();
            operations.clear();
            calcDisplay.clear();
            lastPress.clear();
        }
        else if(s.equals("DEL")){
            del(fullNums, operations,lastPress);

        }
        else if(s.equals(".")){

            fullNums.add(retNums(nums));
            nums.clear();
            operations.add(s);
            calcDisplay.setText(calcDisplay.getText()+s);
            lastPress.add(1);
        }
        else if(s.equals("Sqrt")||s.equals("Sin")||s.equals("Cos")||s.equals("Tan")||s.equals("Log")){
            double x = retNums(nums);
            nums.clear();
            if(s.equals("Sqrt")){
                setTextMono(x,"Sqrt(");
                fullNums.add(Math.sqrt(x));
            }else if(s.equals("Sin")){
                setTextMono(x,"Sin(");
                fullNums.add(Math.sin(x));
            }else if(s.equals("Cos")){
                setTextMono(x,"Cos(");
                fullNums.add(Math.cos(x));
            }else if(s.equals("Tan")){
                setTextMono(x,"Tan(");
                fullNums.add(Math.tan(x));
            }else if(s.equals("Log")){
                setTextMono(x,"Log(");
                fullNums.add(Math.log(x));
            }else{
                fullNums.add(x);
            }

            lastPress.add(3);
        }else if(s.equals("!")){
            double x = retNums(nums);
            nums.clear();
            lastPress.add(3);
            calcDisplay.setText(calcDisplay.getText()+s);
            fullNums.add(factorial(x));
        }
    }
    //combines all button presses up till operation into one number
    public double retNums(ArrayList<Integer> digits){
        double ret = 0;
        int place = 0;
        for(int i=digits.size()-1; i>=0; i--){
            ret += digits.get(i)*Math.pow(10,place);
            place++;
        }
        return ret;
    }

    //returns calculated answer as only element in either arraylist
    public Exp calculate(ArrayList<Double> numbers, ArrayList<String> operations){
        Exp exp = new Exp(numbers, operations);
        double temp=0;

        for(int i=0; i<operations.size(); i++){
            if(operations.get(i).equals(".")) {
                int k = Double.toString(numbers.get(i+1)).length();
                k-=2;
                temp =numbers.get(i)+(numbers.get(i+1)/(Math.pow(10,k)));
                exp.adjust(temp,i);
            }
        }
        for(int i=0; i<operations.size(); i++){
            if(operations.get(i).equals("^")) {
                temp =Math.pow( numbers.get(i) , numbers.get(i + 1));
                exp.adjust(temp,i);
            }
        }
        for(int i=0; i<operations.size(); i++) {
            if (operations.get(i).equals("X")) {
                temp = numbers.get(i)*numbers.get(i + 1);
                exp.adjust(temp,i);
            } else if (operations.get(i).equals("/")) {
                temp = numbers.get(i)/numbers.get(i + 1);
                exp.adjust(temp,i);
            }
        }
        for(int i=0; i<operations.size(); i++){
            if(operations.get(i).equals("+")){
                temp = numbers.get(i)+numbers.get(i+1);
                exp.adjust(temp,i);
            }
            else if(operations.get(i).equals("-")){
                temp = numbers.get(i)-numbers.get(i+1);
                exp.adjust(temp,i);
            }
        }
        return exp;
    }

    public double factorial(double x){
        if(x==1){
            return 1;
        }
        else{
            return x*factorial(x-1);
        }
    }

    //DOESNT WORK RIGHT YET
    public void makeDecimals(ArrayList<Double> numbers, ArrayList<String> operations){
        double decimal;
        double ret;
        for(int i=0; i<operations.size(); i++){
            if(operations.get(i).equals(".")) {
                decimal = numbers.get(i+1);
                while (decimal > 1) {
                    decimal *= 0.1;
                }
                decimal+=numbers.get(i);
                fullNums.remove(i);
                fullNums.remove(i-1);
                operations.remove(i);
                fullNums.add(i-1, decimal);
            }
        }
    }

    public void setTextMono(double x, String str){
        System.out.print(x);
        StringBuilder newDisplay = new StringBuilder(calcDisplay.getText());
        StringBuilder s = new StringBuilder(Double.toString(x));
        s.delete(s.length()-2,s.length());
        int i = newDisplay.indexOf(s.toString());
        newDisplay.insert(i+s.length(),")");
        newDisplay.insert(i,str);
        calcDisplay.setText(newDisplay.toString());
    }
    public int lastFullNumsLength(){
        Double lastNum = fullNums.get(fullNums.size()-1);
        String number = lastNum.toString();
        return number.length();
    }
    //Buggy when deleting operations ; if a number is pressed next it is seprate from the previous number and  display
    // ex. 22+ press del -> 22 press 3 -> 223 press + -> 223+ press = -> 25 ie 22+3
    //case 3 crashes
    public void del(ArrayList<Double> numbers, ArrayList<String> operations, Stack<Integer> lastPress){
        int howMany=0;
        int last = lastPress.pop();
        //0-> int button was last
        //1-> operation button
        //2-> equals button
        //3-> Mono parameter operation button
        //4-> . button
        //5-> DEL button
        switch (last){
            case 0:
                if(nums.size()>0){
                    nums.remove(nums.size()-1);
                }
                howMany=1;
                if(!lastPress.empty()) {
                    last = lastPress.pop();
                }
                break;
            case 1: operations.remove(operations.size()-1);
                howMany=1;
                last = lastPress.pop();
                fixFullNums();
                break;
            case 2: fullNums.remove(fullNums.size()-1);
                howMany = lastFullNumsLength();
                if(!lastPress.empty()) {
                    last = lastPress.pop();
                }
                break;
            case 3: operations.remove(operations.size()-1);
                howMany = lastFullNumsLength()+6;
                if(!lastPress.empty()) {
                    last = lastPress.pop();
                }
                break;


        }
        calcDisplay.setText(delText(calcDisplay.getText(), howMany));
        lastPress.add(last);
    }
    public String delText(String s, int howMany){
        StringBuilder str = new StringBuilder(s);
        str.delete(str.length()-howMany , str.length());
        return str.toString();
    }
    public void fixFullNums(){
        Double x =fullNums.get(fullNums.size()-1);
        String s =x.toString();
        Double replacement;
        fullNums.remove(fullNums.size()-1);

        for(int i =0; i<s.length(); i++){
            if(s.charAt(i)=='.'){
                //fullNums.add(retNums(nums));
                //operations.add(".");
                i=s.length()+1;
                break;
            }else {
                nums.add((int) s.charAt(i) - 48);
            }
        }
        nums.trimToSize();
        fullNums.trimToSize();
    }

    public void calcPage() {
        lblCalc = new Label("This is the Calculator");
        FlowPane flow = new FlowPane();

        //flow.getChildren().add(btnNumber);
        //flow.getChildren().add(btnLength);

        bPaneCalc = new BorderPane();
        bPaneCalc.getStyleClass().add("pane");
        bPaneCalc.setBottom(btnNumber);
        //bPaneCalc.setBottom(flow);

        fPaneCalc = new FlowPane();
        fPaneCalc.setAlignment(Pos.CENTER);
        fPaneCalc.setHgap(5);
        fPaneCalc.setVgap(5);
        calcDisplay.getStyleClass().add("display");
        calcDisplay.setEditable(false);
        calcDisplay.setMinSize(textfieldWidth, 70);
        bPaneCalc.setTop(calcDisplay);

        for (String button: buttons) {
            Button b = new Button(button);
            b.getStyleClass().add("label");
            b.setMinSize(85,78);
            b.getStyleClass().add("calc-button");
            fPaneCalc.getChildren().addAll(b);
            b.setOnAction(e -> doSomething(b.getText()));
        }
        bPaneCalc.setCenter(fPaneCalc);

        sceneCalc = new Scene(bPaneCalc, width, height);
        sceneCalc.getStylesheets().add(css);
    }

    public void numberPage() {
        TextField decDisplay = new TextField();
        TextField binDisplay = new TextField();
        TextField hexDisplay = new TextField();
        TextField octDisplay = new TextField();

        FlowPane flow2 = new FlowPane();

        //flow2.getChildren().add(btnCalc);
        //flow2.getChildren().add(btnNumber);

        lblConv = new Label("This is the Converter");
        bPaneNumber = new BorderPane();
        bPaneNumber.getStyleClass().add("pane");
        bPaneNumber.setBottom(btnCalc);
        //bPaneNumber.setBottom(flow2);

        fPaneNumber = new FlowPane();
        fPaneNumber.setAlignment(Pos.CENTER);
        fPaneNumber.setHgap(5);
        fPaneNumber.setVgap(5);

        decDisplay.setMinSize(textfieldWidth,40);
        decDisplay.getStyleClass().add("display");
        fPaneNumber.getChildren().addAll(new Label("Decimal"), decDisplay);
        binDisplay.setMinSize(textfieldWidth,40);
        binDisplay.getStyleClass().add("display");
        fPaneNumber.getChildren().addAll(new Label("Binary"), binDisplay);
        hexDisplay.setMinSize(textfieldWidth,40);
        hexDisplay.getStyleClass().add("display");
        fPaneNumber.getChildren().addAll(new Label("Hexadecimal"), hexDisplay);
        octDisplay.setMinSize(textfieldWidth,40);
        octDisplay.getStyleClass().add("display");
        fPaneNumber.getChildren().addAll(new Label("Octal"), octDisplay);


        //Number converters
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

        //Event listeners for Enter key
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


        bPaneNumber.setCenter(fPaneNumber);
        sceneNumber = new Scene(bPaneNumber, width, height);
        sceneNumber.getStylesheets().add(css);
    }

    public void lengthPage() {
        TextField millimeter = new TextField();
        TextField centimeters = new TextField();
        TextField meter = new TextField();
        TextField inch = new TextField();
        TextField feet = new TextField();
        TextField mile = new TextField();

        FlowPane flow3 = new FlowPane();
        //flow3.getChildren().add(btnCalc);
        //flow3.getChildren().add(btnNumber);

        lblLength = new Label("This is the Length Converter");

        bPaneLength = new BorderPane();
        bPaneLength.getStyleClass().add("pane");
        //bPaneLength.setBottom(btnCalc);
        //bPaneLength.setBottom(flow3);

        fPanelength = new FlowPane();
        fPanelength.setAlignment(Pos.CENTER);
        //fPaneCalc.setPadding(new Insets(30,20,20,20));
        fPanelength.setHgap(5);
        fPanelength.setVgap(5);

        millimeter.setMinSize(textfieldWidth,40);
        millimeter.getStyleClass().add("display");
        fPanelength.getChildren().addAll(new Label("Millimeter"), millimeter);
        centimeters.setMinSize(textfieldWidth,40);
        centimeters.getStyleClass().add("display");
        fPanelength.getChildren().addAll(new Label("Centimeter"), centimeters);
        meter.setMinSize(textfieldWidth,40);
        meter.getStyleClass().add("display");
        fPanelength.getChildren().addAll(new Label("Meter"), meter);
        inch.setMinSize(textfieldWidth,40);
        inch.getStyleClass().add("display");
        fPanelength.getChildren().addAll(new Label("Inch"), inch);
        feet.setMinSize(textfieldWidth,40);
        feet.getStyleClass().add("display");
        fPanelength.getChildren().addAll(new Label("Feet"), feet);
        mile.setMinSize(textfieldWidth,40);
        mile.getStyleClass().add("display");
        fPanelength.getChildren().addAll(new Label("Mile"), mile);

        bPaneLength.setCenter(fPanelength);
        sceneLength = new Scene(bPaneLength, width, height);
    }
}