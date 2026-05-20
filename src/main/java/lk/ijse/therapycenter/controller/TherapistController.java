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
import lk.ijse.therapycenter.bo.custom.TherapistBO;
import lk.ijse.therapycenter.dto.TherapistDTO;
import lk.ijse.therapycenter.dto.tm.TherapistTM;
import lk.ijse.therapycenter.util.ValidationUtil;

import java.net.URL;
import java.util.ResourceBundle;

public class TherapistController implements Initializable {



    // ===================== FXML Fields
    @FXML
    private TextField txtId;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtPhone;

    @FXML
    private TextField txtSpec;



    // field buttons
    @FXML
    private Button btnSave;

    @FXML
    private Button btnUpdate;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnClear;




    //     tble view
    @FXML
    private TableView<TherapistTM> tblTherapist;

    @FXML
    private TableColumn<TherapistTM, String> colId;

    @FXML
    private TableColumn<TherapistTM, String> colName;

    @FXML
    private TableColumn<TherapistTM, String> colEmail;

    @FXML
    private TableColumn<TherapistTM, String> colPhone;

    @FXML
    private TableColumn<TherapistTM, String> colSpec;




    // ===================== BO layer  =====================
    private final TherapistBO bo = BOFactory.getInstance().getBO(BOTypes.THERAPIST);




    // ===================== Initialize =====================
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colSpec.setCellValueFactory(new PropertyValueFactory<>("specialization"));


        loadTable();
        generateId();

        tblTherapist.getSelectionModel()
                .selectedItemProperty()
                .addListener((o, ov, sel) -> {

                    if (sel != null) {
                        txtId.setText(sel.getId());
                        txtName.setText(sel.getName());
                        txtEmail.setText(sel.getEmail());
                        txtPhone.setText(sel.getPhone());
                        txtSpec.setText(sel.getSpecialization());
                    }
                });
    }

    // ===================== Private Helper Methods =====================

    //  therapists load table
    private void loadTable() {

        try {

            ObservableList<TherapistTM> list =
                    FXCollections.observableArrayList();

            bo.getAll().forEach(d -> {

                TherapistTM tm = new TherapistTM(
                        d.getId(),
                        d.getName(),
                        d.getEmail(),
                        d.getPhone(),
                        d.getSpecialization()
                );

                list.add(tm);
            });

            tblTherapist.setItems(list);

        } catch (Exception e) {

            alert(e.getMessage());
        }
    }



    //  generate id
    private void generateId() {

        try {

            txtId.setText(bo.generateNextId());

        } catch (Exception e) {

            txtId.setText("T001");
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

    private TherapistDTO getData() {

        return new TherapistDTO(
                txtId.getText().trim(),
                txtName.getText().trim(),
                txtEmail.getText().trim(),
                txtPhone.getText().trim(),
                txtSpec.getText().trim()
        );
    }


    // Form fields clear
    private void clear() {

        txtName.clear();
        txtEmail.clear();
        txtPhone.clear();
        txtSpec.clear();
        tblTherapist.getSelectionModel().clearSelection();
    }





    // ===================== Alert  =====================
    private void alert(String message) {

        new Alert(Alert.AlertType.ERROR, message).show();
    }

    private void info(String message) {

        new Alert(Alert.AlertType.INFORMATION, message).show();
    }

    private boolean confirm(String message) {

        return new Alert(Alert.AlertType.CONFIRMATION, message)
                .showAndWait()
                .filter(b -> b == ButtonType.OK)
                .isPresent();
    }






    // =====================    Button actions    =====================

    //Save button
    @FXML
    void btnSaveOnAction(ActionEvent e) {

        if (!validate()) {
            return;
        }

        try {
            bo.save(getData());
            info("Therapist saved successfully!");
            clear();
            loadTable();
            generateId();

        } catch (Exception ex) {

            alert(ex.getMessage());
        }
    }

    // Update button
    @FXML
    void btnUpdateOnAction(ActionEvent e) {

        if (!validate()) {
            return;
        }

        try {

            bo.update(getData());
            info("Therapist updated successfully!");
            clear();
            loadTable();

        } catch (Exception ex) {

            alert(ex.getMessage());
        }
    }



    // Delete button
    @FXML
    void btnDeleteOnAction(ActionEvent e) {

        if (txtId.getText().isEmpty()) {
            alert("Please select a therapist from the table.");

            return;
        }

        if (confirm("Are you sure you want to delete therapist " + txtId.getText() + "?")) {

            try {

                bo.delete(txtId.getText());
                info("Therapist deleted successfully!");
                clear();
                loadTable();
                generateId();

            } catch (Exception ex) {

                alert(ex.getMessage());
            }
        }
    }



    // Clear button
    @FXML
    void btnClearOnAction(ActionEvent e) {
        clear();
        generateId();
    }
}