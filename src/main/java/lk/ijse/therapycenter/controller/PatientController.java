package lk.ijse.therapycenter.controller;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.therapycenter.bo.BOFactory;
import lk.ijse.therapycenter.bo.BOTypes;
import lk.ijse.therapycenter.bo.custom.PatientBO;
import lk.ijse.therapycenter.dto.PatientDTO;
import lk.ijse.therapycenter.dto.tm.PatientTM;
import lk.ijse.therapycenter.util.ValidationUtil;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class PatientController implements Initializable {

    // =====================  FXML fields ================
    @FXML private TextField txtId;
    @FXML private TextField txtName;
    @FXML private TextField txtEmail;
    @FXML private TextField txtPhone;
    @FXML private TextField txtAddress;
    @FXML private TextArea txtMedicalHistory;



    @FXML
    private Button btnSave;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnClear;


    // ===================== tble column ==================
    @FXML
    private TableView<PatientTM> tblPatient;
    @FXML
    private TableColumn<PatientTM, String> colId;
    @FXML
    private TableColumn<PatientTM, String> colName;
    @FXML
    private TableColumn<PatientTM, String> colEmail;
    @FXML
    private TableColumn<PatientTM, String> colPhone;
    @FXML
    private TableColumn<PatientTM, String> colAddress;
    @FXML
    private TableColumn<PatientTM, String> colHistory;




    // ===================== BO layer
    private final PatientBO bo = BOFactory.getInstance().getBO(BOTypes.PATIENT);




    // =====================  initialize
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // table columns
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colHistory.setCellValueFactory(new PropertyValueFactory<>("medicalHistory"));

        // loading data and  Id generate
        loadTable();
        generateId();


        // table row select
        tblPatient.getSelectionModel().selectedItemProperty().addListener(

                (ObservableValue<? extends PatientTM> o, PatientTM oldValue, PatientTM selectedItem) -> {
                    if (selectedItem != null) {
                        txtId.setText(selectedItem.getId());
                        txtName.setText(selectedItem.getName());
                        txtEmail.setText(selectedItem.getEmail());
                        txtPhone.setText(selectedItem.getPhone());
                        txtAddress.setText(selectedItem.getAddress());


                        try {
                            Optional<PatientDTO> result = bo.findById(selectedItem.getId());
                            if (result.isPresent()) {
                                txtMedicalHistory.setText(result.get().getMedicalHistory());
                            }
                        } catch (Exception ignored) {}
                    }
                }
        );


    }





    // ===================== helper methods =====================
     //  load table
    private void loadTable() {
        try {
            ObservableList<PatientTM> list = FXCollections.observableArrayList();
            bo.getAll().forEach(d -> list.add(new PatientTM(
                    d.getId(),
                    d.getName(),
                    d.getEmail(),
                    d.getPhone(),
                    d.getAddress(),
                    d.getMedicalHistory()
            )));
            tblPatient.setItems(list);
        } catch (Exception e) {
            alert(e.getMessage());
        }
    }


    private void generateId() {
        try {
            txtId.setText(bo.generateNextId());
        } catch (Exception e) {
            txtId.setText("PAT001");
        }
    }



    private boolean validate() {
        if (!ValidationUtil.isValidName(txtName.getText())) {

            alert("Invalid name. Letters only.");
            return false;
        }
        if (!ValidationUtil.isValidEmail(txtEmail.getText())) {
            alert("Invalid email format.");
            return false;
        }
        if (!ValidationUtil.isValidPhone(txtPhone.getText())) {
            alert("Invalid phone. Use 0XXXXXXXXX or +94XXXXXXXXX");
            return false;
        }
        return true;
    }


    private PatientDTO getData() {
        return new PatientDTO(
                txtId.getText().trim(),
                txtName.getText().trim(),
                txtEmail.getText().trim(),
                txtPhone.getText().trim(),
                txtAddress.getText().trim(),
                txtMedicalHistory.getText().trim()
        );


    }




   //   fields clear
    private void clear() {
        txtName.clear();
        txtEmail.clear();
        txtPhone.clear();
        txtAddress.clear();
        txtMedicalHistory.clear();
        tblPatient.getSelectionModel().clearSelection();
    }




    // ===================== Alert methods
    // error alert
    private void alert(String message) {

        new Alert(Alert.AlertType.ERROR, message).show();
    }


    //  success alert
    private void info(String message) {

        new Alert(Alert.AlertType.INFORMATION, message).show();
    }


    // conform dialog
    private boolean confirm(String message) {
        return new Alert(Alert.AlertType.CONFIRMATION, message)
                .showAndWait()
                .filter(b -> b == ButtonType.OK)
                .isPresent();
    }








    // ===================== Buttons =====================

    //  Save button
    @FXML
    void btnSaveOnAction(ActionEvent e) {
        if (!validate()) return;

        try {
            bo.save(getData());
            info("Patient saved successfully!");
            clear();
            loadTable();
            generateId();


        } catch (Exception ex) {
            alert(ex.getMessage());
        }


    }



        //    Update button
    @FXML
    void btnUpdateOnAction(ActionEvent e) {
        if (!validate()) return;
        try {
            bo.update(getData());
            info("Patient updated successfully!");
            clear();
            loadTable();
        } catch (Exception ex) {
            alert(ex.getMessage());
        }
    }



    //      delete button
    @FXML
    void btnDeleteOnAction(ActionEvent e) {
        if (txtId.getText().isEmpty()) {
            alert("Please select a patient from the table.");
            return;
        }
        if (confirm("Are you sure you want to delete patient " + txtId.getText() + "?")) {

            try {

                bo.delete(txtId.getText());
                info("Patient deleted successfully!");
                clear();
                loadTable();
                generateId();


            } catch (Exception ex) {
                alert(ex.getMessage());
            }
        }
    }




       //   Clear button
    @FXML
    void btnClearOnAction(ActionEvent e) {
        clear();
        generateId();
    }






}





