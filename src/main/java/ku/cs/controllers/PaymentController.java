package ku.cs.controllers;

import com.github.saacsos.FXRouter;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import ku.cs.services.DBConnector;
import ku.cs.services.Effect;

import java.io.IOException;
import java.sql.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.concurrent.Callable;

public class PaymentController {

    @FXML
    private Label balanceLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private Label totalPriceLabel;

    @FXML
    private Label paidLabel;

    @FXML
    private TextField paidTextField;

    @FXML
    private Label roomNumberLabel;

    @FXML
    private TextField roomTextField;

    @FXML
    private Pane saveSuccessfulPane;

    @FXML
    private Label errorLabel;

    private float totalPrice;

    private ArrayList<String> room;

    private float lastPaid;

    private Effect effect;

    private float paid;

    public void initialize() {
        effect = new Effect();
        room = new ArrayList<>();
        saveSuccessfulPane.setDisable(true);
        saveSuccessfulPane.setOpacity(0);
    }

    public void roomSearchBtn(ActionEvent actionEvent) {
        try {
            Connection connection = DBConnector.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT เลขที่ห้องเช่า FROM ลูกค้า");
            while (resultSet.next()) {
                room.add(resultSet.getString("เลขที่ห้องเช่า"));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        if (roomTextField.getText().isEmpty()) {
            errorLabel.setText("กรุณาใส่เลขห้อง");
        }
        else if (!room.contains(roomTextField.getText())) {
            errorLabel.setText("ไม่มีเลขห้องนี้ในระบบ");
        }
        else {
            try {
                Integer.parseInt(roomTextField.getText());
                try {
                    Connection con = DBConnector.getConnection();
                    Statement statement = con.createStatement();
                    Statement statement1 = con.createStatement();
                    ResultSet resultSet = statement.executeQuery("SELECT เลขที่ห้องเช่า,ยอดเงินสุทธิ,ยอดเงินที่ชำระ,สถานะการชำระเงิน FROM ใบแจ้งหนี้ WHERE (เลขที่ห้องเช่า,วัน_เดือน_ปีที่ออกใบแจ้งหนี้) IN (SELECT เลขที่ห้องเช่า, MAX(วัน_เดือน_ปีที่ออกใบแจ้งหนี้) FROM ใบแจ้งหนี้ WHERE เลขที่ห้องเช่า = "+roomTextField.getText()+");");
                    ResultSet resultSet1 = statement1.executeQuery("SELECT ชื่อ_นามสกุล,ยอดค้างชำระ FROM ลูกค้า WHERE เลขที่ห้องเช่า = "+roomTextField.getText()+";");
                    while (resultSet.next() && resultSet1.next()) {
                        roomNumberLabel.setText(resultSet.getString("เลขที่ห้องเช่า"));
                        nameLabel.setText(resultSet1.getString("ชื่อ_นามสกุล"));
                        lastPaid = resultSet.getFloat("ยอดเงินที่ชำระ");
                        if (!room.contains(roomTextField.getText())) {
                            errorLabel.setText("ไม่มีเลขห้องนี้ในระบบ");
                        }
                        if (resultSet.getInt("สถานะการชำระเงิน") == 0){
                            totalPriceLabel.setText(String.format("%,.2f", resultSet.getFloat("ยอดเงินสุทธิ")));
                        }
                        else {
                            totalPriceLabel.setText(String.format("%,.2f", resultSet1.getFloat("ยอดค้างชำระ")));
                        }
                        paidLabel.setText("0.00");
                        balanceLabel.setText("0.00");
                        errorLabel.setText("");
                    }
                    statement.close();
                    statement1.close();
                    con.close();
                } catch (Exception e) {
                    System.out.println(e);
                }
            } catch (NumberFormatException e) {
                errorLabel.setText("กรุณาใส่ตัวเลข");
            }
        }
        effect.fadeOutLabelEffect(errorLabel, 3);
    }

    public void saveBtn(ActionEvent actionEvent) {
        try {
            Connection connection = DBConnector.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT ยอดเงินสุทธิ,ยอดเงินที่ชำระ,สถานะการชำระเงิน FROM ใบแจ้งหนี้ WHERE (เลขที่ห้องเช่า,วัน_เดือน_ปีที่ออกใบแจ้งหนี้) IN (SELECT เลขที่ห้องเช่า, MAX(วัน_เดือน_ปีที่ออกใบแจ้งหนี้) FROM ใบแจ้งหนี้ WHERE เลขที่ห้องเช่า = "+roomTextField.getText()+");");
            while (resultSet.next()) {
                totalPrice = resultSet.getFloat("ยอดเงินสุทธิ");
                paid = resultSet.getFloat("ยอดเงินที่ชำระ");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        if (paidTextField.getText().equals(""))
        {
            errorLabel.setText("กรุณาใส่จำนวนเงิน");
        }
        else if (roomNumberLabel.getText().equals("")) {
            errorLabel.setText("กรุณาใส่เลขห้อง");
        }
        if (Float.parseFloat(paidTextField.getText()) > (totalPrice - paid) || Float.parseFloat(paidTextField.getText()) < 0) {
                errorLabel.setText("กรุณาใส่จำนวนเงินให้ถูกต้อง");
        }
        else {
            try {
                Float.parseFloat(paidTextField.getText());
                try {
                    Connection con = DBConnector.getConnection();
                    PreparedStatement preparedStatement = con.prepareStatement("UPDATE ใบแจ้งหนี้ SET ยอดเงินที่ชำระ = ?, สถานะการชำระเงิน = ? WHERE (เลขที่ห้องเช่า,วัน_เดือน_ปีที่ออกใบแจ้งหนี้) IN ( SELECT เลขที่ห้องเช่า, MAX(วัน_เดือน_ปีที่ออกใบแจ้งหนี้) FROM ใบแจ้งหนี้ WHERE เลขที่ห้องเช่า = " + roomTextField.getText() + ");");
                    PreparedStatement preparedStatement1 = con.prepareStatement("UPDATE ลูกค้า SET ยอดค้างชำระ = ? WHERE เลขที่ห้องเช่า = " + roomTextField.getText() + ";");
                    preparedStatement.setFloat(1, Float.parseFloat(paidTextField.getText()) + lastPaid);
                    preparedStatement.setInt(2, checkPaidStatus(Float.parseFloat(paidTextField.getText())));
                    preparedStatement1.setFloat(1, calPaid(Float.parseFloat(paidTextField.getText())));
                    preparedStatement.executeUpdate();
                    preparedStatement1.executeUpdate();
                    paidLabel.setText(String.format("%,.2f", Float.parseFloat(paidTextField.getText())));
                    balanceLabel.setText(String.format("%,.2f",calPaid(Float.parseFloat(paidTextField.getText()))));
                    saveSuccessfulPane.setOpacity(1);
                    saveSuccessfulPane.setDisable(false);
                    errorLabel.setText("");
                } catch (Exception e) {
                    System.out.println(e);
                }
            } catch (NumberFormatException e) {
                errorLabel.setText("กรุณาใส่ข้อมูลให้ถูกต้อง");
            }

        }
        effect.fadeOutLabelEffect(errorLabel, 3);
    }

    public float calPaid(float paid) throws SQLException {
        Connection connection = DBConnector.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT ยอดเงินที่ชำระ,ยอดเงินสุทธิ,สถานะการชำระเงิน FROM ใบแจ้งหนี้ WHERE (เลขที่ห้องเช่า,วัน_เดือน_ปีที่ออกใบแจ้งหนี้) IN (SELECT เลขที่ห้องเช่า, MAX(วัน_เดือน_ปีที่ออกใบแจ้งหนี้) FROM ใบแจ้งหนี้ WHERE เลขที่ห้องเช่า = "+roomTextField.getText()+");");
        while (resultSet.next()) {
            return resultSet.getFloat("ยอดเงินสุทธิ") - (lastPaid + paid);
        }
        return 0;
    }
    public int checkPaidStatus(float paid) throws SQLException {
        Connection connection = DBConnector.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT ยอดเงินสุทธิ FROM ใบแจ้งหนี้ WHERE (เลขที่ห้องเช่า,วัน_เดือน_ปีที่ออกใบแจ้งหนี้) IN (SELECT เลขที่ห้องเช่า, MAX(วัน_เดือน_ปีที่ออกใบแจ้งหนี้) FROM ใบแจ้งหนี้ WHERE เลขที่ห้องเช่า = "+roomTextField.getText()+");");
        while (resultSet.next()) {
            float totalNum = resultSet.getFloat("ยอดเงินสุทธิ");
            if (totalNum == (paid + lastPaid)) {
                return 1;
            }
            else if (totalNum > paid) {
                return 2;
            }
        }

        return 0;
    }

    @FXML
    public void handleCloseCalculateSuccess() {
        saveSuccessfulPane.setOpacity(0);
        saveSuccessfulPane.setDisable(true);
    }

    @FXML
    public void clickToBack(Event event) {
        try {
            FXRouter.goTo ( "Home" );
        } catch (IOException e) {
            System.err.println ( "ไปที่หน้า Home ไม่ด้" );
        }
    }

    public void receiptBtn(ActionEvent actionEvent) {
        try {
            FXRouter.goTo("Receipt");
        } catch (IOException e) {
            System.err.println("ไปที่หน้า login_detail ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
    }
}
