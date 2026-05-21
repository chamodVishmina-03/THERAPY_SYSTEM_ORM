package lk.ijse.therapycenter.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.therapycenter.bo.BOFactory;
import lk.ijse.therapycenter.bo.BOTypes;
import lk.ijse.therapycenter.bo.custom.PaymentBO;
import lk.ijse.therapycenter.dto.PaymentDTO;
import lk.ijse.therapycenter.dto.tm.PaymentTM;

import java.net.URL;
import java.util.ResourceBundle;

public class ReportController implements Initializable {

    @FXML
    private Label lblTotalRevenue;

    @FXML
    private Label lblCompletedPayments;

    @FXML
    private Label lblPendingPayments;

    @FXML
    private TableView<PaymentTM> tblReport;

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

    private final PaymentBO bo =
            BOFactory.getInstance().getBO(BOTypes.PAYMENT);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        colPatient.setCellValueFactory(
                new PropertyValueFactory<>("patientName")
        );

        colProgram.setCellValueFactory(
                new PropertyValueFactory<>("programName")
        );

        colAmount.setCellValueFactory(
                new PropertyValueFactory<>("amount")
        );

        colDate.setCellValueFactory(
                new PropertyValueFactory<>("paymentDate")
        );

        colStatus.setCellValueFactory(
                new PropertyValueFactory<>("status")
        );

        loadTable();

        loadSummary();
    }

    private void loadTable() {

        try {

            ObservableList<PaymentTM> list =
                    FXCollections.observableArrayList();

            for (PaymentDTO d : bo.getAll()) {

                PaymentTM tm = new PaymentTM(
                        d.getId(),
                        d.getPatientName(),
                        d.getProgramName(),
                        d.getPaymentDate().toString(),
                        d.getStatus(),
                        d.getAmount()
                );

                list.add(tm);
            }

            tblReport.setItems(list);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private void loadSummary() {

        try {

            double totalRevenue = 0;

            int completedCount = 0;

            int pendingCount = 0;

            for (PaymentDTO d : bo.getAll()) {

                totalRevenue += d.getAmount();

                if (d.getStatus().equalsIgnoreCase("COMPLETED")) {

                    completedCount++;

                } else if (d.getStatus().equalsIgnoreCase("PENDING")) {

                    pendingCount++;
                }
            }

            lblTotalRevenue.setText("LKR " + totalRevenue);

            lblCompletedPayments.setText(
                    String.valueOf(completedCount)
            );

            lblPendingPayments.setText(
                    String.valueOf(pendingCount)
            );

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}