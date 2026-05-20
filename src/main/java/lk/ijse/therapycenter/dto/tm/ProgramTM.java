package lk.ijse.therapycenter.dto.tm;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProgramTM {
    private String id,
            name,
            duration,
            therapistName;

    private double fee;
}













