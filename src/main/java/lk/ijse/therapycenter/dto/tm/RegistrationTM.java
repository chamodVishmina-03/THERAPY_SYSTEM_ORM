package lk.ijse.therapycenter.dto.tm;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegistrationTM {
    private String id,
            patientName,
            programName,
            registrationDate;
    private double fee;


}














