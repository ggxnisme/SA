package ku.cs.controllers;

import com.github.saacsos.FXRouter;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import ku.cs.services.DBConnector;
import ku.cs.services.Effect;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class DebtController {

    @FXML
    private ListView<String> depositListView;

    @FXML
    private ListView<String> owedListView;

    @FXML
    private ListView<String> roomNumList;

    @FXML
    private Label errorLabel;

    private Effect effect;

    private String room;

    public void initialize() {
        effect = new Effect();
        showData();
        handleSelectedListView();
    }

    public void showData() {
        try {
            Connection connection = DBConnector.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT เลขที่ห้องเช่า, ยอดค้างชำระ, เงินประกัน FROM ลูกค้า ORDER BY เลขที่ห้องเช่า ASC");

            while (resultSet.next()) {
                String เลขที่ห้องเช่า = resultSet.getString("เลขที่ห้องเช่า");
                String listOut = เลขที่ห้องเช่า;
                String ยอดค้างชำระ = String.format("%,.2f",resultSet.getFloat("ยอดค้างชำระ"));
                String listOut1 = ยอดค้างชำระ;
                String เงินประกัน = String.format("%,.2f",resultSet.getFloat("เงินประกัน"));
                String listOut2 = เงินประกัน;
                roomNumList.getItems().add(listOut);
                owedListView.getItems().add(listOut1);
                depositListView.getItems().add(listOut2);
                owedListView.setMouseTransparent(true);
                owedListView.setFocusTraversable(false);
                depositListView.setMouseTransparent(true);
                depositListView.setFocusTraversable(false);
            }
            statement.close();
            connection.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void handleSelectedListView() {
        roomNumList.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                        room = t1;
                    }
                }
        );
    }

    @FXML
    void deleteDepositBtn(ActionEvent event) {
        if (room == null) {
            errorLabel.setText("กรุณาเลือกห้อง");
        }
        try {
            Connection connection = DBConnector.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT เงินประกัน,ยอดค้างชำระ FROM ลูกค้า WHERE เลขที่ห้องเช่า = "+room);
            while (resultSet.next()) {
                if (resultSet.getFloat("ยอดค้างชำระ") >= resultSet.getFloat("เงินประกัน")) {
                    updateData(resultSet.getFloat("เงินประกัน"),resultSet.getFloat("ยอดค้างชำระ"));
                    errorLabel.setText("");
                }
                else {
                    errorLabel.setText("ไม่สามารถหักเงินประกันได้");
                }
            }
            roomNumList.getItems().clear();
            depositListView.getItems().clear();
            owedListView.getItems().clear();
            showData();
            statement.close();
            connection.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        effect.fadeOutLabelEffect(errorLabel,3);
    }

    public void updateData(float dep, float ow) {
        try {
            Connection connection = DBConnector.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE ลูกค้า SET เงินประกัน = ?, ยอดค้างชำระ = ? WHERE เลขที่ห้องเช่า = "+room);
            preparedStatement.setFloat(1, dep - dep);
            preparedStatement.setFloat(2, ow - ow);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        }catch (Exception e) {
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

}
