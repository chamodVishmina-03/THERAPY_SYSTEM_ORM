package lk.ijse.therapycenter.dto;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class TherapyProgramDTO {

    private String id,
            name,
            duration,
            description,
            therapistId,
            therapistName;
    private double fee;

}






























