package lk.ijse.therapycenter.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lk.ijse.therapycenter.bo.BOFactory;
import lk.ijse.therapycenter.bo.BOTypes;
import lk.ijse.therapycenter.bo.custom.UserBO;
import lk.ijse.therapycenter.bo.exception.LoginException;
import lk.ijse.therapycenter.dto.UserDTO;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    // ===================== FXML fields =====================
    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private TextField txtPasswordVisible;

    @FXML
    private CheckBox chkShowPassword;

    @FXML
    private Label lblError;

    @FXML
    private Button btnLogin;



    // ===================== BO layer =====================
    private final UserBO userBO = BOFactory.getInstance().getBO(BOTypes.USER);



    // ===================== Initialize =====================
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        lblError.setVisible(false);

        txtPasswordVisible.setVisible(false);

        txtPasswordVisible.managedProperty().bind(
                txtPasswordVisible.visibleProperty()
        );

        txtPassword.textProperty().addListener(
                (observable, oldValue, newValue) -> {

                    if (!txtPasswordVisible.getText().equals(newValue)) {

                        txtPasswordVisible.setText(newValue);
                    }
                }



        );

        txtPasswordVisible.textProperty().addListener(
                (observable, oldValue, newValue) -> {

                    if (!txtPassword.getText().equals(newValue)) {
                        txtPassword.setText(newValue);
                    }
                }


        );
    }





    // ===================== btn actions =====================

    //   Show password checkbox
    @FXML
    void chkShowPasswordOnAction(ActionEvent e) {

        boolean show = chkShowPassword.isSelected();

        txtPassword.setVisible(!show);
        txtPasswordVisible.setVisible(show);

    }



    //   Login button
    @FXML
    void btnLoginOnAction(ActionEvent e) {

        lblError.setVisible(false);

        try {

            Optional<UserDTO> user = userBO.login(
                    txtUsername.getText().trim(),
                    txtPassword.getText()
            );

            if (user.isPresent()) {
                loadDashboard(user.get());
            }

        } catch (LoginException ex) {

            lblError.setText(ex.getMessage());
            lblError.setVisible(true);
        }
    }




    private void loadDashboard(UserDTO user) {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/view/Dashboard.fxml")
            );


            Scene scene = new Scene(loader.load());

            DashboardController controller = loader.getController();

            controller.setCurrentUser(user);

            Stage stage = (Stage) btnLogin.getScene().getWindow();
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.setResizable(true);


        } catch (IOException ex) {

            new Alert(
                    Alert.AlertType.ERROR,
                    "Dashboard load failed: " + ex.getMessage()
            ).show();
        }
    }




}