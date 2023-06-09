package GUI;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import controller.Controller;

public class ParserUI {

    @FXML
    private TextArea sourceCode;

    @FXML
    private TextArea errorMessage;

    @FXML
    private Label outputStatus;

    private File file;


    @FXML
    void chooseFileAction(ActionEvent event) {
        clearScreen();
        FileChooser fileChooser = new FileChooser();
        file = fileChooser.showOpenDialog(null);
        writeFileOnScreen();
        sourceCode.setEditable(false);
    }

    private void writeFileOnScreen() {
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine() + "\n";
                sourceCode.setText(sourceCode.getText() + line);
            }

        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Wrong File");
            alert.show();
        }
    }
    private void writeFileOnScreen(int lineNumber) {
        try {
        	int i=0,first=0,last=0;
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
            	i++;
                String line = scanner.nextLine() + "\n";
                int f=sourceCode.getLength();
                sourceCode.setText(sourceCode.getText() + line);
                if(i==lineNumber) {
                	first=f;last=line.length()+first;
                }
            }

            sourceCode.setStyle("-fx-highlight-fill: red;");
            sourceCode.selectRange(first, last);

        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Wrong File");
            alert.show();
        }
    }

    @FXML
    void clearAction(ActionEvent event) {
        clearScreen();
    }

    @FXML
    void parsingAction(ActionEvent event) throws NullPointerException {

        try {
        	String result=Controller.RunProgram(file.getAbsolutePath());
            if(!result.equals("Successful parsing.")) {
            	sourceCode.clear();
            	errorMessage.setText(result);
                writeFileOnScreen(extractLineNumber(result));
            }else {
                outputStatus.setText(result);
            }
        } catch (Exception syntaxException) {
            outputStatus.setText("Fail Parsing ");
        }
    }
    
    
    public int extractLineNumber(String input) {
        // Define a regular expression to match the line number pattern
        Pattern pattern = Pattern.compile("^line (\\d+) :");

        // Match the input string against the regular expression
        Matcher matcher = pattern.matcher(input);

        // If the pattern is found, extract the line number and return it as an integer
        if (matcher.find()) {
            String lineNumber = matcher.group(1);
            return Integer.parseInt(lineNumber);
        } else {
            // If the pattern is not found, return -1 to indicate an error
            return -1;
        }
    }


    private void clearScreen() {
        sourceCode.clear();
        errorMessage.clear();
        outputStatus.setText("");
    }
}
