package com.company;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Group;
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
 * http://www.javafxtutorials.com/tutorials/switching-to-different-screens-in-javafx-and-fxml/
 * http://stackoverflow.com/questions/27313975/javafx-number-conversion
 */
public class Main extends Application {

    List<String> buttons = Arrays.asList("7", "8", "9", "DEL", "CLEAR", "Sin", "4", "5", "6", "^", "Sqrt", "Cos", "1", "2", "3", "+", "-", "Tan", ".", "0", "=", "X", "/", "Log", "!");
    String css = this.getClass().getResource("calculator.css").toExternalForm();

    TextField calcDisplay = new TextField();
    int height = 550;
    int width = 600;
    int textfieldWidth = width - 20;
    private Button btnCalc, btnNumber, btnLength, btnWeight, btnVolume, btnTemp;
    private Label lblCalc, lblConv, lblLength, lblWeight, lblVolume, lblTemp;
    BorderPane main;
    FlowPane fPanebuttons;
    Group root;
    private FlowPane fPaneCalc, fPaneNumber, fPanelength, fPaneWeight, fPaneVolume, fPaneTemp;
    private Stage currentStage;
    private ArrayList<Integer> nums = new ArrayList<>(); //individual button presses
    private ArrayList<Double> fullNums = new ArrayList<>(); //after user input is put together
    private ArrayList<String> operations = new ArrayList<>();//operations
    private Exp expression = new Exp();
    private Stack<Integer> lastPress = new Stack<>();

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primarystage) throws Exception {
        primarystage.setResizable(false);
        currentStage = primarystage;

        btnCalc = new Button("Calculator");
        btnCalc.getStyleClass().add("scene-button");
        btnCalc.setOnAction(e -> ButtonClicked(e));

        btnNumber = new Button("Number");
        btnNumber.getStyleClass().add("scene-button");
        btnNumber.setOnAction(e -> ButtonClicked(e));

        btnLength = new Button("Length");
        btnLength.getStyleClass().add("scene-button");
        btnLength.setOnAction(e -> ButtonClicked(e));

        btnWeight = new Button("Weight");
        btnWeight.getStyleClass().add("scene-button");
        btnWeight.setOnAction(e -> ButtonClicked(e));

        btnVolume = new Button("Volume");
        btnVolume.getStyleClass().add("scene-button");
        btnVolume.setOnAction(e -> ButtonClicked(e));

        btnTemp = new Button("Temperature");
        btnTemp.getStyleClass().add("scene-button");
        btnTemp.setOnAction(e -> ButtonClicked(e));

        calcPage();
        numberPage();
        lengthPage();
        weightPage();
        volumePage();
        tempPage();

        fPanebuttons = new FlowPane();
        fPanebuttons.getChildren().addAll(btnCalc, btnNumber, btnLength, btnWeight, btnVolume, btnTemp);

        main = new BorderPane();
        main.setMinSize(610, 560);
        main.getStyleClass().add("pane");
        main.setBottom(fPanebuttons);
        main.setTop(calcDisplay);
        main.setCenter(fPaneCalc);

        root = new Group();
        root.getChildren().add(main);

        Scene scene = new Scene(root, 600, 550);
        scene.getStylesheets().add(css);
        primarystage.setScene(scene);
        primarystage.setTitle("JavaFX Calculator");
        primarystage.show();
    }

    public void ButtonClicked(ActionEvent e) {
        if (e.getSource() == btnCalc) {
            main.setTop(calcDisplay);
            main.setCenter(fPaneCalc);
        } else if (e.getSource() == btnNumber) {
            main.setCenter(fPaneNumber);
            main.setTop(null);
        } else if (e.getSource() == btnLength) {
            main.setCenter(fPanelength);
            main.setTop(null);
        } else if (e.getSource() == btnWeight) {
            main.setCenter(fPaneWeight);
            main.setTop(null);
        } else if (e.getSource() == btnVolume) {
            main.setCenter(fPaneVolume);
            main.setTop(null);
        } else {
            main.setCenter(fPaneTemp);
            main.setTop(null);
        }
    }

    //handles the button presses
    public void doSomething(String s) {

        if (s.equals("0") || s.equals("1") || s.equals("2") || s.equals("3") || s.equals("4") || s.equals("5") || s.equals("6") || s.equals("7") ||
                s.equals("8") || s.equals("9")) {
            nums.add(Integer.parseInt(s));
            calcDisplay.setText(calcDisplay.getText() + s);
            lastPress.add((Integer) 0);
        } else if (s.equals("+") || s.equals("-") || s.equals("/") || s.equals("X") || s.equals("^")) {
            if (nums.size() != 0) {
                fullNums.add(retNums(nums));
            }
            nums.clear();
            operations.add(s);
            calcDisplay.setText(calcDisplay.getText() + s);
            lastPress.add(1);
        } else if (s.equals("=")) {

            fullNums.add(retNums(nums));
            Exp ans = calculate(fullNums, operations);
            double temp = ans.getNums().get(0);
            calcDisplay.setText(Double.toString(temp));
            nums.clear();
            fullNums.clear();
            operations.clear();
            fullNums.add(temp);
            lastPress.add(2);
        } else if (s.equals("CLEAR")) {
            nums.clear();
            fullNums.clear();
            operations.clear();
            calcDisplay.clear();
            lastPress.clear();
        } else if (s.equals("DEL")) {
            del(fullNums, operations, lastPress);

        } else if (s.equals(".")) {

            fullNums.add(retNums(nums));
            nums.clear();
            operations.add(s);
            calcDisplay.setText(calcDisplay.getText() + s);
            lastPress.add(1);
        } else if (s.equals("Sqrt") || s.equals("Sin") || s.equals("Cos") || s.equals("Tan") || s.equals("Log")) {
            double x = retNums(nums);
            nums.clear();
            if (s.equals("Sqrt")) {
                setTextMono(x, "Sqrt(");
                fullNums.add(Math.sqrt(x));
            } else if (s.equals("Sin")) {
                setTextMono(x, "Sin(");
                fullNums.add(Math.sin(x));
            } else if (s.equals("Cos")) {
                setTextMono(x, "Cos(");
                fullNums.add(Math.cos(x));
            } else if (s.equals("Tan")) {
                setTextMono(x, "Tan(");
                fullNums.add(Math.tan(x));
            } else if (s.equals("Log")) {
                setTextMono(x, "Log(");
                fullNums.add(Math.log(x));
            } else {
                fullNums.add(x);
            }

            lastPress.add(3);
        } else if (s.equals("!")) {
            double x = retNums(nums);
            nums.clear();
            lastPress.add(3);
            calcDisplay.setText(calcDisplay.getText() + s);
            fullNums.add(factorial(x));
        }
    }

    //combines all button presses up till operation into one number
    public double retNums(ArrayList<Integer> digits) {
        double ret = 0;
        int place = 0;
        for (int i = digits.size() - 1; i >= 0; i--) {
            ret += digits.get(i) * Math.pow(10, place);
            place++;
        }
        return ret;
    }

    //returns calculated answer as only element in either arraylist
    public Exp calculate(ArrayList<Double> numbers, ArrayList<String> operations) {
        Exp exp = new Exp(numbers, operations);
        double temp = 0;

        for (int i = 0; i < operations.size(); i++) {
            if (operations.get(i).equals(".")) {
                int k = Double.toString(numbers.get(i + 1)).length();
                k -= 2;
                temp = numbers.get(i) + (numbers.get(i + 1) / (Math.pow(10, k)));
                exp.adjust(temp, i);
            }
        }
        for (int i = 0; i < operations.size(); i++) {
            if (operations.get(i).equals("^")) {
                temp = Math.pow(numbers.get(i), numbers.get(i + 1));
                exp.adjust(temp, i);
            }
        }
        for (int i = 0; i < operations.size(); i++) {
            if (operations.get(i).equals("X")) {
                temp = numbers.get(i) * numbers.get(i + 1);
                exp.adjust(temp, i);
            } else if (operations.get(i).equals("/")) {
                temp = numbers.get(i) / numbers.get(i + 1);
                exp.adjust(temp, i);
            }
        }
        for (int i = 0; i < operations.size(); i++) {
            if (operations.get(i).equals("+")) {
                temp = numbers.get(i) + numbers.get(i + 1);
                exp.adjust(temp, i);
            } else if (operations.get(i).equals("-")) {
                temp = numbers.get(i) - numbers.get(i + 1);
                exp.adjust(temp, i);
            }
        }
        return exp;
    }

    public double factorial(double x) {
        if (x == 1) {
            return 1;
        } else {
            return x * factorial(x - 1);
        }
    }

    //DOESNT WORK RIGHT YET
    public void makeDecimals(ArrayList<Double> numbers, ArrayList<String> operations) {
        double decimal;
        double ret;
        for (int i = 0; i < operations.size(); i++) {
            if (operations.get(i).equals(".")) {
                decimal = numbers.get(i + 1);
                while (decimal > 1) {
                    decimal *= 0.1;
                }
                decimal += numbers.get(i);
                fullNums.remove(i);
                fullNums.remove(i - 1);
                operations.remove(i);
                fullNums.add(i - 1, decimal);
            }
        }
    }

    public void setTextMono(double x, String str) {
        System.out.print(x);
        StringBuilder newDisplay = new StringBuilder(calcDisplay.getText());
        StringBuilder s = new StringBuilder(Double.toString(x));
        s.delete(s.length() - 2, s.length());
        int i = newDisplay.indexOf(s.toString());
        newDisplay.insert(i + s.length(), ")");
        newDisplay.insert(i, str);
        calcDisplay.setText(newDisplay.toString());
    }

    public int lastFullNumsLength() {
        Double lastNum = fullNums.get(fullNums.size() - 1);
        String number = lastNum.toString();
        return number.length();
    }

    //Buggy when deleting operations ; if a number is pressed next it is seprate from the previous number and  display
    // ex. 22+ press del -> 22 press 3 -> 223 press + -> 223+ press = -> 25 ie 22+3
    //case 3 crashes
    public void del(ArrayList<Double> numbers, ArrayList<String> operations, Stack<Integer> lastPress) {
        int howMany = 0;
        int last = lastPress.pop();
        //0-> int button was last
        //1-> operation button
        //2-> equals button
        //3-> Mono parameter operation button
        //4-> . button
        //5-> DEL button
        switch (last) {
            case 0:
                if (nums.size() > 0) {
                    nums.remove(nums.size() - 1);
                }
                howMany = 1;
                if (!lastPress.empty()) {
                    last = lastPress.pop();
                }
                break;
            case 1:
                operations.remove(operations.size() - 1);
                howMany = 1;
                last = lastPress.pop();
                fixFullNums();
                break;
            case 2:
                fullNums.remove(fullNums.size() - 1);
                howMany = lastFullNumsLength();
                if (!lastPress.empty()) {
                    last = lastPress.pop();
                }
                break;
            case 3:
                operations.remove(operations.size() - 1);
                howMany = lastFullNumsLength() + 6;
                if (!lastPress.empty()) {
                    last = lastPress.pop();
                }
                break;


        }
        calcDisplay.setText(delText(calcDisplay.getText(), howMany));
        lastPress.add(last);
    }

    public String delText(String s, int howMany) {
        StringBuilder str = new StringBuilder(s);
        str.delete(str.length() - howMany, str.length());
        return str.toString();
    }

    public void fixFullNums() {
        Double x = fullNums.get(fullNums.size() - 1);
        String s = x.toString();
        Double replacement;
        fullNums.remove(fullNums.size() - 1);

        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '.') {
                //fullNums.add(retNums(nums));
                //operations.add(".");
                i = s.length() + 1;
                break;
            } else {
                nums.add((int) s.charAt(i) - 48);
            }
        }
        nums.trimToSize();
        fullNums.trimToSize();
    }

    public void calcPage() {
        lblCalc = new Label("This is the Calculator");

        fPaneCalc = new FlowPane();
        fPaneCalc.setAlignment(Pos.CENTER);
        fPaneCalc.setHgap(5);
        fPaneCalc.setVgap(5);
        calcDisplay.getStyleClass().add("display");
        calcDisplay.setEditable(false);
        calcDisplay.setMinSize(textfieldWidth, 70);
        //bPaneCalc.setTop(calcDisplay);

        for (String button : buttons) {
            Button b = new Button(button);
            b.getStyleClass().add("label");
            b.setMinSize(85, 78);
            b.getStyleClass().add("calc-button");
            fPaneCalc.getChildren().addAll(b);
            b.setOnAction(e -> doSomething(b.getText()));
        }
    }

    public void numberPage() {
        TextField decDisplay = new TextField();
        TextField binDisplay = new TextField();
        TextField hexDisplay = new TextField();
        TextField octDisplay = new TextField();

        lblConv = new Label("This is the Converter");

        fPaneNumber = new FlowPane();
        fPaneNumber.setAlignment(Pos.CENTER);
        fPaneNumber.setHgap(5);
        fPaneNumber.setVgap(5);

        decDisplay.setMinSize(textfieldWidth, 40);
        decDisplay.getStyleClass().add("display");
        fPaneNumber.getChildren().addAll(new Label("Decimal"), decDisplay);
        binDisplay.setMinSize(textfieldWidth, 40);
        binDisplay.getStyleClass().add("display");
        fPaneNumber.getChildren().addAll(new Label("Binary"), binDisplay);
        hexDisplay.setMinSize(textfieldWidth, 40);
        hexDisplay.getStyleClass().add("display");
        fPaneNumber.getChildren().addAll(new Label("Hexadecimal"), hexDisplay);
        octDisplay.setMinSize(textfieldWidth, 40);
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
                int binary = Integer.parseInt(binDisplay.getText(), 2);
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
                int hex = Integer.parseInt(hexDisplay.getText(), 16);
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

    public void lengthPage() {
        TextField milliDisplay = new TextField();
        TextField centiDisplay = new TextField();
        TextField meterDisplay = new TextField();
        TextField inchDisplay = new TextField();
        TextField feetDisplay = new TextField();

        lblLength = new Label("This is the Length Converter");

        fPanelength = new FlowPane();
        fPanelength.setAlignment(Pos.CENTER);
        fPanelength.setHgap(5);
        fPanelength.setVgap(5);

        milliDisplay.setMinSize(textfieldWidth, 40);
        milliDisplay.getStyleClass().add("display");
        fPanelength.getChildren().addAll(new Label("Millimeter"), milliDisplay);
        centiDisplay.setMinSize(textfieldWidth, 40);
        centiDisplay.getStyleClass().add("display");
        fPanelength.getChildren().addAll(new Label("Centimeter"), centiDisplay);
        meterDisplay.setMinSize(textfieldWidth, 40);
        meterDisplay.getStyleClass().add("display");
        fPanelength.getChildren().addAll(new Label("Meter"), meterDisplay);
        inchDisplay.setMinSize(textfieldWidth, 40);
        inchDisplay.getStyleClass().add("display");
        fPanelength.getChildren().addAll(new Label("Inch"), inchDisplay);
        feetDisplay.setMinSize(textfieldWidth, 40);
        feetDisplay.getStyleClass().add("display");
        fPanelength.getChildren().addAll(new Label("Feet"), feetDisplay);


        milliDisplay.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*.")) {
                    milliDisplay.setText(newValue.replaceAll("[^\\d.]", ""));
                }
            }
        });

        centiDisplay.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*.")) {
                    centiDisplay.setText(newValue.replaceAll("[^\\d.]", ""));
                }
            }
        });

        meterDisplay.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*.")) {
                    meterDisplay.setText(newValue.replaceAll("[^\\d.]", ""));
                }
            }
        });

        inchDisplay.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*.")) {
                    inchDisplay.setText(newValue.replaceAll("[^\\d.]", ""));
                }
            }
        });

        feetDisplay.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*.")) {
                    feetDisplay.setText(newValue.replaceAll("[^\\d.]", ""));
                }
            }
        });

        //Length Converters
        milliDisplay.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                double millimeter = Double.parseDouble(milliDisplay.getText());
                String centimeter = Double.toString(millimeter / 10.0);
                String meter = Double.toString(millimeter / 1000.0);
                String inch = Double.toString(millimeter / 25.4);
                String foot = Double.toString(millimeter / 304.8);

                centiDisplay.setText(centimeter);
                meterDisplay.setText(meter);
                inchDisplay.setText(inch);
                feetDisplay.setText(foot);
            }
        });

        centiDisplay.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                double centimeter = Double.parseDouble(centiDisplay.getText());
                String millimeter = Double.toString(centimeter * 10.0);
                String meter = Double.toString(centimeter / 100.0);
                String inch = Double.toString(centimeter / 2.54);
                String foot = Double.toString(centimeter / 30.48);

                milliDisplay.setText(millimeter);
                meterDisplay.setText(meter);
                inchDisplay.setText(inch);
                feetDisplay.setText(foot);
            }
        });

        meterDisplay.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                double meter = Double.parseDouble(meterDisplay.getText());
                String millimeter = Double.toString(meter * 1000.0);
                String centimeter = Double.toString(meter * 100.0);
                String inch = Double.toString(meter / .0254);
                String foot = Double.toString(meter / .3048);

                milliDisplay.setText(millimeter);
                centiDisplay.setText(centimeter);
                inchDisplay.setText(inch);
                feetDisplay.setText(foot);
            }
        });

        inchDisplay.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                double inch = Double.parseDouble(inchDisplay.getText());
                String millimeter = Double.toString(inch * 25.4);
                String centimeter = Double.toString(inch * 2.54);
                String meter = Double.toString(inch * .0254);
                String foot = Double.toString(inch / 12);

                milliDisplay.setText(millimeter);
                centiDisplay.setText(centimeter);
                meterDisplay.setText(meter);
                feetDisplay.setText(foot);
            }
        });

        feetDisplay.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                double feet = Double.parseDouble(feetDisplay.getText());
                String millimeter = Double.toString(feet * 304.8);
                String centimeter = Double.toString(feet * 30.48);
                String meter = Double.toString(feet * 0.3048);
                String inch = Double.toString(feet * 12);

                milliDisplay.setText(millimeter);
                centiDisplay.setText(centimeter);
                meterDisplay.setText(meter);
                inchDisplay.setText(inch);
            }
        });
    }

    public void weightPage() {
        TextField gramDisplay = new TextField();
        TextField kiloGramDisplay = new TextField();
        TextField ounceDisplay = new TextField();
        TextField poundDisplay = new TextField();

        lblWeight = new Label("This is the Weight Converter");

        fPaneWeight = new FlowPane();
        fPaneWeight.setAlignment(Pos.CENTER);
        fPaneWeight.setHgap(5);
        fPaneWeight.setVgap(5);

        gramDisplay.setMinSize(textfieldWidth, 40);
        gramDisplay.getStyleClass().add("display");
        fPaneWeight.getChildren().addAll(new Label("Gram"), gramDisplay);
        kiloGramDisplay.setMinSize(textfieldWidth, 40);
        kiloGramDisplay.getStyleClass().add("display");
        fPaneWeight.getChildren().addAll(new Label("Killogram"), kiloGramDisplay);
        ounceDisplay.setMinSize(textfieldWidth, 40);
        ounceDisplay.getStyleClass().add("display");
        fPaneWeight.getChildren().addAll(new Label("Ounce"), ounceDisplay);
        poundDisplay.setMinSize(textfieldWidth, 40);
        poundDisplay.getStyleClass().add("display");
        fPaneWeight.getChildren().addAll(new Label("Pound"), poundDisplay);

        gramDisplay.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*.")) {
                    gramDisplay.setText(newValue.replaceAll("[^\\d.]", ""));
                }
            }
        });

        kiloGramDisplay.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*.")) {
                    kiloGramDisplay.setText(newValue.replaceAll("[^\\d.]", ""));
                }
            }
        });

        ounceDisplay.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*.")) {
                    ounceDisplay.setText(newValue.replaceAll("[^\\d.]", ""));
                }
            }
        });

        poundDisplay.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*.")) {
                    poundDisplay.setText(newValue.replaceAll("[^\\d.]", ""));
                }
            }
        });

        //Length Converters
        gramDisplay.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                double gram = Double.parseDouble(gramDisplay.getText());
                String kilogram = Double.toString(gram / 1000.0);
                String ounce = Double.toString(gram * 0.03527396195);
                String pound = Double.toString(gram * 0.00220462262185);

                kiloGramDisplay.setText(kilogram);
                ounceDisplay.setText(ounce);
                poundDisplay.setText(pound);
            }
        });

        kiloGramDisplay.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                double kilogram = Double.parseDouble(kiloGramDisplay.getText());
                String gram = Double.toString(kilogram * 1000.0);
                String ounce = Double.toString(kilogram * 35.27396195);
                String pound = Double.toString(kilogram * 2.20462262185);

                gramDisplay.setText(gram);
                ounceDisplay.setText(ounce);
                poundDisplay.setText(pound);
            }
        });

        ounceDisplay.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                double ounce = Double.parseDouble(ounceDisplay.getText());
                String gram = Double.toString(ounce * 28.34952);
                String kilogram = Double.toString(ounce * 0.02834952);
                String pound = Double.toString(ounce * 0.0625);

                gramDisplay.setText(gram);
                kiloGramDisplay.setText(kilogram);
                poundDisplay.setText(pound);
            }
        });

        poundDisplay.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                double pound = Double.parseDouble(poundDisplay.getText());
                String gram = Double.toString(pound * 453.59237);
                String kilogram = Double.toString(pound * 0.45359237);
                String ounce = Double.toString(pound * 16.0);

                gramDisplay.setText(gram);
                kiloGramDisplay.setText(kilogram);
                ounceDisplay.setText(ounce);
            }
        });
    }

    public void volumePage() {
        TextField literDisplay = new TextField();
        TextField fluidOzDisplay = new TextField();
        TextField cupDisplay = new TextField();
        TextField pintDisplay = new TextField();
        TextField gallonDisplay = new TextField();

        lblVolume = new Label("This is the Volume Converter");

        fPaneVolume = new FlowPane();
        fPaneVolume.setAlignment(Pos.CENTER);
        fPaneVolume.setHgap(5);
        fPaneVolume.setVgap(5);

        literDisplay.setMinSize(textfieldWidth, 40);
        literDisplay.getStyleClass().add("display");
        fPaneVolume.getChildren().addAll(new Label("Liter"), literDisplay);
        fluidOzDisplay.setMinSize(textfieldWidth, 40);
        fluidOzDisplay.getStyleClass().add("display");
        fPaneVolume.getChildren().addAll(new Label("Fluid Ounces"), fluidOzDisplay);
        cupDisplay.setMinSize(textfieldWidth, 40);
        cupDisplay.getStyleClass().add("display");
        fPaneVolume.getChildren().addAll(new Label("Cups"), cupDisplay);
        pintDisplay.setMinSize(textfieldWidth, 40);
        pintDisplay.getStyleClass().add("display");
        fPaneVolume.getChildren().addAll(new Label("Pints"), pintDisplay);
        gallonDisplay.setMinSize(textfieldWidth, 40);
        gallonDisplay.getStyleClass().add("display");
        fPaneVolume.getChildren().addAll(new Label("Gallons"), gallonDisplay);


        literDisplay.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*.")) {
                    literDisplay.setText(newValue.replaceAll("[^\\d.]", ""));
                }
            }
        });

        fluidOzDisplay.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*.")) {
                    fluidOzDisplay.setText(newValue.replaceAll("[^\\d.]", ""));
                }
            }
        });

        cupDisplay.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*.")) {
                    cupDisplay.setText(newValue.replaceAll("[^\\d.]", ""));
                }
            }
        });

        pintDisplay.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*.")) {
                    pintDisplay.setText(newValue.replaceAll("[^\\d.]", ""));
                }
            }
        });

        gallonDisplay.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*.")) {
                    gallonDisplay.setText(newValue.replaceAll("[^\\d.]", ""));
                }
            }
        });

        //Length Converters
        literDisplay.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                double liter = Double.parseDouble(literDisplay.getText());
                String fluidOz = Double.toString(liter * 33.81402);
                String cup = Double.toString(liter * 4.2267528377);
                String pint = Double.toString(liter * 2.113376);
                String gallon = Double.toString(liter * 0.26172);

                fluidOzDisplay.setText(fluidOz);
                cupDisplay.setText(cup);
                pintDisplay.setText(pint);
                gallonDisplay.setText(gallon);
            }
        });

        fluidOzDisplay.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                double fluidOz = Double.parseDouble(fluidOzDisplay.getText());
                String liter = Double.toString(fluidOz * 0.029574);
                String cup = Double.toString(fluidOz * 0.125);
                String pint = Double.toString(fluidOz * 0.0625);
                String gallon = Double.toString(fluidOz * 0.007813);

                literDisplay.setText(liter);
                cupDisplay.setText(cup);
                pintDisplay.setText(pint);
                gallonDisplay.setText(gallon);
            }
        });

        cupDisplay.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                double cup = Double.parseDouble(cupDisplay.getText());
                String liter = Double.toString(cup * 0.236588);
                String fluidOz = Double.toString(cup * 8);
                String pint = Double.toString(cup * 0.5);
                String gallon = Double.toString(cup * 0.0625);

                literDisplay.setText(liter);
                fluidOzDisplay.setText(fluidOz);
                pintDisplay.setText(pint);
                gallonDisplay.setText(gallon);
            }
        });

        pintDisplay.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                double pint = Double.parseDouble(pintDisplay.getText());
                String liter = Double.toString(pint * 0.473176);
                String fluidOz = Double.toString(pint * 16);
                String cup = Double.toString(pint * 2);
                String gallon = Double.toString(pint * 0.125);

                literDisplay.setText(liter);
                fluidOzDisplay.setText(fluidOz);
                cupDisplay.setText(cup);
                gallonDisplay.setText(gallon);
            }
        });

        gallonDisplay.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                double gallon = Double.parseDouble(gallonDisplay.getText());
                String liter = Double.toString(gallon * 3.785412);
                String fluidOz = Double.toString(gallon * 128);
                String cup = Double.toString(gallon * 16);
                String pint = Double.toString(gallon * 8);

                literDisplay.setText(liter);
                fluidOzDisplay.setText(fluidOz);
                cupDisplay.setText(cup);
                pintDisplay.setText(pint);
            }
        });
    }

    public void tempPage() {
        TextField celsiusDisplay = new TextField();
        TextField fahrenheitDisplay = new TextField();
        TextField kelvinDisplay = new TextField();
        //TextField pintDisplay = new TextField();
        //TextField gallonDisplay = new TextField();

        lblTemp = new Label("This is the Temperature Converter");

        fPaneTemp = new FlowPane();
        fPaneTemp.setAlignment(Pos.CENTER);
        fPaneTemp.setHgap(5);
        fPaneTemp.setVgap(5);

        celsiusDisplay.setMinSize(textfieldWidth, 40);
        celsiusDisplay.getStyleClass().add("display");
        fPaneTemp.getChildren().addAll(new Label("Celsius"), celsiusDisplay);
        fahrenheitDisplay.setMinSize(textfieldWidth, 40);
        fahrenheitDisplay.getStyleClass().add("display");
        fPaneTemp.getChildren().addAll(new Label("Fahrenheit"), fahrenheitDisplay);
        kelvinDisplay.setMinSize(textfieldWidth, 40);
        kelvinDisplay.getStyleClass().add("display");
        fPaneTemp.getChildren().addAll(new Label("Kelvin"), kelvinDisplay);

        celsiusDisplay.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*.-")) {
                    celsiusDisplay.setText(newValue.replaceAll("[^\\d.-]", ""));
                }
            }
        });

        fahrenheitDisplay.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*.-")) {
                    fahrenheitDisplay.setText(newValue.replaceAll("[^\\d.-]", ""));
                }
            }
        });

        kelvinDisplay.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*.-")) {
                    kelvinDisplay.setText(newValue.replaceAll("[^\\d.-]", ""));
                }
            }
        });

        //Length Converters
        celsiusDisplay.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                double celsius = Double.parseDouble(celsiusDisplay.getText());
                String fahrenheit = Double.toString(celsius * 1.8 + 32);
                String kelvin = Double.toString(celsius + 273.15);

                fahrenheitDisplay.setText(fahrenheit);
                kelvinDisplay.setText(kelvin);
            }
        });

        fahrenheitDisplay.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                double fahrenheit = Double.parseDouble(fahrenheitDisplay.getText());
                String celcius = Double.toString((fahrenheit - 32) * 5 / 9);
                String kelvin = Double.toString((fahrenheit + 459.67) * 5 / 9);

                celsiusDisplay.setText(celcius);
                kelvinDisplay.setText(kelvin);
            }
        });

        kelvinDisplay.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                double kelvin = Double.parseDouble(kelvinDisplay.getText());
                String celsius = Double.toString(kelvin - 273.15);
                String fahrenheit = Double.toString((kelvin * 9 / 5) - 459.67);

                celsiusDisplay.setText(celsius);
                fahrenheitDisplay.setText(fahrenheit);
            }
        });
    }
}