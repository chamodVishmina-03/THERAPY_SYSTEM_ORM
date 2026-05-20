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
import lk.ijse.therapycenter.bo.custom.TherapyProgramBO;
import lk.ijse.therapycenter.dto.TherapyProgramDTO;
import lk.ijse.therapycenter.dto.tm.ProgramTM;

import java.net.URL;
import java.util.ResourceBundle;

public class TherapyProgramController implements Initializable {

    // ===================== FXML fields =====================
    @FXML
    private TextField txtId;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtDuration;
    @FXML
    private TextField txtFee;
    @FXML
    private TextArea txtDescription;
    @FXML
    private ComboBox<String> cmbTherapistId;



    @FXML
    private Button btnSave;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnClear;




    // ===================== Tble  fields =====================

    @FXML
    private TableView<ProgramTM> tblProgram;
    @FXML
    private TableColumn<ProgramTM, String> colId;
    @FXML
    private TableColumn<ProgramTM, String> colName;
    @FXML
    private TableColumn<ProgramTM, String> colDuration;
    @FXML
    private TableColumn<ProgramTM, Double> colFee;
    @FXML
    private TableColumn<ProgramTM, String> colTherapist;
    @FXML
    private TableColumn<ProgramTM, String> colDescription;

    // ===================== BO layer ====
    private final TherapyProgramBO bo  = BOFactory.getInstance().getBO(BOTypes.THERAPY_PROGRAM);
    private final TherapistBO      tbo = BOFactory.getInstance().getBO(BOTypes.THERAPIST);



    // ===================== Initialize ========
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Table columns setup
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDuration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        colFee.setCellValueFactory(new PropertyValueFactory<>("fee"));
        colTherapist.setCellValueFactory(new PropertyValueFactory<>("therapistName"));

        loadCombo();
        loadTable();
        generateId();


                    // Table row select
        tblProgram.getSelectionModel().selectedItemProperty().addListener((o, ov, sel) -> {

            if (sel != null) {

                txtId.setText(sel.getId());
                txtName.setText(sel.getName());
                txtDuration.setText(sel.getDuration());
                txtFee.setText(String.valueOf(sel.getFee()));


                try {


                    bo.findById(sel.getId()).ifPresent(d -> {
                        txtDescription.setText(d.getDescription());
                        cmbTherapistId.setValue(d.getTherapistId());
                    });



                } catch (Exception ignored) {}
            }

        });



    }





    // ===================== Helper methods =====================

    //  therapist id load for select
    private void loadCombo() {

        try {

            cmbTherapistId.setItems(FXCollections.observableArrayList(tbo.getAllIds()));

        } catch (Exception e) {
            alert("Failed to load therapists: " + e.getMessage());
        }
    }




      //  load tables
    private void loadTable() {
        try {


            ObservableList<ProgramTM> list = FXCollections.observableArrayList();
            bo.getAll().forEach(d -> list.add(new ProgramTM(
                    d.getId(), d.getName(), d.getDuration(), d.getTherapistName(), d.getFee()
            )));

            tblProgram.setItems(list);


        } catch (Exception e) {
            alert(e.getMessage());
        }


    }



                    // Program id  auto-generate
    private void generateId() {
        try {
            txtId.setText(bo.generateNextId());
        } catch (Exception e) {
            txtId.setText("P001");
        }
    }


    private boolean validate() {
        if (txtName.getText().trim().isEmpty()) {
            alert("Program name is required.");
            return false;
        }
        if (txtDuration.getText().trim().isEmpty()) {
            alert("Duration is required.");
            return false;
        }


        try {
            Double.parseDouble(txtFee.getText().trim());


        } catch (Exception e) {
            alert("Invalid fee amount. Enter a valid number.");
            return false;
        }
        return true;

    }





    private TherapyProgramDTO getData() {

        return new TherapyProgramDTO(
                txtId.getText().trim(),
                txtName.getText().trim(),
                txtDuration.getText().trim(),
                txtDescription.getText().trim(),
                cmbTherapistId.getValue(),
                null,
                Double.parseDouble(txtFee.getText().trim())
        );

    }






    //  fields clear
    private void clear() {
        txtName.clear();
        txtDuration.clear();
        txtFee.clear();
        txtDescription.clear();
        cmbTherapistId.setValue(null);
        tblProgram.getSelectionModel().clearSelection();
    }



    // ===================== Alert =====================
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








    // Save button
    @FXML
    void btnSaveOnAction(ActionEvent e) {

        if (!validate()) return;

        try {

            bo.save(getData());
            info("Program saved successfully!");
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
            info("Program updated successfully!");
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
            alert("Please select a program from the table.");
            return;
        }


        if (confirm("Are you sure you want to delete program " + txtId.getText() + "?")) {

            try {
                bo.delete(txtId.getText());
                info("Program deleted successfully!");
                clear();
                loadTable();
                generateId();

            } catch (Exception ex) {
                alert(ex.getMessage());
            }
        }
    }


       //  Clear button
    @FXML
    void btnClearOnAction(ActionEvent e) {
        clear();
        generateId();
    }


}













