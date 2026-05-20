package lk.ijse.therapycenter.dao.custom;
import lk.ijse.therapycenter.dao.CrudDAO; import lk.ijse.therapycenter.entity.TherapySession;
import java.sql.Date; import java.sql.Time; import java.util.List;



public interface TherapySessionDAO extends CrudDAO<TherapySession> {

    List<TherapySession> findByPatientId(String id);
    boolean hasConflict(String tid,Date d,Time t);
    String getLastId();


}














