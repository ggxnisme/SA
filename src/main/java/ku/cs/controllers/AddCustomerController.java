package ku.cs.controllers;

import com.github.saacsos.FXRouter;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import ku.cs.models.Customer;
import ku.cs.services.DBConnector;
import ku.cs.services.Effect;

import java.io.IOException;
import java.sql.*;

public class AddCustomerController {

    private Customer customer;

    @FXML
    private TextArea addressTextField;

    @FXML
    private TextField idTextField;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField depositTextField;

    @FXML
    private TextField phoneTextField;

    @FXML
    private TextField rentTextField;
    @FXML
    private MenuButton roomMenu;

    @FXML
    private MenuItem item;
    @FXML
    private Label errorLabel;

    private Effect effect;

    private String roomNumber;
    public void initialize() {
        effect = new Effect();
        showRoomMenu();
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
                roomMenu.getItems().add(item);
                item.setOnAction((ActionEvent e)-> {
                    roomNumPick(listOut);
                    roomMenu.setText(listOut);
                });

            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void roomNumPick(String roomNum) {
        this.roomNumber = roomNum;
    }

    public void clickToSave(ActionEvent actionEvent) {

        if (idTextField.getText().isEmpty() || roomNumber == null || nameTextField.getText().isEmpty() || addressTextField.getText().isEmpty() || phoneTextField.getText().isEmpty() || rentTextField.getText().isEmpty() || depositTextField.getText().isEmpty()) {
            errorLabel.setText("กรุณาใส่ข้อมูลให้ครบถ้วน");
        }
        else if ((idTextField.getText().length() != 13)) {
            errorLabel.setText("กรุณาใส่เลขบัตรประชาชนให้ครบถ้วน");
        }
        else if ((phoneTextField.getText().length() != 10)) {
            errorLabel.setText("กรุณาใส่เบอร์โทรศัพท์ให้ครบถ้วน");
        }
        else if (Integer.parseInt(depositTextField.getText()) < 2000) {
            errorLabel.setText("กรุณาใส่ข้อมูลให้ถูกต้อง");
        }
        else if (Integer.parseInt(rentTextField.getText()) < 0) {
            errorLabel.setText("กรุณาใส่ข้อมูลให้ถูกต้อง");
        }
        else {
            try {
                String id = idTextField.getText();
                int roomNum = Integer.parseInt(roomNumber);
                String name = nameTextField.getText();
                String address = addressTextField.getText();
                String phone = phoneTextField.getText();
                String rent = rentTextField.getText();
                int deposit = Integer.parseInt(depositTextField.getText());
                addCustomerToDB(roomNum, id, name, address, phone, rent, deposit);
                errorLabel.setText("");
                try {
                    FXRouter.goTo("Tenants", customer);
                } catch (IOException e) {
                    System.err.println("ไปที่หน้า login_detail ไม่ได้");
                    System.err.println("ให้ตรวจสอบการกำหนด route");
                }
            } catch (NumberFormatException e) {
                errorLabel.setText("กรุณาใส่ข้อมูลให้ถูกต้อง");
            }
        }
        effect.fadeOutLabelEffect(errorLabel,3);
    }

    public void addCustomerToDB(int roomNum, String id, String name, String address, String phone, String rent, int deposit) {

        try {
            Connection con = DBConnector.getConnection();
            String sql = "INSERT INTO ลูกค้า (เลขที่ห้องเช่า, เลขบัตรประชาชน, ชื่อ_นามสกุล, ที่อยู่, เบอร์โทรศัพท์, ระยะเวลาเช่า, เงินประกัน, ยอดค้างชำระ) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, roomNum);
            preparedStatement.setString(2, id);
            preparedStatement.setString(3, name);
            preparedStatement.setString(4, address);
            preparedStatement.setString(5, phone);
            preparedStatement.setString(6, rent);
            preparedStatement.setInt(7, deposit);
            preparedStatement.setInt(8, 0);
            int addedRows = preparedStatement.executeUpdate();
            if (addedRows > 0) {
                customer = new Customer(roomNum, id, name, address, phone, rent, deposit);
            }
            PreparedStatement preparedStatement1 = con.prepareStatement("UPDATE ห้องเช่า SET สถานะการเข้าอยู่ = ? WHERE เลขที่ห้องเช่า = "+roomNumber+";");
            preparedStatement1.setString(1, "ไม่ว่าง");
            preparedStatement1.executeUpdate();
            clear();
            preparedStatement.close();
            preparedStatement1.close();
            con.close();


        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void clear() {
        idTextField.clear();
        nameTextField.clear();
        addressTextField.clear();
        phoneTextField.clear();
        rentTextField.clear();
        depositTextField.clear();
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
