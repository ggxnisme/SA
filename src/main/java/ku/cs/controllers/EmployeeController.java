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

public class EmployeeController {

    @FXML
    private ListView<String> employName;

    @FXML
    private ListView<String> employRole;

    @FXML
    private ListView<String> employTel;

    public void initialize() {
        showEmployData();
    }

    public void showEmployData() {
        try {
            Connection connection = DBConnector.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM พนักงาน");
            while (resultSet.next()) {
                String name = resultSet.getString("ชื่อ_นามสกุล");
                String tel = resultSet.getString("เบอร์โทรศัพท์");
                String role = resultSet.getString("ตำแหน่ง");
                String addName = name;
                String addTel = tel;
                String addRole = role;
                employName.getItems().add(addName);
                employRole.getItems().add(addRole);
                employTel.getItems().add(addTel);
                employName.setMouseTransparent(true);
                employName.setFocusTraversable(false);
                employTel.setMouseTransparent(true);
                employTel.setFocusTraversable(false);
                employRole.setMouseTransparent(true);
                employRole.setFocusTraversable(false);
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

}
