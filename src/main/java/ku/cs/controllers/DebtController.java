package ku.cs.controllers;

import com.github.saacsos.FXRouter;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import ku.cs.services.DBConnector;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class DebtController {

    @FXML
    private ListView<String> depositListView;

    @FXML
    private ListView<String> owedListView;

    @FXML
    private ListView<String> roomNumList;

    public void initialize() {
        showData();
    }

    public void showData() {

        try {
            Connection connection = DBConnector.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT เลขที่ห้องเช่า, ยอดค้างชำระ, เงินประกัน FROM ลูกค้า;");

            while (resultSet.next()) {
                String เลขที่ห้องเช่า = resultSet.getString("เลขที่ห้องเช่า");
                String listOut = เลขที่ห้องเช่า;
                String ยอดค้างชำระ = String.format("%.2f",resultSet.getFloat("ยอดค้างชำระ"));
                String listOut1 = ยอดค้างชำระ;
                String เงินประกัน = String.format("%.2f",resultSet.getFloat("เงินประกัน"));
                String listOut2 = เงินประกัน;
                roomNumList.getItems().add(listOut);
                owedListView.getItems().add(listOut1);
                depositListView.getItems().add(listOut2);
                roomNumList.setMouseTransparent(true);
                roomNumList.setFocusTraversable(false);
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

    @FXML
    public void clickToBack(Event event) {
        try {
            FXRouter.goTo ( "Home" );
        } catch (IOException e) {
            System.err.println ( "ไปที่หน้า Home ไม่ด้" );
        }
    }

}
