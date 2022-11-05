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
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class InvoiceController {

    @FXML
    private Label ElectPriceTotalText;

    @FXML
    private Label ElectUnit;

    @FXML
    private Label InvoiceNumText;

    @FXML
    private Label RoomText;

    @FXML
    private Label invoiceDateText;

    @FXML
    private Label nameText;

    @FXML
    private Label owedText;

    @FXML
    private Label owedTotalText;

    @FXML
    private Label roomPriceText;

    @FXML
    private Label roomPriceTotalText;

    @FXML
    private Label totalPriceText;

    @FXML
    private Label waterPriceTotalText;

    @FXML
    private Label waterUnitText;

    @FXML
    private Label numTextLabel;

    private Invoice invoice = (Invoice) FXRouter.getData();

    public void initialize() {
        showInvoiceDetail();
    }

    public void showInvoiceDetail() {

        try {
            Connection con = DBConnector.getConnection();
            Statement statement = con.createStatement();
            Statement statement1 = con.createStatement();
            Statement statement2 = con.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT เลขที่ใบแจ้งหนี้,วัน_เดือน_ปีที่ออกใบแจ้งหนี้,เลขที่ห้องเช่า,ค่าห้อง FROM ใบแจ้งหนี้ WHERE (เลขที่ห้องเช่า,วัน_เดือน_ปีที่ออกใบแจ้งหนี้) IN (SELECT เลขที่ห้องเช่า, MAX(วัน_เดือน_ปีที่ออกใบแจ้งหนี้) FROM ใบแจ้งหนี้ WHERE เลขที่ห้องเช่า = "+invoice.getRoomNum()+");");
            ResultSet resultSet1 = statement1.executeQuery("SELECT ชื่อ_นามสกุล,ยอดค้างชำระ FROM ลูกค้า WHERE เลขที่ห้องเช่า = "+invoice.getRoomNum());
            ResultSet resultSet2 = statement2.executeQuery("SELECT มิเตอร์น้ำเดือนปัจจุบัน,มิเตอร์ไฟเดือนปัจจุบัน,ค่าไฟ,ค่าน้ำ FROM มิเตอร์ WHERE (เลขที่ห้องเช่า,วัน_เดือน_ปีที่จด) IN ( SELECT เลขที่ห้องเช่า, MAX(วัน_เดือน_ปีที่จด) FROM มิเตอร์ WHERE เลขที่ห้องเช่า = "+invoice.getRoomNum()+");");

            while (resultSet.next() & resultSet1.next() & resultSet2.next()){
                RoomText.setText(resultSet.getString("เลขที่ห้องเช่า"));
                InvoiceNumText.setText(resultSet.getString("เลขที่ใบแจ้งหนี้"));
                invoiceDateText.setText(dateString(resultSet.getDate("วัน_เดือน_ปีที่ออกใบแจ้งหนี้").toLocalDate()));
                nameText.setText(resultSet1.getString("ชื่อ_นามสกุล"));
                waterUnitText.setText(resultSet2.getString("มิเตอร์น้ำเดือนปัจจุบัน"));
                ElectUnit.setText(String.format("%.2f", resultSet2.getFloat("มิเตอร์ไฟเดือนปัจจุบัน")));
                ElectPriceTotalText.setText(String.format("%.2f",resultSet2.getFloat("ค่าไฟ")));
                waterPriceTotalText.setText(String.format("%.2f", resultSet2.getFloat("ค่าน้ำ")));
                roomPriceText.setText(String.format("%.2f",resultSet.getFloat("ค่าห้อง")));
                roomPriceTotalText.setText(String.format("%.2f",resultSet.getFloat("ค่าห้อง")));
                owedText.setText(String.format("%.2f",resultSet1.getFloat("ยอดค้างชำระ")));
                owedTotalText.setText(String.format("%.2f",resultSet1.getFloat("ยอดค้างชำระ")));
                totalPriceText.setText(String.format("%.2f",resultSet2.getFloat("ค่าไฟ") + resultSet2.getFloat("ค่าน้ำ") + resultSet.getFloat("ค่าห้อง") + resultSet1.getFloat("ยอดค้างชำระ")));
                numTextLabel.setText(new NumberMap().getText(Float.toString(resultSet2.getFloat("ค่าไฟ") + resultSet2.getFloat("ค่าน้ำ") + resultSet.getFloat("ค่าห้อง") + resultSet1.getFloat("ยอดค้างชำระ"))));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void cancelBtn(ActionEvent actionEvent) {
        try {
            FXRouter.goTo("CreateInvoice");
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
