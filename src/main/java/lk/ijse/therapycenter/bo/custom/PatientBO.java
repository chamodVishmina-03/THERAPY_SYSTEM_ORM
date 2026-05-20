package lk.ijse.therapycenter.bo.custom;
import lk.ijse.therapycenter.bo.SuperBO; import lk.ijse.therapycenter.dto.PatientDTO; import java.util.*;


public interface PatientBO extends SuperBO {

    boolean save(PatientDTO d);
    boolean update(PatientDTO d);
    boolean delete(String id);
    Optional<PatientDTO> findById(String id);
    List<PatientDTO> getAll();
    List<String> getAllIds(); String generateNextId();
    List<PatientDTO> getPatientsEnrolledInAllPrograms();
    List<PatientDTO> getPatientsWithPrograms();

}
