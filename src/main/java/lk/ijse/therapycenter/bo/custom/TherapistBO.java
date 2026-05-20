package lk.ijse.therapycenter.bo.custom;
import lk.ijse.therapycenter.bo.SuperBO; import lk.ijse.therapycenter.dto.TherapistDTO; import java.util.*;


public interface TherapistBO extends SuperBO {


    boolean save(TherapistDTO d);
    boolean update(TherapistDTO d);
    boolean delete(String id);
    Optional<TherapistDTO> findById(String id);
    List<TherapistDTO> getAll();
    List<String> getAllIds();
    String generateNextId();


}
