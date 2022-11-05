package ku.cs;

import com.github.saacsos.FXRouter;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.application.Application;




public class App extends Application {

    private static Scene scene;

    public void start(Stage stage) throws IOException {
        FXRouter.bind(this, stage, "Dormitory", 640, 400);
        stage.setResizable(false);
        configRoute();
        FXRouter.goTo("Home");
    }
    private static void configRoute() {
        String packageStr = "ku/cs/";

        FXRouter.when("Home", packageStr + "Home.fxml");
        FXRouter.when("AddCustomer", packageStr + "AddCustomer.fxml");
        FXRouter.when("CalMeter", packageStr + "CalMeter.fxml");
        FXRouter.when("CreateInvoice", packageStr + "CreateInvoice.fxml");
        FXRouter.when("Invoice", packageStr + "Invoice.fxml", 600, 400);
        FXRouter.when("AllTenants", packageStr + "AllTenants.fxml");
        FXRouter.when("Receipt", packageStr + "Receipt.fxml");
        FXRouter.when("Payment", packageStr + "Payment.fxml");
        FXRouter.when("Debt", packageStr + "Debt.fxml");
        FXRouter.when("MonthlySum", packageStr + "MonthlySum.fxml");
        FXRouter.when("ReceiptPrint", packageStr + "ReceiptPrint.fxml", 600, 400);
        FXRouter.when("Tenants", packageStr + "Tenants.fxml");
        FXRouter.when("Employee", packageStr + "Employee.fxml");
        FXRouter.when("MonthlySumPrint",packageStr + "MonthlySumPrint.fxml",600,400);
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}
