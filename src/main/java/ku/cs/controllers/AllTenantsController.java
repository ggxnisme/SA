package ku.cs.controllers;

import com.github.saacsos.FXRouter;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import ku.cs.services.DBConnector;
import ku.cs.services.Effect;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class AllTenantsController {

    @FXML
    private Label addressTextField;

    @FXML
    private Label depositTextField;

    @FXML
    private Label errorLabel;

    @FXML
    private Label idTextField;

    @FXML
    private Label nameTextField;

    @FXML
    private Label owedLabel;

    @FXML
    private Label phoneTextField;

    @FXML
    private Label rentTextField;

    @FXML
    private ListView<String> roomListView;

    @FXML
    private MenuButton roomNumMenuBtn;

    @FXML
    private Pane saveSuccessfulPane;

    private String roomNumber;

    private String room;

    private MenuItem item;

    private Effect effect;

    public void initialize() {
        showListView();
        handleSelectedListView();
        showRoomMenu();
        effect = new Effect();
        saveSuccessfulPane.setDisable(true);
        saveSuccessfulPane.setOpacity(0);
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
                        room = t1;
                    }
                }
        );
    }

    public void showRoomMenu() {
        try {
            Connection con = DBConnector.getConnection();
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT เลขที่ห้องเช่า FROM ห้องเช่า WHERE สถานะการเข้าอยู่ = 'ว่าง'");
            while (resultSet.next()) {
                String เลขที่ห้องเช่า = resultSet.getString(1);
                String listOut = เลขที่ห้องเช่า;
                item = new MenuItem(listOut);
                roomNumMenuBtn.getItems().add(item);
                item.setOnAction((ActionEvent e)-> {
                    roomNumPick(listOut);
                    roomNumMenuBtn.setText(listOut);
                });

            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void roomNumPick(String roomNum) {
        this.roomNumber = roomNum;
    }
    public void showDataFromDB(String t1) {

        try {
            Connection connection = DBConnector.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM ลูกค้า WHERE เลขที่ห้องเช่า = "+t1+";");
            while (resultSet.next()) {
                nameTextField.setText(resultSet.getString("ชื่อ_นามสกุล"));
                idTextField.setText(resultSet.getString("เลขบัตรประชาชน"));
                addressTextField.setText(resultSet.getString("ที่อยู่"));
                rentTextField.setText(resultSet.getString("ระยะเวลาเช่า"));
                roomNumMenuBtn.setText(resultSet.getString("เลขที่ห้องเช่า"));
                depositTextField.setText(String.format("%,.2f",resultSet.getFloat("เงินประกัน")));
                owedLabel.setText(String.format("%,.2f",resultSet.getFloat("ยอดค้างชำระ")));
                phoneTextField.setText(resultSet.getString("เบอร์โทรศัพท์"));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void saveBtn(ActionEvent actionEvent) {
        try {
            int roomNum = Integer.parseInt(roomNumber);
            updateCustomerToDB(roomNum);
            roomListView.getItems().clear();
            roomNumMenuBtn.getItems().clear();
            showListView();
            showRoomMenu();
            saveSuccessfulPane.setOpacity(1);
            saveSuccessfulPane.setDisable(false);
            errorLabel.setText("");
        } catch (NumberFormatException e) {
            System.out.println(e);
            errorLabel.setText("กรุณาเลือกห้อง");
        }
        effect.fadeOutLabelEffect(errorLabel,3);
    }

    public void updateCustomerToDB(int roomNum) {
        try {
            Connection connection = DBConnector.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE ลูกค้า SET เลขที่ห้องเช่า = ? WHERE เลขที่ห้องเช่า = "+room);
            PreparedStatement preparedStatement1 = connection.prepareStatement("UPDATE ห้องเช่า SET สถานะการเข้าอยู่ = ? WHERE เลขที่ห้องเช่า = "+room);
            PreparedStatement preparedStatement2 = connection.prepareStatement("UPDATE ห้องเช่า SET สถานะการเข้าอยู่ = ? WHERE เลขที่ห้องเช่า = "+roomNumber);
            preparedStatement.setInt(1,roomNum);
            preparedStatement1.setString(1,"ว่าง");
            preparedStatement2.setString(1, "ไม่ว่าง");
            preparedStatement.executeUpdate();
            preparedStatement1.executeUpdate();
            preparedStatement2.executeUpdate();
            preparedStatement.close();
            preparedStatement1.close();
            preparedStatement2.close();
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

    @FXML
    public void handleCloseCalculateSuccess() {
        saveSuccessfulPane.setOpacity(0);
        saveSuccessfulPane.setDisable(true);
    }
}
