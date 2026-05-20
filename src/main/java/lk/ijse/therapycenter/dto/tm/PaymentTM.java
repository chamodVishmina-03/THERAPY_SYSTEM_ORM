package lk.ijse.therapycenter.dto.tm;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaymentTM {
    private String id,
            patientName,
            programName,
            paymentDate,
            status;
    private double amount;

}







