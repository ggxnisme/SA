package ku.cs.controllers;

import com.github.saacsos.FXRouter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import ku.cs.models.Customer;

import java.io.IOException;

public class TenantsController {

    @FXML
    private Label DepositLabel;

    @FXML
    private Label OwedLabel;

    @FXML
    private Label addressLabel;

    @FXML
    private Label idLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private Label phoneLabel;

    @FXML
    private Label rentalPeriodLabel;

    @FXML
    private Label roomNumberLabel;

    private Customer customer = (Customer) FXRouter.getData();

    public void initialize() {
        showTenantsData();
    }

    public void showTenantsData() {
        roomNumberLabel.setText(String.valueOf(customer.getRoomNum()));
        DepositLabel.setText(String.format("%.2f",customer.getDeposit()));
        OwedLabel.setText(String.format("%.2f",customer.getOwed()));
        addressLabel.setText(customer.getAddress());
        nameLabel.setText(customer.getName());
        idLabel.setText(customer.getIdCard());
        phoneLabel.setText(customer.getPhoneNum());
        rentalPeriodLabel.setText(customer.getRentalPeriod());
    }

    public void clickToAgree(ActionEvent actionEvent) {
        try {
            FXRouter.goTo("AddCustomer");
        } catch (IOException e) {
            System.err.println("ไปที่หน้า login_detail ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
    }

}
