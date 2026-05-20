package lk.ijse.therapycenter.dao.custom;
import lk.ijse.therapycenter.dao.CrudDAO; import lk.ijse.therapycenter.entity.Registration; import java.util.List;




public interface RegistrationDAO extends CrudDAO<Registration> {

    List<Registration> findByPatientId(String id);

    String getLastId();






}































