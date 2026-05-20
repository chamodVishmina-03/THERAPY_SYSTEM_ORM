package lk.ijse.therapycenter.bo.custom;
import lk.ijse.therapycenter.bo.SuperBO; import lk.ijse.therapycenter.dto.TherapyProgramDTO; import java.util.*;


public interface TherapyProgramBO extends SuperBO {

    boolean save(TherapyProgramDTO d);
    boolean update(TherapyProgramDTO d);
    boolean delete(String id);
    Optional<TherapyProgramDTO> findById(String id);
    List<TherapyProgramDTO> getAll();
    List<String> getAllIds();
    String generateNextId();

}
