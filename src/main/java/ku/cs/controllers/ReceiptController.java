package ku.cs.controllers;

import com.github.saacsos.FXRouter;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import ku.cs.models.Invoice;
import ku.cs.services.DBConnector;
import ku.cs.services.Effect;

import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.concurrent.Callable;

public class ReceiptController {

    @FXML
    private ListView<String> roomNumList;

    @FXML
    private ListView<String> statusList;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Label errorLabel;

    private Invoice invoice;

    private String roomNum;

    private Effect effect;

    public void initialize() {
        effect = new Effect();
        showDataONTable();
        handleSelectedListView();
    }

    public void showDataONTable()  {
        try {
            Connection connection = DBConnector.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT เลขที่ห้องเช่า,สถานะการชำระเงิน FROM ใบแจ้งหนี้ WHERE (เลขที่ห้องเช่า,วัน_เดือน_ปีที่ออกใบแจ้งหนี้) IN ( SELECT เลขที่ห้องเช่า, MAX(วัน_เดือน_ปีที่ออกใบแจ้งหนี้) FROM ใบแจ้งหนี้ WHERE สถานะการชำระเงิน BETWEEN 1 AND 2 GROUP BY เลขที่ห้องเช่า);");
            while (resultSet.next()) {
                String เลขที่ห้องเช่า = resultSet.getString(1);
                String listOut = เลขที่ห้องเช่า;
                String สถานะการชำระเงิน = callStatus(resultSet.getString(2));
                String listOut1 = สถานะการชำระเงิน;
                roomNumList.getItems().add(listOut);
                statusList.getItems().add(listOut1);
                statusList.setMouseTransparent(true);
                statusList.setFocusTraversable(false);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void handleSelectedListView() {
        roomNumList.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                        keepDataToCreateReceipt(t1);
                        roomNum = t1;
                    }
                }
        );
    }

    public void keepDataToCreateReceipt(String roomNum) {
        try {
            Connection connection = DBConnector.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM ใบแจ้งหนี้ WHERE (เลขที่ห้องเช่า,วัน_เดือน_ปีที่ออกใบแจ้งหนี้) IN ( SELECT เลขที่ห้องเช่า, MAX(วัน_เดือน_ปีที่ออกใบแจ้งหนี้) FROM ใบแจ้งหนี้ WHERE เลขที่ห้องเช่า = "+roomNum+");");
            while (resultSet.next()) {
                invoice = new Invoice(resultSet.getInt("เลขที่ห้องเช่า"),resultSet.getLong("เลขที่ใบแจ้งหนี้"),resultSet.getDate("วัน_เดือน_ปีที่ออกใบแจ้งหนี้").toLocalDate(),resultSet.getFloat("ค่าห้อง"),resultSet.getFloat("ยอดเงินสุทธิ"),resultSet.getFloat("ยอดเงินที่ชำระ"),resultSet.getInt("สถานะการชำระเงิน"));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public String callStatus (String status) {
        if (status.equals("1")) {
            return "ชำระเงินเต็มจำนวน";
        }
        return "ชำระเงินไม่เต็มจำนวน";
    }

    public void printReceiptBtn(ActionEvent actionEvent) {
        if (roomNum == null) {
            errorLabel.setText("กรุณาเลือกห้อง");
        }
        else if (datePicker.getValue() == null) {
            errorLabel.setText("กรุณาเลือกวันที่");
        }
        else {
            addReceiptDate(datePicker.getValue());
            errorLabel.setText("");
        }
        effect.fadeOutLabelEffect(errorLabel,3);
    }

    public void addReceiptDate(LocalDate date) {
        try {
            Connection connection = DBConnector.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE ใบแจ้งหนี้ SET วัน_เดือน_ปีที่ออกใบเสร็จ = ? WHERE (เลขที่ห้องเช่า,วัน_เดือน_ปีที่ออกใบแจ้งหนี้) IN ( SELECT เลขที่ห้องเช่า, MAX(วัน_เดือน_ปีที่ออกใบแจ้งหนี้) FROM ใบแจ้งหนี้ WHERE เลขที่ห้องเช่า = "+roomNum+");");
            preparedStatement.setDate(1, Date.valueOf(date));
            preparedStatement.executeUpdate();
            try {
                FXRouter.goTo("ReceiptPrint", invoice);
            } catch (IOException e) {
                System.err.println("ไปที่หน้า login_detail ไม่ได้");
                System.err.println("ให้ตรวจสอบการกำหนด route");
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

    public String dateString(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        return formatter.format(date);
    }

}
