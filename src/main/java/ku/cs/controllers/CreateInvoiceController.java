package ku.cs.controllers;

import com.github.saacsos.FXRouter;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import ku.cs.models.Invoice;
import ku.cs.services.DBConnector;
import ku.cs.services.Effect;

import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class CreateInvoiceController {

    public DatePicker datePicker;
    @FXML
    private Label roomText;
    @FXML
    private Label totalPriceText;
    @FXML
    private Label roomPriceText;
    @FXML
    private Label owedText;
    @FXML
    private Label waterUnitText;
    @FXML
    private Label elecUnitText;
    @FXML
    private Label roomPriceTotalText;
    @FXML
    private Label waterPriceTotalText;
    @FXML
    private Label elecPriceTotalText;
    @FXML
    private Label owedTotalText;
    @FXML
    private ListView<String> showRoomListView;
    @FXML
    private TextField invoiceNumberTextField;
    @FXML
    private Label errorLabel;

    @FXML
    private Pane calculateSuccessfulPane;

    private Invoice invoice;

    private Effect effect;


    public void initialize() {
        showListViewInCreateInv();
        handleSelectedListView();
        effect = new Effect();
        calculateSuccessfulPane.setDisable(true);
        calculateSuccessfulPane.setOpacity(0);
    }

    public void showListViewInCreateInv() {

        try {
            Connection con = DBConnector.getConnection();
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT เลขที่ห้องเช่า FROM ลูกค้า");
            while (resultSet.next()) {
                String เลขที่ห้องเช่า = resultSet.getString(1);
                String listOut = เลขที่ห้องเช่า;
                showRoomListView.getItems().add(listOut);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void handleSelectedListView() {
        showRoomListView.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                        showDataFromDB(t1);
                    }
                }
        );
    }

    public void showDataFromDB(String t1) {
        try {
            Connection con = DBConnector.getConnection();
            Statement statement1 = con.createStatement();
            Statement statement2 = con.createStatement();
            Statement stetement3 = con.createStatement();
            ResultSet resultSet1 = statement1.executeQuery("SELECT เลขที่ห้องเช่า,ยอดค้างชำระ FROM ลูกค้า WHERE เลขที่ห้องเช่า = " + t1);
            ResultSet resultSet2 = statement2.executeQuery("SELECT หน่วยน้ำ,หน่วยไฟ,ค่าน้ำ,ค่าไฟ FROM มิเตอร์ WHERE (เลขที่ห้องเช่า,วัน_เดือน_ปีที่จด) IN (SELECT เลขที่ห้องเช่า, MAX(วัน_เดือน_ปีที่จด) FROM มิเตอร์ WHERE เลขที่ห้องเช่า = "+t1+");");
            ResultSet resultSet3 = stetement3.executeQuery("SELECT ค่าห้อง FROM ใบแจ้งหนี้ WHERE เลขที่ห้องเช่า = " + t1);

            while (resultSet1.next() & resultSet2.next() & resultSet3.next()) {
                roomText.setText(resultSet1.getString("เลขที่ห้องเช่า"));
                owedText.setText(String.format("%.2f",resultSet1.getFloat("ยอดค้างชำระ")));
                owedTotalText.setText(String.format("%.2f",resultSet1.getFloat("ยอดค้างชำระ")));
                waterUnitText.setText(resultSet2.getString("หน่วยน้ำ"));
                elecUnitText.setText(resultSet2.getString("หน่วยไฟ"));
                waterPriceTotalText.setText(String.format("%.2f",resultSet2.getFloat("ค่าน้ำ")));
                elecPriceTotalText.setText(String.format("%.2f",resultSet2.getFloat("ค่าไฟ")));
                roomPriceText.setText(String.format("%.2f",resultSet3.getFloat("ค่าห้อง")));
                roomPriceTotalText.setText(String.format("%.2f",resultSet3.getFloat("ค่าห้อง")));
                statement1.close();
                statement2.close();
                stetement3.close();
                con.close();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public float calInvoice(float roomPrice, float waterPrice, float electPrice, float owedPrice) {
        return (roomPrice + waterPrice + electPrice + owedPrice);
    }

    @FXML
    public void calInvoiceBtn(ActionEvent actionEvent) {
        if (roomText.getText().equals("RoomNumber")) {
            errorLabel.setText("กรุณาเลือกห้อง");
        }
        else if ((invoiceNumberTextField.getText()).isEmpty()) {
            errorLabel.setText("กรุณากรอกเลขที่ใบแจ้งหนี้");
        } else if (datePicker.getValue() == null) {
            errorLabel.setText("กรุณาระบุวันที่");
        }
        else {
            try {
                Long.parseLong(invoiceNumberTextField.getText());
                if (Long.parseLong(invoiceNumberTextField.getText()) < 0) {
                    errorLabel.setText("กรุณากรอกเลขที่ใบแจ้งหนี้ให้ถูกต้อง");
                }
                try {
                    long invoiceNumber = Long.parseLong(invoiceNumberTextField.getText());
                    float roomPrice = Float.parseFloat(roomPriceTotalText.getText());
                    float waterPrice = Float.parseFloat(waterPriceTotalText.getText());
                    float electPrice = Float.parseFloat(elecPriceTotalText.getText());
                    float owedPrice = Float.parseFloat(owedTotalText.getText());
                    float calInvoice = calInvoice(roomPrice, waterPrice, electPrice, owedPrice);
                    totalPriceText.setText(String.format("%.2f",calInvoice));

                    addCalInvoiceToDB(Integer.parseInt(roomText.getText()), invoiceNumber, datePicker.getValue() , roomPrice, calInvoice, 0, 0);

                    calculateSuccessfulPane.setOpacity(1);
                    calculateSuccessfulPane.setDisable(false);
                    effect.fadeInPage(calculateSuccessfulPane);
                    errorLabel.setText("");
                } catch (Exception e) {
                    System.out.println(e);
                }
            } catch (NumberFormatException e) {
                errorLabel.setText("กรุณาใส่ตัวเลข");
            }

        }
        effect.fadeOutLabelEffect(errorLabel, 3);
    }

    @FXML
    public void handleCloseCalculateSuccess() {
        calculateSuccessfulPane.setOpacity(0);
        calculateSuccessfulPane.setDisable(true);
    }

    public void addCalInvoiceToDB(int roomNum, long invoiceNum, LocalDate date, float roomPrice, float calInvoice, float customerPaid, int paidStatus) {

        try {
            Connection con = DBConnector.getConnection();
            Statement statement = con.createStatement();
            String sql = "INSERT INTO ใบแจ้งหนี้(เลขที่ห้องเช่า, เลขที่ใบแจ้งหนี้, วัน_เดือน_ปีที่ออกใบแจ้งหนี้, ค่าห้อง, ยอดเงินสุทธิ, ยอดเงินที่ชำระ, สถานะการชำระเงิน)" +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, roomNum);
            preparedStatement.setLong(2, invoiceNum);
            preparedStatement.setDate(3, Date.valueOf(date));
            preparedStatement.setFloat(4, roomPrice);
            preparedStatement.setFloat(5, calInvoice);
            preparedStatement.setFloat(6, customerPaid);
            preparedStatement.setInt(7, paidStatus);

            int addedRows = preparedStatement.executeUpdate();
            if (addedRows > 0) {
                invoice = new Invoice(roomNum, invoiceNum, date, roomPrice, calInvoice, customerPaid, paidStatus);
            }
            statement.close();
            con.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void datePicker(ActionEvent actionEvent) {
        datePicker.getValue();
    }


    @FXML
    public void printBtn(ActionEvent actionEvent) {

        if (roomText.getText().equals("RoomNumber")) {
            errorLabel.setText("กรุณาเลือกห้อง");
        }
        if ((invoiceNumberTextField.getText()).isEmpty()) {
            errorLabel.setText("กรุณากรอกเลขที่ใบแจ้งหนี้");
        } else if (Long.parseLong(invoiceNumberTextField.getText()) < 0) {
            errorLabel.setText("กรุณากรอกเลขที่ใบแจ้งหนี้ให้ถูกต้อง");
            ;
        } else if (datePicker.getValue() == null) {
            errorLabel.setText("กรุณาระบุวันที่");
        } else {
            try {
                long invoiceNumber = Long.parseLong(invoiceNumberTextField.getText());
                float roomPrice = Float.parseFloat(roomPriceTotalText.getText());
                float waterPrice = Float.parseFloat(waterPriceTotalText.getText());
                float electPrice = Float.parseFloat(elecPriceTotalText.getText());
                float owedPrice = Float.parseFloat(owedTotalText.getText());
                float calInvoice = calInvoice(roomPrice, waterPrice, electPrice, owedPrice);
                totalPriceText.setText(Float.toString(calInvoice));
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YYYY");
                String date = datePicker.getValue().format(formatter);

//            addCalInvoiceToDB(Integer.parseInt(roomText.getText()), invoiceNumber, date, roomPrice, calInvoice, 0, 0, "");
                try {
                    FXRouter.goTo("Invoice", invoice);
                } catch (Exception e) {
                    System.err.println("ไปที่หน้า Invoice ไม่ได้");
                    System.err.println("ตรวจสอบการกำหนด route");
                }
            } catch (NumberFormatException e) {
                errorLabel.setText("กรุณาใส่ตัวเลข");
            }

        } effect.fadeOutLabelEffect(errorLabel, 3);
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



