package lk.ijse.therapycenter.bo.custom;
import lk.ijse.therapycenter.bo.SuperBO; import lk.ijse.therapycenter.dto.TherapySessionDTO; import java.util.*;


public interface TherapySessionBO extends SuperBO {

    boolean save(TherapySessionDTO d);
    boolean update(TherapySessionDTO d);
    boolean delete(String id);
    List<TherapySessionDTO> getAll();
    List<TherapySessionDTO> findByPatientId(String id);
    String generateNextId();


}
