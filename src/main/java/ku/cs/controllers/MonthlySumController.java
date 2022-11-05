package ku.cs.controllers;

import com.github.saacsos.FXRouter;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import ku.cs.models.MonthlySum;
import ku.cs.services.DBConnector;
import ku.cs.services.Effect;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.format.DateTimeFormatter;

public class MonthlySumController {

    @FXML
    private Label ElectPriceAmount;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Label electUnitAmountLabel;

    @FXML
    private Label roomPriceAmount;

    @FXML
    private Label roomTotalAmount;

    @FXML
    private Label roomUnitAmountLabel;

    @FXML
    private Label totalOwed;

    @FXML
    private Label totalPayment;

    @FXML
    private Label waterPriceAmount;

    @FXML
    private Label waterUnitAmountLabel;

    @FXML
    private Label errorLabel;

    @FXML
    private TextField dateSearchTextField;

    @FXML
    private Label monthlySumDate;

    private Effect effect;

    private int roomCounter;
    private float roomPriceCounter;
    private int electUnitCounter;
    private int waterUnitCounter;
    private float waterPriceCounter;
    private float electPriceCounter;
    private float roomTotalCounter;
    private float paymentCounter;
    private float owedCounter;

    private MonthlySum monthlySum;

    public void initialize() {
        effect = new Effect();
        showSumData();
    }

    public void showSumData() {
        try {
            Connection con = DBConnector.getConnection();
            Statement statement = con.createStatement();
            Statement statement1 = con.createStatement();
            Statement statement2 = con.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT วัน_เดือน_ปีที่ออกใบแจ้งหนี้,ยอดเงินสุทธิ,ยอดเงินที่ชำระ FROM ใบแจ้งหนี้ WHERE (เลขที่ห้องเช่า,วัน_เดือน_ปีที่ออกใบแจ้งหนี้) IN ( SELECT เลขที่ห้องเช่า, MAX(วัน_เดือน_ปีที่ออกใบแจ้งหนี้) FROM ใบแจ้งหนี้ WHERE สถานะการชำระเงิน BETWEEN 1 AND 2 GROUP BY เลขที่ห้องเช่า);");
            ResultSet resultSet1 = statement1.executeQuery("SELECT เลขที่ห้องเช่า,ยอดค้างชำระ FROM ลูกค้า");
            ResultSet resultSet2 = statement2.executeQuery("SELECT ค่าน้ำ,ค่าไฟ,หน่วยน้ำ,หน่วยไฟ FROM มิเตอร์ WHERE (เลขที่ห้องเช่า,วัน_เดือน_ปีที่จด) IN ( SELECT เลขที่ห้องเช่า, MAX(วัน_เดือน_ปีที่จด) FROM มิเตอร์ GROUP BY เลขที่ห้องเช่า);");
            while (resultSet1.next()) {
                resultSet1.getString("เลขที่ห้องเช่า");
                roomCounter++;
            }
            while (resultSet2.next()) {
                waterUnitCounter = waterUnitCounter + resultSet2.getInt("หน่วยน้ำ");
                electUnitCounter = electUnitCounter + resultSet2.getInt("หน่วยไฟ");
                waterPriceCounter = waterPriceCounter + resultSet2.getFloat("ค่าน้ำ");
                electPriceCounter = electPriceCounter + resultSet2.getFloat("ค่าไฟ");
            }
            while (resultSet.next()) {
                roomTotalCounter = roomTotalCounter + resultSet.getFloat("ยอดเงินสุทธิ");
                paymentCounter = paymentCounter + resultSet.getFloat("ยอดเงินที่ชำระ");
                owedCounter = owedCounter + (resultSet.getFloat("ยอดเงินสุทธิ") - resultSet.getFloat("ยอดเงินที่ชำระ"));
                monthlySumDate.setText(splitDate(resultSet.getString("วัน_เดือน_ปีที่ออกใบแจ้งหนี้")));
            }
            roomPriceCounter = 2000 * roomCounter;
            ElectPriceAmount.setText(String.format("%.2f", electPriceCounter));
            waterPriceAmount.setText(String.format("%.2f", waterPriceCounter));
            electUnitAmountLabel.setText(String.valueOf(electUnitCounter));
            waterUnitAmountLabel.setText(String.valueOf(waterUnitCounter));
            roomPriceAmount.setText(String.format("%.2f", roomPriceCounter));
            roomUnitAmountLabel.setText(String.valueOf(roomCounter));
            totalOwed.setText(String.format("%.2f", owedCounter));
            totalPayment.setText(String.format("%.2f", paymentCounter));
            roomTotalAmount.setText(String.format("%.2f", roomTotalCounter));
            statement.close();
            statement1.close();
            statement2.close();
            con.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void printBtn(ActionEvent actionEvent) {
        if (datePicker.getValue() == null) {
            errorLabel.setText("กรุณาใส่วันที่");
        }
        else {
            monthlySum = new MonthlySum(datePicker.getValue(),roomCounter,waterUnitCounter,electUnitCounter,roomPriceCounter,waterPriceCounter,electPriceCounter,roomTotalCounter,paymentCounter,owedCounter);
            try {
                FXRouter.goTo("MonthlySumPrint", monthlySum);
            } catch (IOException e) {
                System.err.println("ไปที่หน้า login_detail ไม่ได้");
                System.err.println("ให้ตรวจสอบการกำหนด route");
            }
        }
        effect.fadeOutLabelEffect(errorLabel,3);
    }

    @FXML
    public void clickToBack(Event event) {
        try {
            FXRouter.goTo ( "Home" );
        } catch (IOException e) {
            System.err.println ( "ไปที่หน้า Home ไม่ด้" );
        }
    }

    public void dateSearchBtn(ActionEvent actionEvent) {
        roomCounter = 0;
        waterUnitCounter = 0;
        electUnitCounter = 0;
        waterPriceCounter = 0;
        electPriceCounter = 0;
        roomTotalCounter = 0;
        paymentCounter = 0;
        owedCounter = 0;
        if (dateSearchTextField.getText().isEmpty()) {
            errorLabel.setText("กรุณาใส่ข้อมูลให้ถูกต้อง");
        }
        else {
            try {
                Connection con = DBConnector.getConnection();
                Statement statement = con.createStatement();
                Statement statement1 = con.createStatement();
                Statement statement2 = con.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT ยอดเงินสุทธิ,ยอดเงินที่ชำระ FROM ใบแจ้งหนี้ WHERE วัน_เดือน_ปีที่ออกใบแจ้งหนี้ = '"+convertDate(dateSearchTextField.getText())+"25'");
                ResultSet resultSet1 = statement1.executeQuery("SELECT เลขที่ห้องเช่า FROM ลูกค้า");
                ResultSet resultSet2 = statement2.executeQuery("SELECT ค่าน้ำ,ค่าไฟ,หน่วยน้ำ,หน่วยไฟ FROM มิเตอร์ WHERE วัน_เดือน_ปีที่จด = '" +convertDate(dateSearchTextField.getText()) + "-20'");
                while (resultSet1.next()) {
                    resultSet1.getString("เลขที่ห้องเช่า");
                    roomCounter++;
                }
                while (resultSet2.next()) {
                    waterUnitCounter = waterUnitCounter + resultSet2.getInt("หน่วยน้ำ");
                    electUnitCounter = electUnitCounter + resultSet2.getInt("หน่วยไฟ");
                    waterPriceCounter = waterPriceCounter + resultSet2.getFloat("ค่าน้ำ");
                    electPriceCounter = electPriceCounter + resultSet2.getFloat("ค่าไฟ");
                }
                while (resultSet.next()) {
                    roomTotalCounter = roomTotalCounter + resultSet.getFloat("ยอดเงินสุทธิ");
                    paymentCounter = paymentCounter + resultSet.getFloat("ยอดเงินที่ชำระ");
                    owedCounter = owedCounter + (resultSet.getFloat("ยอดเงินสุทธิ") - resultSet.getFloat("ยอดเงินที่ชำระ"));

                }
                roomPriceCounter = 2000 * roomCounter;
                ElectPriceAmount.setText(String.format("%.2f", electPriceCounter));
                waterPriceAmount.setText(String.format("%.2f", waterPriceCounter));
                electUnitAmountLabel.setText(String.valueOf(electUnitCounter));
                waterUnitAmountLabel.setText(String.valueOf(waterUnitCounter));
                roomPriceAmount.setText(String.format("%.2f", roomPriceCounter));
                roomUnitAmountLabel.setText(String.valueOf(roomCounter));
                totalOwed.setText(String.format("%.2f", owedCounter));
                totalPayment.setText(String.format("%.2f", paymentCounter));
                roomTotalAmount.setText(String.format("%.2f", roomTotalCounter));
                monthlySumDate.setText(dateSearchTextField.getText());
                statement.close();
                statement1.close();
                statement2.close();
                con.close();

            } catch (Exception e) {
                System.out.println(e);
            }
        }
        effect.fadeOutLabelEffect(errorLabel,3);
    }

    public String splitDate(String date){
        String[] splDate = date.split("-",3);
        return splDate[1]+"/"+splDate[0];
    }

    public String convertDate(String date) {
        String[] splDate = date.split("/",2);
        return splDate[1]+"-"+splDate[0];
    }

}
