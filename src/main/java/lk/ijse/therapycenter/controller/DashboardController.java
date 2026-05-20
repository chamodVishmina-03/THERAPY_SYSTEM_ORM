package lk.ijse.therapycenter.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import lk.ijse.therapycenter.dto.UserDTO;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    // ===================== FXML fields =====================
    @FXML
    private AnchorPane ancMainContainer;

    @FXML
    private Button btnPatient;

    @FXML
    private Button btnTherapist;

    @FXML
    private Button btnProgram;

    @FXML
    private Button btnRegistration;

    @FXML
    private Button btnSession;

    @FXML
    private Button btnPayment;

    @FXML
    private Button btnSettings;

    @FXML
    private Label lblUserInfo;

    //  user details
    private UserDTO currentUser;





    // ===================== initialize =====================
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        navigateTo("/view/PatientPage.fxml");
    }




    public void setCurrentUser(UserDTO user) {

        this.currentUser = user;

        lblUserInfo.setText(
                "User: " +
                        user.getUsername() +
                        " (" +
                        user.getRole() +
                        ")"
        );

        if ("RECEPTIONIST".equals(user.getRole())) {

            btnTherapist.setDisable(true);

            btnProgram.setDisable(true);
        }
    }

    // =====================  Navigate btns =====================



    //    patient manage navigate
    @FXML
    void btnPatientOnAction(ActionEvent e) {

        navigateTo("/view/PatientPage.fxml");
    }




    //    therapist manage navigate
    @FXML
    void btnTherapistOnAction(ActionEvent e) {

        navigateTo("/view/TherapistPage.fxml");
    }




    // Therapy program management
    @FXML
    void btnProgramOnAction(ActionEvent e) {

        navigateTo("/view/TherapyProgramPage.fxml");
    }



    //    Registration page
    @FXML
    void btnRegistrationOnAction(ActionEvent e) {

        navigateTo("/view/RegistrationPage.fxml");
    }



    //    Therapy session
    @FXML
    void btnSessionOnAction(ActionEvent e) {

        navigateTo("/view/TherapySessionPage.fxml");
    }




    //    payment  manage
    @FXML
    void btnPaymentOnAction(ActionEvent e) {

        navigateTo("/view/PaymentPage.fxml");
    }



    @FXML
    void btnSettingsOnAction(ActionEvent e) {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/view/SettingsPage.fxml")
            );

            AnchorPane pane = loader.load();

            SettingsController controller =
                    loader.getController();

            controller.setCurrentUser(currentUser);

            ancMainContainer.getChildren().clear();

            pane.prefWidthProperty().bind(
                    ancMainContainer.widthProperty()
            );

            pane.prefHeightProperty().bind(
                    ancMainContainer.heightProperty()
            );

            ancMainContainer.getChildren().add(pane);

        } catch (IOException ex) {

            new Alert(
                    Alert.AlertType.ERROR,
                    ex.getMessage()
            ).show();
        }
    }






    private void navigateTo(String path) {

        try {

            ancMainContainer.getChildren().clear();

            AnchorPane pane = FXMLLoader.load(
                    getClass().getResource(path)
            );

            pane.prefWidthProperty().bind(
                    ancMainContainer.widthProperty()
            );

            pane.prefHeightProperty().bind(
                    ancMainContainer.heightProperty()
            );

            ancMainContainer.getChildren().add(pane);

        } catch (IOException ex) {

            new Alert(
                    Alert.AlertType.ERROR,
                    "Page not found: " + ex.getMessage()
            ).show();
        }
    }







}















