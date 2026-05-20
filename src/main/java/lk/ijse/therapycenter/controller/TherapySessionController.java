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
import lk.ijse.therapycenter.bo.custom.TherapistBO;
import lk.ijse.therapycenter.bo.custom.TherapyProgramBO;
import lk.ijse.therapycenter.bo.custom.TherapySessionBO;
import lk.ijse.therapycenter.bo.exception.SchedulingConflictException;
import lk.ijse.therapycenter.dto.TherapySessionDTO;
import lk.ijse.therapycenter.dto.tm.SessionTM;

import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class TherapySessionController implements Initializable {



    // ===================== FXML fields =====================
    @FXML
    private TextField txtId;
    @FXML
    private TextField txtTime;
    @FXML
    private ComboBox<String> cmbPatientId;
    @FXML
    private ComboBox<String> cmbTherapistId;
    @FXML
    private ComboBox<String> cmbProgramId;
    @FXML
    private ComboBox<String> cmbStatus;
    @FXML
    private DatePicker dpDate;




    @FXML
    private Button btnSave;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnClear;




    @FXML
    private TableView<SessionTM> tblSession;
    @FXML
    private TableColumn<SessionTM, String> colId;
    @FXML
    private TableColumn<SessionTM, String> colPatient;
    @FXML
    private TableColumn<SessionTM, String> colTherapist;
    @FXML
    private TableColumn<SessionTM, String> colProgram;
    @FXML
    private TableColumn<SessionTM, String> colDate;
    @FXML
    private TableColumn<SessionTM, String> colTime;
    @FXML
    private TableColumn<SessionTM, String> colStatus;



    // ===================== BO layer =====================
    private final TherapySessionBO bo   = BOFactory.getInstance().getBO(BOTypes.THERAPY_SESSION);
    private final PatientBO        pbo  = BOFactory.getInstance().getBO(BOTypes.PATIENT);
    private final TherapistBO      tbo  = BOFactory.getInstance().getBO(BOTypes.THERAPIST);
    private final TherapyProgramBO prbo = BOFactory.getInstance().getBO(BOTypes.THERAPY_PROGRAM);




    // ===================== Initialize =====================
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Table columns setup
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colPatient.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        colTherapist.setCellValueFactory(new PropertyValueFactory<>("therapistName"));
        colProgram.setCellValueFactory(new PropertyValueFactory<>("programName"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("sessionDate"));
        colTime.setCellValueFactory(new PropertyValueFactory<>("sessionTime"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));


        cmbStatus.setItems(FXCollections.observableArrayList(
                "SCHEDULED", "COMPLETED", "CANCELLED", "RESCHEDULED"
        ));
        cmbStatus.setValue("SCHEDULED");


        dpDate.setValue(LocalDate.now());
        txtTime.setPromptText("09:00:00");

        loadCombos();
        loadTable();
        generateId();

        // ==========   Table row select ======
        tblSession.getSelectionModel().selectedItemProperty().addListener((o, ov, sel) -> {
            if (sel != null) {
                txtId.setText(sel.getId());
                cmbStatus.setValue(sel.getStatus());
            }
        });
    }





    // ===================== Private Helper Methods =====================


    /**  ids generate  */
    private void loadCombos() {
        try {
            cmbPatientId.setItems(FXCollections.observableArrayList(pbo.getAllIds()));
            cmbTherapistId.setItems(FXCollections.observableArrayList(tbo.getAllIds()));
            cmbProgramId.setItems(FXCollections.observableArrayList(prbo.getAllIds()));
        } catch (Exception e) {
            alert("Failed to load combos: " + e.getMessage());
        }
    }


    /**  load table  */
    private void loadTable() {
        try {
            ObservableList<SessionTM> list = FXCollections.observableArrayList();
            bo.getAll().forEach(d -> list.add(new SessionTM(
                    d.getId(),
                    d.getPatientName(),
                    d.getTherapistName(),
                    d.getProgramName(),
                    d.getSessionDate().toString(),
                    d.getSessionTime().toString(),
                    d.getStatus()
            )));
            tblSession.setItems(list);
        } catch (Exception e) {
            alert(e.getMessage());
        }
    }


   // -======  auto generate id
    private void generateId() {
        try {
            txtId.setText(bo.generateNextId());
        } catch (Exception e) {
            txtId.setText("SES001");
        }
    }



    private boolean validate() {
        if (cmbPatientId.getValue() == null) {
            alert("Please select a patient.");
            return false;
        }
        if (cmbTherapistId.getValue() == null) {
            alert("Please select a therapist.");
            return false;
        }
        if (cmbProgramId.getValue() == null) {
            alert("Please select a therapy program.");
            return false;
        }
        if (!txtTime.getText().matches("\\d{2}:\\d{2}:\\d{2}")) {
            alert("Invalid time format. Use HH:MM:SS (e.g. 09:00:00)");
            return false;
        }
        return true;
    }



    private TherapySessionDTO getData() {
        return new TherapySessionDTO(
                txtId.getText().trim(),
                cmbPatientId.getValue(),
                null,
                cmbTherapistId.getValue(),
                null,
                cmbProgramId.getValue(),
                null,
                cmbStatus.getValue(),
                Date.valueOf(dpDate.getValue()),
                Time.valueOf(txtTime.getText().trim())
        );
    }



   //  form clear
    private void clear() {
        cmbPatientId.setValue(null);
        cmbTherapistId.setValue(null);
        cmbProgramId.setValue(null);
        cmbStatus.setValue("SCHEDULED");
        dpDate.setValue(LocalDate.now());
        txtTime.clear();
        tblSession.getSelectionModel().clearSelection();
    }



    // =====================    Alert   =====================
    private void alert(String message) {

        new Alert(Alert.AlertType.ERROR, message).show();
    }
    private void info(String message)  {
        new Alert(Alert.AlertType.INFORMATION, message).show();

    }
    private boolean confirm(String message) {
        return new Alert(Alert.AlertType.CONFIRMATION, message)
                .showAndWait().filter(b -> b == ButtonType.OK).isPresent();
    }





    // ===================== Button Actions =====================
    @FXML
    void btnSaveOnAction(ActionEvent e) {
        if (!validate()) return;
        try {
            bo.save(getData());
            info("Session scheduled successfully!");
            clear();
            loadTable();
            generateId();
        } catch (SchedulingConflictException ex) {

            alert("Scheduling conflict: " + ex.getMessage());
        } catch (Exception ex) {
            alert(ex.getMessage());
        }
    }





    /** Update button  */
    @FXML
    void btnUpdateOnAction(ActionEvent e) {
        if (!validate()) return;
        try {
            bo.update(getData());
            info("Session updated successfully!");
            clear();
            loadTable();
        } catch (Exception ex) {
            alert(ex.getMessage());
        }
    }




    /**      Delete button */
    @FXML
    void btnDeleteOnAction(ActionEvent e) {
        if (txtId.getText().isEmpty()) {
            alert("Please select a session from the table.");
            return;
        }
        if (confirm("Are you sure you want to delete session " + txtId.getText() + "?")) {
            try {
                bo.delete(txtId.getText());
                info("Session deleted successfully!");
                clear();
                loadTable();
                generateId();
            } catch (Exception ex) {
                alert(ex.getMessage());
            }
        }
    }




    /**   Clear button  */
    @FXML
    void btnClearOnAction(ActionEvent e) {
        clear();
        generateId();
    }



}
















