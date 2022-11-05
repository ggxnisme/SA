package ku.cs.controllers;

import com.github.saacsos.FXRouter;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class HomeController {
    @FXML
    public void clickToAddCustomer(ActionEvent event) {
        try {
            FXRouter.goTo("AddCustomer");
        } catch (IOException e) {
            System.err.println("ไปที่หน้า login_detail ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
    }

    public void clickCalFacility(ActionEvent actionEvent) {
        try {
            FXRouter.goTo("CalMeter");
        } catch (IOException e) {
            System.err.println("ไปที่หน้า login_detail ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
    }

    public void clickCreateInvoice(ActionEvent actionEvent) {
        try {
            FXRouter.goTo("CreateInvoice");
        } catch (IOException e) {
            System.err.println("ไปที่หน้า login_detail ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
    }

    public void clickTenant(ActionEvent actionEvent) {
        try {
            FXRouter.goTo("AllTenants");
        } catch (IOException e) {
            System.err.println("ไปที่หน้า login_detail ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
    }

    public void clickCheckPayment(ActionEvent actionEvent){
        try {
            FXRouter.goTo("Payment");
        } catch (IOException e) {
            System.err.println("ไปที่หน้า login_detail ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
    }

    public void clickCreateReceipt(ActionEvent actionEvent) {
        try {
            FXRouter.goTo("Receipt");
        } catch (IOException e) {
            System.err.println("ไปที่หน้า login_detail ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
    }

    public void clickMonthlySum(ActionEvent actionEvent) {
        try {
            FXRouter.goTo("MonthlySum");
        } catch (IOException e) {
            System.err.println("ไปที่หน้า login_detail ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
    }

    public void clickDebt(ActionEvent actionEvent) {
        try {
            FXRouter.goTo("Debt");
        } catch (IOException e) {
            System.err.println("ไปที่หน้า login_detail ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
    }

    public void employDataBtn(Event event) {
        try {
            FXRouter.goTo("Employee");
        } catch (IOException e) {
            System.err.println("ไปที่หน้า login_detail ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
    }


}
