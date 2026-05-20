package lk.ijse.therapycenter.dao.custom;
import lk.ijse.therapycenter.dao.CrudDAO; import lk.ijse.therapycenter.entity.User; import java.util.Optional;



public interface UserDAO extends CrudDAO<User> {


    Optional<User> findByUsername(String u);
    boolean updateCredentials(String u,String nu,String np);



}













