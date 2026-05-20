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
import java.util.ResourceBundle;

public class SettingsController implements Initializable {




    // ===================== FXML Fields =====================
    @FXML
    private TextField     txtCurrentUsername;
    @FXML
    private TextField     txtNewUsername;
    @FXML
    private PasswordField txtNewPassword;
    @FXML
    private TextField     txtNewPasswordVisible;
    @FXML
    private PasswordField txtConfirmPassword;
    @FXML
    private CheckBox      chkShowPassword;
    @FXML
    private Button        btnUpdate;
    @FXML
    private Button        btnLogout;
    @FXML
    private Label         lblMessage;




    // ===================== BO layer =====================
    private final UserBO bo = BOFactory.getInstance().getBO(BOTypes.USER);

    private UserDTO currentUser;




    // ===================== Initialize =====================
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblMessage.setVisible(false);
        txtNewPasswordVisible.setVisible(false);
        txtNewPasswordVisible.managedProperty().bind(txtNewPasswordVisible.visibleProperty());


        txtNewPassword.textProperty().addListener((o, ov, nv) -> {
            if (!txtNewPasswordVisible.getText().equals(nv)) {
                txtNewPasswordVisible.setText(nv);
            }
        });


        txtNewPasswordVisible.textProperty().addListener((o, ov, nv) -> {
            if (!txtNewPassword.getText().equals(nv)) {
                txtNewPassword.setText(nv);
            }
        });
    }


    public void setCurrentUser(UserDTO user) {
        this.currentUser = user;
        txtCurrentUsername.setText(user.getUsername());
        txtNewUsername.setText(user.getUsername());
    }




    //============ check password================
    @FXML
    void chkShowPasswordOnAction(ActionEvent e) {
        boolean show = chkShowPassword.isSelected();
        txtNewPassword.setVisible(!show);
        txtNewPasswordVisible.setVisible(show);
    }


    //============   Update password================
    @FXML
    void btnUpdateOnAction(ActionEvent e) {

        if (!txtNewPassword.getText().equals(txtConfirmPassword.getText())) {

            showMessage("Passwords do not match!", true);
            return;

        }



        try {


            bo.updateCredentials(
                    currentUser.getUsername(),
                    txtNewUsername.getText().trim(),
                    txtNewPassword.getText()
            );


            currentUser.setUsername(txtNewUsername.getText().trim());
            txtCurrentUsername.setText(currentUser.getUsername());
            showMessage("Credentials updated successfully!", false);


        } catch (LoginException ex) {
            showMessage(ex.getMessage(), true);
        }


    }




    //============   Logout btn  ================
    @FXML
    void btnLogoutOnAction(ActionEvent e) {
        try {
            Stage stage = (Stage) btnLogout.getScene().getWindow();
            stage.setScene(new Scene(
                    new FXMLLoader(getClass().getResource("/view/LoginPage.fxml")).load()
            ));
            stage.setTitle("Serenity Mental Health Therapy Center");
            stage.setResizable(false);
            stage.setMaximized(false);
        } catch (IOException ex) {
            new Alert(Alert.AlertType.ERROR, "Logout failed: " + ex.getMessage()).show();
        }
    }



    private void showMessage(String message, boolean isError) {

        lblMessage.setText(message);
        lblMessage.setStyle(isError ? "-fx-text-fill: red;" : "-fx-text-fill: green;");
        lblMessage.setVisible(true);

    }



}














