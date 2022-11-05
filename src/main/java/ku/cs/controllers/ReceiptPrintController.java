package ku.cs.controllers;

import com.github.saacsos.FXRouter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import ku.cs.models.Invoice;
import ku.cs.services.DBConnector;
import ku.cs.services.NumberMap;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ReceiptPrintController {

    @FXML
    private Label ElectUnitLabel;

    @FXML
    private Label OwedLabel;

    @FXML
    private Label electPriceLabel;

    @FXML
    private Label invoiceNumberLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private Label owedPriceLabel;

    @FXML
    private Label receiptDateLabel;

    @FXML
    private Label roomNumberLabel;

    @FXML
    private Label roomPriceLabel;

    @FXML
    private Label roomPriceLabel1;

    @FXML
    private Label totalPriceLabel;

    @FXML
    private Label waterPriceLabel;

    @FXML
    private Label waterUnitLabel;

    @FXML
    private Label numTextLabel;

    private Invoice invoice = (Invoice) FXRouter.getData();

    public void initialize() {
        showData();
    }

    public void showData() {

        try {
            Connection connection = DBConnector.getConnection();
            Statement statement = connection.createStatement();
            Statement statement1 = connection.createStatement();
            Statement statement2 = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT เลขที่ห้องเช่า,ชื่อ_นามสกุล,ยอดค้างชำระ FROM ลูกค้า WHERE เลขที่ห้องเช่า = "+invoice.getRoomNum()+";");
            ResultSet resultSet1 = statement1.executeQuery("SELECT ค่าน้ำ,ค่าไฟ,หน่วยน้ำ,หน่วยไฟ FROM มิเตอร์ WHERE (เลขที่ห้องเช่า,วัน_เดือน_ปีที่จด) IN ( SELECT เลขที่ห้องเช่า, MAX(วัน_เดือน_ปีที่จด) FROM มิเตอร์ WHERE เลขที่ห้องเช่า = "+invoice.getRoomNum()+");");
            ResultSet resultSet2 = statement2.executeQuery("SELECT เลขที่ใบแจ้งหนี้,ค่าห้อง,ยอดเงินสุทธิ,วัน_เดือน_ปีที่ออกใบเสร็จ FROM ใบแจ้งหนี้ WHERE (เลขที่ห้องเช่า,วัน_เดือน_ปีที่ออกใบแจ้งหนี้) IN (SELECT เลขที่ห้องเช่า, MAX(วัน_เดือน_ปีที่ออกใบแจ้งหนี้) FROM ใบแจ้งหนี้ WHERE เลขที่ห้องเช่า = "+invoice.getRoomNum()+");");
            while (resultSet.next() & resultSet1.next() & resultSet2.next()) {
                roomNumberLabel.setText(resultSet.getString("เลขที่ห้องเช่า"));
                nameLabel.setText(resultSet.getString("ชื่อ_นามสกุล"));
                ElectUnitLabel.setText(resultSet1.getString("หน่วยไฟ"));
                waterUnitLabel.setText(resultSet1.getString("หน่วยน้ำ"));
                waterPriceLabel.setText(String.format("%.2f",resultSet1.getFloat("ค่าน้ำ")));
                electPriceLabel.setText(String.format("%.2f",resultSet1.getFloat("ค่าไฟ")));
                receiptDateLabel.setText(dateString(resultSet2.getDate("วัน_เดือน_ปีที่ออกใบเสร็จ").toLocalDate()));
                OwedLabel.setText(String.format("%.2f",resultSet.getFloat("ยอดค้างชำระ")));
                owedPriceLabel.setText(String.format("%.2f",resultSet.getFloat("ยอดค้างชำระ")));
                invoiceNumberLabel.setText(resultSet2.getString("เลขที่ใบแจ้งหนี้"));
                roomPriceLabel.setText(String.format("%.2f",resultSet2.getFloat("ค่าห้อง")));
                roomPriceLabel1.setText(String.format("%.2f",resultSet2.getFloat("ค่าห้อง")));
                totalPriceLabel.setText(String.format("%.2f",resultSet2.getFloat("ยอดเงินสุทธิ")));
                numTextLabel.setText(new NumberMap().getText(resultSet2.getString("ยอดเงินสุทธิ")));
            }


        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void cancelBtn(ActionEvent actionEvent) {
        try {
            FXRouter.goTo("Receipt");
        } catch (IOException e) {
            System.err.println("ไปที่หน้า login_detail ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
    }

    public String dateString(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YYYY");
        return date.format(formatter);
    }
}
