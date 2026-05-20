package lk.ijse.therapycenter.dto;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO {
    private Integer id;
    private String username;
    private String password;
    private String role;

}
