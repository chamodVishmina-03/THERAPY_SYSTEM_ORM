package lk.ijse.therapycenter.dao;
import java.util.List; import java.util.Optional;



public interface CrudDAO<T> extends SuperDAO {

    boolean save(T e);
    boolean update(T e);
    boolean delete(String id);
    Optional<T> findById(String id);
    List<T> getAll();
    List<String> getAllIds();


}






