package lk.ijse.therapycenter.dto;
import lombok.*; import java.sql.Date;


@AllArgsConstructor
@NoArgsConstructor
@Data

public class RegistrationDTO {


    private String id,
            patientId,
            patientName,
            programId,
            programName;
    private Date registrationDate;
    private double fee;


}

















