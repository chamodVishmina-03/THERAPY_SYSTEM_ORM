package lk.ijse.therapycenter.bo.custom;
import lk.ijse.therapycenter.bo.SuperBO; import lk.ijse.therapycenter.dto.RegistrationDTO; import java.util.*;



public interface RegistrationBO extends SuperBO {

    boolean save(RegistrationDTO d);
    boolean delete(String id);
    List<RegistrationDTO> getAll();
    List<RegistrationDTO> findByPatientId(String id);
    String generateNextId();


}
