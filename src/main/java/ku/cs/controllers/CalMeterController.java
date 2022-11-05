package ku.cs.controllers;

import com.github.saacsos.FXRouter;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import ku.cs.models.Customer;
import ku.cs.models.Meter;
import ku.cs.services.DBConnector;
import ku.cs.services.Effect;


import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class CalMeterController {

    @FXML
    private Label electBillLabel;

    @FXML
    private TextField electMeterTextField;

    @FXML
    private Label electUnitLabel;

    @FXML
    private Label lastMonthElectLabel;

    @FXML
    private Label lastMonthLabel;

    @FXML
    private Label lastMonthLabel1;

    @FXML
    private Label lastMonthWaterLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private ListView<String> roomListView;

    @FXML
    private Label roomNumberLabel;

    @FXML
    private Label waterBillLabel;

    @FXML
    private TextField waterMeterTextField;
    public DatePicker datePicker;
    @FXML
    private Label waterUnitLabel;

    @FXML
    private Pane calculateSuccessfulPane;

    @FXML
    private Label errorLabel;

    private Effect effect;


    private Meter meter;

    public void initialize(){
        showListView();
        handleSelectedListView();
        effect = new Effect();
        calculateSuccessfulPane.setDisable(true);
        calculateSuccessfulPane.setOpacity(0);
    }


    public void showListView() {
        try {
            Connection con = DBConnector.getConnection();
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT เลขที่ห้องเช่า FROM ลูกค้า");
            while (resultSet.next()) {
                String เลขที่ห้องเช่า = resultSet.getString(1);
                String listOut = เลขที่ห้องเช่า;
                roomListView.getItems().add(listOut);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void handleSelectedListView() {
        roomListView.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                        showDataFromDB(t1);
                    }
                }
        );
    }

    public void showDataFromDB(String t1) {
        try {
            Connection con = DBConnector.getConnection();
            Statement statement1 = con.createStatement();
            Statement statement2 = con.createStatement();
            ResultSet resultSet1 = statement1.executeQuery("SELECT เลขที่ห้องเช่า,วัน_เดือน_ปีที่จด,มิเตอร์น้ำเดือนปัจจุบัน,มิเตอร์ไฟเดือนปัจจุบัน FROM มิเตอร์ WHERE (เลขที่ห้องเช่า,วัน_เดือน_ปีที่จด) IN ( SELECT เลขที่ห้องเช่า, MAX(วัน_เดือน_ปีที่จด) FROM มิเตอร์ WHERE เลขที่ห้องเช่า = "+t1+");");
            ResultSet resultSet2 = statement2.executeQuery("SELECT ชื่อ_นามสกุล FROM ลูกค้า Where เลขที่ห้องเช่า = "+t1);
            while (resultSet1.next() & resultSet2.next()) {
               nameLabel.setText(resultSet2.getString("ชื่อ_นามสกุล"));
               roomNumberLabel.setText(resultSet1.getString("เลขที่ห้องเช่า"));
               lastMonthLabel.setText(dateString(resultSet1.getDate("วัน_เดือน_ปีที่จด").toLocalDate()));
               lastMonthLabel1.setText(dateString(resultSet1.getDate("วัน_เดือน_ปีที่จด").toLocalDate()));
               lastMonthWaterLabel.setText(resultSet1.getString("มิเตอร์น้ำเดือนปัจจุบัน"));
               lastMonthElectLabel.setText(resultSet1.getString("มิเตอร์ไฟเดือนปัจจุบัน"));
            }
            statement1.close();
            statement2.close();
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public float calWater(int oldWater, int thisWater, int waterUnit) {
        return (thisWater - oldWater) * waterUnit;
    }

    public float calElect(int oldElect, int thisElect, int electUnit) {
        return (thisElect - oldElect) * electUnit;
    }

    @FXML
    public void calMeterBtn(ActionEvent actionEvent) {
        if (roomNumberLabel.getText().equals("RoomNumber")) {
            errorLabel.setText("กรุณาเลือกห้อง");
        }
        else if (datePicker.getValue() == null) {
            errorLabel.setText("กรุณาใส่วันที่");
        }
        else if (waterMeterTextField.getText().isEmpty()) {
            errorLabel.setText("กรุณากรอกมิเตอร์น้ำเดือนปัจจุบัน");
        }
        else if (electMeterTextField.getText().isEmpty()) {
            errorLabel.setText("กรุณากรอกมิเตอร์ไฟเดือนปัจจุบัน");
        }
        else {
            try {
                int thisElect = Integer.parseInt(electMeterTextField.getText());
                int thisWater = Integer.parseInt(waterMeterTextField.getText());
                int electUnit = 7;
                int waterUnit = 25;
                int oldElect = Integer.parseInt(lastMonthElectLabel.getText());
                int oldWater = Integer.parseInt(lastMonthWaterLabel.getText());
                float calWater = calWater(oldWater,thisWater,waterUnit);
                float calElect = calElect(oldElect,thisElect,electUnit);
                int calElectUnit = thisElect - oldElect;
                int calWaterUnit = thisWater - oldWater;
                waterBillLabel.setText(Float.toString(calWater));
                electBillLabel.setText(Float.toString(calElect));
                electUnitLabel.setText(Integer.toString(calElectUnit));
                waterUnitLabel.setText(Integer.toString(calWaterUnit));

                addCalMeterToDB(Integer.parseInt(roomNumberLabel.getText()), datePicker.getValue(), lastMonthWaterLabel.getText(), waterMeterTextField.getText(), lastMonthElectLabel.getText(), electMeterTextField.getText(), waterUnit, electUnit , calWater, calElect, calWaterUnit, calElectUnit);

                calculateSuccessfulPane.setOpacity(1);
                calculateSuccessfulPane.setDisable(false);
                effect.fadeInPage(calculateSuccessfulPane);
                errorLabel.setText("");
            } catch (NumberFormatException e) {
                errorLabel.setText("กรุณาใส่ตัวเลข");
            }
        }
        effect.fadeOutLabelEffect(errorLabel,3);
    }

    public void datePicker(ActionEvent actionEvent) {
        datePicker.getValue();
    }

    public void addCalMeterToDB(int roomNum, LocalDate date, String oldWater, String thisWater, String oldElect, String thisElect, int waterUnit, int electUnit, float calWater, float calElect, int calWaterUnit, int calElectUnit) {

        try {
            Connection con = DBConnector.getConnection();
            Statement statement = con.createStatement();
            String sql = "INSERT INTO มิเตอร์ (เลขที่ห้องเช่า, วัน_เดือน_ปีที่จด, มิเตอร์น้ำเดือนเก่า, มิเตอร์น้ำเดือนปัจจุบัน, มิเตอร์ไฟเดือนเก่า, มิเตอร์ไฟเดือนปัจจุบัน, ราคาต่อหน่วยน้ำ, ราคาต่อหน่วยไฟ, ค่าน้ำ, ค่าไฟ, หน่วยน้ำ, หน่วยไฟ) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, roomNum);
            preparedStatement.setDate(2, Date.valueOf(date));
            preparedStatement.setString(3, oldWater);
            preparedStatement.setString(4, thisWater);
            preparedStatement.setString(5, oldElect);
            preparedStatement.setString(6, thisElect);
            preparedStatement.setInt(7, waterUnit);
            preparedStatement.setInt(8, electUnit);
            preparedStatement.setFloat(9, calWater);
            preparedStatement.setFloat(10, calElect);
            preparedStatement.setInt(11, calWaterUnit);
            preparedStatement.setInt(12, calElectUnit);

            int addedRows = preparedStatement.executeUpdate();
            if (addedRows > 0) {
                meter = new Meter(roomNum,date,oldWater,thisWater,oldElect,thisElect,waterUnit,electUnit,calWater,calElect,calWaterUnit,calElectUnit);
            }
            statement.close();
            con.close();


        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public void handleCloseCalculateSuccess() {
        calculateSuccessfulPane.setOpacity(0);
        calculateSuccessfulPane.setDisable(true);
    }

    @FXML
    public void clickToBack(Event event) {
        try {
            FXRouter.goTo ( "Home" );
        } catch (IOException e) {
            System.err.println ( "ไปที่หน้า Home ไม่ด้" );
        }
    }

    public String dateString(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YYYY");
        return date.format(formatter);
    }
}