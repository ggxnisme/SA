package ku.cs.controllers;

import com.github.saacsos.FXRouter;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import ku.cs.services.DBConnector;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class AllTenantsController {

    @FXML
    private Label Owed;

    @FXML
    private Label addressLabel;

    @FXML
    private Label depositLabel;

    @FXML
    private Label idLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private Label phoneLabel;

    @FXML
    private Label rentalPeriodLabel;

    @FXML
    private ListView<String> roomListView;

    @FXML
    private Label roomNumLabel;

    public void initialize() {
        showListView();
        handleSelectedListView();
    }
    public void showListView() {

        try {
            Connection connection = DBConnector.getConnection();
            Statement statement = connection.createStatement();
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
            Connection connection = DBConnector.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * FROM ลูกค้า WHERE เลขที่ห้องเช่า = "+t1+";");
            while (resultSet.next()) {
                roomNumLabel.setText(resultSet.getString("เลขที่ห้องเช่า"));
                nameLabel.setText(resultSet.getString("ชื่อ_นามสกุล"));
                rentalPeriodLabel.setText(resultSet.getString("ระยะเวลาเช่า"));
                idLabel.setText(resultSet.getString("เลขบัตรประชาชน"));
                depositLabel.setText(String.format("%.2f",resultSet.getFloat("เงินประกัน")));
                phoneLabel.setText(resultSet.getString("เบอร์โทรศัพท์"));
                addressLabel.setText(resultSet.getString("ที่อยู่ตามทะเบียนบ้าน"));
                Owed.setText(String.format("%.2f",resultSet.getFloat("ยอดค้างชำระ")));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @FXML
    public void clickToBack(Event event) {
        try {
            FXRouter.goTo ( "Home" );
        } catch (IOException e) {
            System.err.println ( "ไปที่หน้า Home ไม่ด้" );
        }
    }
}
