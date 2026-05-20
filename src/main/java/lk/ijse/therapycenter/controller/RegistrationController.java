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
import lk.ijse.therapycenter.bo.custom.PatientBO;
import lk.ijse.therapycenter.bo.custom.RegistrationBO;
import lk.ijse.therapycenter.bo.custom.TherapyProgramBO;
import lk.ijse.therapycenter.dto.RegistrationDTO;
import lk.ijse.therapycenter.dto.tm.RegistrationTM;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class RegistrationController implements Initializable {

    //  FXML fields
    @FXML
    private TextField txtId;

    @FXML
    private ComboBox<String> cmbPatientId;

    @FXML
    private ComboBox<String> cmbProgramId;

    @FXML
    private DatePicker dpDate;



    // btns
    @FXML
    private Button btnSave;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnClear;




    // tble view
    @FXML
    private TableView<RegistrationTM> tblReg;

    @FXML
    private TableColumn<RegistrationTM, String> colId;

    @FXML
    private TableColumn<RegistrationTM, String> colPatient;

    @FXML
    private TableColumn<RegistrationTM, String> colProgram;

    @FXML
    private TableColumn<RegistrationTM, String> colDate;

    @FXML
    private TableColumn<RegistrationTM, Double> colFee;






    // =====================  BO layer =====================
    private final RegistrationBO bo = BOFactory.getInstance().getBO(BOTypes.REGISTRATION);
    private final PatientBO pbo = BOFactory.getInstance().getBO(BOTypes.PATIENT);
    private final TherapyProgramBO prbo = BOFactory.getInstance().getBO(BOTypes.THERAPY_PROGRAM);




    // =====================   initialize   =====================
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colPatient.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        colProgram.setCellValueFactory(new PropertyValueFactory<>("programName"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("registrationDate"));
        colFee.setCellValueFactory(new PropertyValueFactory<>("fee"));




        dpDate.setValue(LocalDate.now());
        loadCombos();
        loadTable();
        generateId();



        tblReg.getSelectionModel()
                .selectedItemProperty()
                .addListener((o, ov, sel) -> {

                    if (sel != null) {
                        txtId.setText(sel.getId());
                    }
                });
    }

    // =============================  helper method   ==========================

    private void loadCombos() {

        try {

            ObservableList<String> patientIds = FXCollections.observableArrayList(pbo.getAllIds());
            cmbPatientId.setItems(patientIds);


            ObservableList<String> programIds = FXCollections.observableArrayList(prbo.getAllIds());
            cmbProgramId.setItems(programIds);

        } catch (Exception e) {

            alert("Failed to load combos: " + e.getMessage());
        }

    }



    // Load the table
    private void loadTable() {

        try {

            ObservableList<RegistrationTM> list =
                    FXCollections.observableArrayList();

            bo.getAll().forEach(d -> {

                RegistrationTM tm = new RegistrationTM(
                        d.getId(),
                        d.getPatientName(),
                        d.getProgramName(),
                        d.getRegistrationDate().toString(),
                        d.getFee()
                );

                list.add(tm);
            });

            tblReg.setItems(list);

        } catch (Exception e) {

            alert(e.getMessage());
        }
    }



    //========== generate next id===============
    private void generateId() {

        try {

            txtId.setText(bo.generateNextId());

        } catch (Exception e) {

            txtId.setText("REG001");
        }
    }





    // ======= clear forms
    private void clear() {
        cmbPatientId.setValue(null);
        cmbProgramId.setValue(null);
        dpDate.setValue(LocalDate.now());
        tblReg.getSelectionModel().clearSelection();
    }






    // ===================== Alert =====================
    private void alert(String message) {

        new Alert(Alert.AlertType.ERROR, message).show();
    }

    private void info(String message) {

        new Alert(Alert.AlertType.INFORMATION, message).show();
    }

    private boolean confirm(String message) {

        return new Alert(Alert.AlertType.CONFIRMATION, message).showAndWait().filter(b -> b == ButtonType.OK).isPresent();
    }





    /** Save  button */
    @FXML
    void btnSaveOnAction(ActionEvent e) {

        if (cmbPatientId.getValue() == null || cmbProgramId.getValue() == null || dpDate.getValue() == null) {
            alert("Please fill all required fields.");
            return;
        }


        try {

            RegistrationDTO dto = new RegistrationDTO(
                    txtId.getText().trim(),
                    cmbPatientId.getValue(),
                    null,
                    cmbProgramId.getValue(),
                    null,
                    Date.valueOf(dpDate.getValue()),
                    0
            );

            bo.save(dto);
            info("Patient registered successfully!");
            clear();
            loadTable();
            generateId();

        } catch (Exception ex) {

            alert(ex.getMessage());
        }

    }




    /** Delete button */
    @FXML
    void btnDeleteOnAction(ActionEvent e) {

        if (txtId.getText().isEmpty()) {

            alert("Please select a registration from the table.");

            return;
        }


        if (confirm("Are you sure you want to delete registration " + txtId.getText() + "?")) {

            try {

                bo.delete(txtId.getText());
                info("Registration deleted successfully!");
                clear();
                loadTable();
                generateId();

            } catch (Exception ex) {

                alert(ex.getMessage());
            }
        }
    }




    /** Clear button */
    @FXML
    void btnClearOnAction(ActionEvent e) {
        clear();
        generateId();
    }




}