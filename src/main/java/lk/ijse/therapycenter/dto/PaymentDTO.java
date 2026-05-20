package lk.ijse.therapycenter.dto;
import lombok.*; import java.sql.Date;


@AllArgsConstructor
@NoArgsConstructor
@Data

public class PaymentDTO {
    private String id,
            registrationId,
            patientName,
            programName,
            status;
    private double amount;
    private Date paymentDate;



}















