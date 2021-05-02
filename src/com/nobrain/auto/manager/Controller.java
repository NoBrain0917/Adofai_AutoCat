package com.nobrain.auto.manager;

import com.nobrain.auto.lib.Adofai;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    public static Adofai adofai = null;
    private Stage primaryStage;
    private String defaultPath = null;



    @FXML
    private CheckBox workshop;
    @FXML
    private Label label;
    @FXML
    private Button button;
    @FXML
    private Button any;
    @FXML
    private TextField lag;
    @FXML
    private TextField name;
    @FXML
    private Label on;


    @FXML public void onApply() throws IOException, ParseException {
        adofai = new Adofai(null, lag, name, on, workshop);
    }

    @FXML public void onDragClick() throws IOException, ParseException {
        boolean isSelect = workshop.isSelected();

        name.setVisible(isSelect);
        label.setVisible(!isSelect);
        button.setVisible(!isSelect);
        any.setVisible(isSelect);

        if(isSelect) {
            if(name.getText()==null) return;
            if(name.getText().trim().length()<1) return;
            adofai = new Adofai(null, lag, name, on, workshop);
        } else {
            name.setText("");
            label.setText("파일 선택되지 않음");
            adofai = null;
        }
    }

    @FXML public void onButtonClick(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("얼불춤 파일", "*.adofai","*.ADOFAI"));
        fileChooser.setTitle("파일 선택");
        if(defaultPath!=null) {
            fileChooser.setInitialDirectory(new File(defaultPath));
        }
        File file = fileChooser.showOpenDialog(primaryStage);

        if(file!=null) {
            label.setText("파일 선택됨 - " + file.getName());
            defaultPath = file.getAbsolutePath().replace(file.getName(),"");

            try {
                adofai = new Adofai(file.getAbsolutePath(), lag, name, on, workshop);
            } catch (ParseException e) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("오류");
                System.out.println(e.toString());
                alert.setHeaderText(e.toString());
                alert.setContentText("맵을 분석할 수가 없습니다.");
                alert.showAndWait();
                adofai = null;
                label.setText("파싱 오류 - " + file.getName());
            }
        }
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        name.setOnKeyPressed(event -> {
            if(event.getCode().getName().equals("Enter")) {
                if(name.getText()==null) return;
                if(name.getText().trim().length()<1) return;

                try {
                    adofai = new Adofai(null, lag, name, on, workshop);
                } catch (ParseException e) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("오류");
                    System.out.println(e.toString());
                    alert.setHeaderText(e.toString());
                    alert.setContentText("맵을 분석할 수가 없습니다.");
                    alert.showAndWait();
                    adofai = null;
                    label.setText("파싱 오류");
                }
            }
        });
    }

}
