package ku.cs.controllers;

import com.github.saacsos.FXRouter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import ku.cs.models.MonthlySum;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MonthlySumPrintController {

    @FXML
    private Label ElectUnitAmountLabel;

    @FXML
    private Label electPriceAmountLabel;

    @FXML
    private Label monthlySumDateLabel;

    @FXML
    private Label roomPriceAmountLabel;

    @FXML
    private Label roomTotalAmountLabel;

    @FXML
    private Label roomUnitAmountLabel;

    @FXML
    private Label totalOwedLabel;

    @FXML
    private Label totalPaymentLabel1;

    @FXML
    private Label waterPriceAmountLabel;

    @FXML
    private Label waterUnitAmountLabel;

    private MonthlySum monthlySum = (MonthlySum) FXRouter.getData();

    public void initialize() {
        showData();

    }

    public void showData() {
        monthlySumDateLabel.setText(dateString(monthlySum.getDate()));
        roomUnitAmountLabel.setText(String.valueOf(monthlySum.getRoomUnitAmount()));
        roomPriceAmountLabel.setText(String.format("%.2f",monthlySum.getRoomPriceAmount()));
        waterUnitAmountLabel.setText(String.valueOf(monthlySum.getWaterUnitAmount()));
        ElectUnitAmountLabel.setText(String.valueOf(monthlySum.getElectUnitAmount()));
        waterPriceAmountLabel.setText(String.format("%.2f",monthlySum.getWaterPriceAmount()));
        electPriceAmountLabel.setText(String.format("%.2f",monthlySum.getElectPriceAmount()));
        roomTotalAmountLabel.setText(String.format("%.2f",monthlySum.getTotalAmount()));
        totalPaymentLabel1.setText(String.format("%.2f",monthlySum.getPaidAmount()));
        totalOwedLabel.setText(String.format("%.2f",monthlySum.getOwedAmount()));
    }
    public void cancelBtn(ActionEvent actionEvent) {
        try {
            FXRouter.goTo("MonthlySum");
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
