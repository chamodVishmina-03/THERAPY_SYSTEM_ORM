package lk.ijse.therapycenter.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.therapycenter.bo.BOFactory;
import lk.ijse.therapycenter.bo.BOTypes;
import lk.ijse.therapycenter.bo.custom.PaymentBO;
import lk.ijse.therapycenter.bo.custom.RegistrationBO;
import lk.ijse.therapycenter.dto.PaymentDTO;
import lk.ijse.therapycenter.dto.RegistrationDTO;
import lk.ijse.therapycenter.dto.tm.PaymentTM;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class PaymentController implements Initializable {


    //form fields
    @FXML
    private TextField txtId;

    @FXML
    private TextField txtAmount;

    @FXML
    private ComboBox<String> cmbRegistrationId;

    @FXML
    private ComboBox<String> cmbStatus;

    @FXML
    private DatePicker dpDate;



    // buttons

    @FXML
    private Button btnSave;

    @FXML
    private Button btnUpdate;

    @FXML
    private Button btnClear;



    // Table fileds

    @FXML
    private TableView<PaymentTM> tblPayment;

    @FXML
    private TableColumn<PaymentTM, String> colId;

    @FXML
    private TableColumn<PaymentTM, String> colPatient;

    @FXML
    private TableColumn<PaymentTM, String> colProgram;

    @FXML
    private TableColumn<PaymentTM, Double> colAmount;

    @FXML
    private TableColumn<PaymentTM, String> colDate;

    @FXML
    private TableColumn<PaymentTM, String> colStatus;




    // BO Layers
    private final PaymentBO bo = BOFactory.getInstance().getBO(BOTypes.PAYMENT);
    private final RegistrationBO rbo = BOFactory.getInstance().getBO(BOTypes.REGISTRATION);



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colPatient.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        colProgram.setCellValueFactory(new PropertyValueFactory<>("programName"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("paymentDate"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        ObservableList<String> statusList = FXCollections.observableArrayList();
        statusList.add("PENDING");
        statusList.add("COMPLETED");
        statusList.add("FAILED");

        cmbStatus.setItems(statusList);
        cmbStatus.setValue("COMPLETED");

        dpDate.setValue(LocalDate.now());

        loadRegCombo();
        loadTable();
        generateId();

        cmbRegistrationId.setOnAction(event -> {
            autoFillAmount();
        });

        tblPayment.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, selected) -> {

                    if (selected != null) {

                        txtId.setText(selected.getId());

                        txtAmount.setText(
                                String.valueOf(selected.getAmount())
                        );

                        cmbStatus.setValue(selected.getStatus());
                    }
                }
        );
    }



    private void autoFillAmount() {

        String regId = cmbRegistrationId.getValue();

        if (regId != null) {

            try {

                List<RegistrationDTO> registrationList = rbo.getAll();

                for (int i = 0; i < registrationList.size(); i++) {

                    RegistrationDTO dto = registrationList.get(i);

                    if (dto.getId().equals(regId)) {

                        txtAmount.setText(
                                String.valueOf(dto.getFee())
                        );

                        break;
                    }
                }

            } catch (Exception e) {

            }
        }
    }


    private void loadRegCombo() {

        try {

            ObservableList<String> ids =
                    FXCollections.observableArrayList();

            List<RegistrationDTO> registrationList = rbo.getAll();

            for (int i = 0; i < registrationList.size(); i++) {

                RegistrationDTO dto = registrationList.get(i);

                ids.add(dto.getId());
            }

            cmbRegistrationId.setItems(ids);

        } catch (Exception e) {

            alert("Failed to load registrations : " + e.getMessage());
        }
    }


    // load table
    private void loadTable() {

        try {

            ObservableList<PaymentTM> list =
                    FXCollections.observableArrayList();

            List<PaymentDTO> paymentList = bo.getAll();

            for (int i = 0; i < paymentList.size(); i++) {

                PaymentDTO dto = paymentList.get(i);

                PaymentTM tm = new PaymentTM(
                        dto.getId(),
                        dto.getPatientName(),
                        dto.getProgramName(),
                        dto.getPaymentDate().toString(),
                        dto.getStatus(),
                        dto.getAmount()
                );

                list.add(tm);
            }

            tblPayment.setItems(list);

        } catch (Exception e) {

            alert(e.getMessage());
        }
    }

    // id generate
    private void generateId() {

        try {

            txtId.setText(bo.generateNextId());

        } catch (Exception e) {

            txtId.setText("PAY001");
        }
    }

    private boolean validate() {

        if (cmbRegistrationId.getValue() == null) {

            alert("Please select a registration.");
            return false;
        }

        try {

            double amount =
                    Double.parseDouble(txtAmount.getText().trim());

            if (amount <= 0) {

                alert("Please enter a valid payment amount.");
                return false;
            }

        } catch (Exception e) {

            alert("Please enter a valid payment amount.");
            return false;
        }

        return true;
    }



    private PaymentDTO getData() {

        return new PaymentDTO(
                txtId.getText().trim(),
                cmbRegistrationId.getValue(),
                null,
                null,
                cmbStatus.getValue(),
                Double.parseDouble(txtAmount.getText().trim()),
                Date.valueOf(dpDate.getValue())
        );
    }


    //clear fields
    private void clear() {

        cmbRegistrationId.setValue(null);

        txtAmount.clear();

        cmbStatus.setValue("COMPLETED");

        dpDate.setValue(LocalDate.now());

        tblPayment.getSelectionModel().clearSelection();
    }




    private void alert(String message) {

        new Alert(Alert.AlertType.ERROR, message).show();
    }

    private void info(String message) {

        new Alert(Alert.AlertType.INFORMATION, message).show();
    }




    @FXML
    void btnSaveOnAction(ActionEvent event) {

        if (!validate()) {
            return;
        }

        try {

            bo.save(getData());

            info("Payment processed successfully!");

            clear();

            loadTable();

            generateId();

        } catch (Exception e) {

            alert(e.getMessage());
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {

        if (!validate()) {
            return;
        }

        try {

            bo.update(getData());

            info("Payment updated successfully!");

            clear();

            loadTable();

        } catch (Exception e) {

            alert(e.getMessage());
        }
    }



    @FXML
    void btnClearOnAction(ActionEvent event) {

        clear();

        generateId();
    }
}