package lk.ijse.therapycenter.dto;
import lombok.*; import java.sql.Date; import java.sql.Time;


@AllArgsConstructor
@NoArgsConstructor
@Data

public class TherapySessionDTO {
    private String id,
            patientId,
            patientName,
            therapistId,
            therapistName,
            programId,
            programName,
            status;

    private Date sessionDate;
    private Time sessionTime;

}










