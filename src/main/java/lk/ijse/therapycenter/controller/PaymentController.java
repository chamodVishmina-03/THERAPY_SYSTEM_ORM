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
import lk.ijse.therapycenter.dto.tm.PaymentTM;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;



public class PaymentController implements Initializable {


    // ===================== FXML Fields =====================
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



    @FXML
    private Button btnSave;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnClear;



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



    // ===================== BO layer =====================
    private final PaymentBO      bo  = BOFactory.getInstance().getBO(BOTypes.PAYMENT);
    private final RegistrationBO rbo = BOFactory.getInstance().getBO(BOTypes.REGISTRATION);




    // ===================== initialize =====================

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //     table columns
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colPatient.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        colProgram.setCellValueFactory(new PropertyValueFactory<>("programName"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("paymentDate"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));


        cmbStatus.setItems(FXCollections.observableArrayList("PENDING", "COMPLETED", "FAILED"));
        cmbStatus.setValue("COMPLETED");

        dpDate.setValue(LocalDate.now());

        loadRegCombo();
        loadTable();
        generateId();


        cmbRegistrationId.setOnAction(e -> autoFillAmount());


        //   Table row select
        tblPayment.getSelectionModel().selectedItemProperty().addListener((o, ov, sel) -> {


            if (sel != null) {
                txtId.setText(sel.getId());
                txtAmount.setText(String.valueOf(sel.getAmount()));
                cmbStatus.setValue(sel.getStatus());
            }

        });

    }



    // ===================== Helper methods =====================

    private void autoFillAmount() {
        String regId = cmbRegistrationId.getValue();
        if (regId != null) {
            try {
                rbo.getAll().stream()
                        .filter(r -> r.getId().equals(regId))
                        .findFirst()
                        .ifPresent(r -> txtAmount.setText(String.valueOf(r.getFee())));
            } catch (Exception ignored) {}
        }
    }


    // Registration id loads
    private void loadRegCombo() {

        try {
            ObservableList<String> ids = FXCollections.observableArrayList();
            rbo.getAll().forEach(r -> ids.add(r.getId()));
            cmbRegistrationId.setItems(ids);


        } catch (Exception e) {
            alert("Failed to load registrations: " + e.getMessage());
        }
    }


    // load table
    private void loadTable() {

        try {
            ObservableList<PaymentTM> list = FXCollections.observableArrayList();
            bo.getAll().forEach(d -> list.add(new PaymentTM(
                    d.getId(),
                    d.getPatientName(),
                    d.getProgramName(),
                    d.getPaymentDate().toString(),
                    d.getStatus(),
                    d.getAmount()
            )));

            tblPayment.setItems(list);


        } catch (Exception e) {
            alert(e.getMessage());
        }
    }



    /**    Payment id auto generate    */
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

            double amount = Double.parseDouble(txtAmount.getText().trim());
            if (amount <= 0) throw new Exception();


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



    //  fields clear
    private void clear() {

        cmbRegistrationId.setValue(null);
        txtAmount.clear();
        cmbStatus.setValue("COMPLETED");
        dpDate.setValue(LocalDate.now());
        tblPayment.getSelectionModel().clearSelection();

    }







    // =====================         Alerts     =====================
    private void alert(String message) {

        new Alert(Alert.AlertType.ERROR, message).show();
    }


    private void info(String message)  {

        new Alert(Alert.AlertType.INFORMATION, message).show();

    }








    // ===================== Button Actions =====================

    //  Save  button
    @FXML
    void btnSaveOnAction(ActionEvent e) {
        if (!validate()) return;
        try {

            bo.save(getData());
            info("Payment processed successfully!");
            clear();
            loadTable();
            generateId();


        } catch (Exception ex) {
            alert(ex.getMessage());
        }
    }



    //   Update button
    @FXML
    void btnUpdateOnAction(ActionEvent e) {

        if (!validate()) return;
        try {
            bo.update(getData());
            info("Payment updated successfully!");
            clear();
            loadTable();


        } catch (Exception ex) {
            alert(ex.getMessage());
        }
    }




    //    Clear button
    @FXML
    void btnClearOnAction(ActionEvent e) {
        clear();
        generateId();

    }



}















