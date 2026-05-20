package lk.ijse.therapycenter.bo.custom;
import lk.ijse.therapycenter.bo.SuperBO; import lk.ijse.therapycenter.dto.UserDTO; import java.util.Optional;


public interface UserBO extends SuperBO {

    Optional<UserDTO> login(String u,String p);
    boolean register(UserDTO dto);
    boolean updateCredentials(String cu,String nu,String np);


}
