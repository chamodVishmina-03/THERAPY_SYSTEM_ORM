package lk.ijse.therapycenter.bo.custom;
import lk.ijse.therapycenter.bo.SuperBO; import lk.ijse.therapycenter.dto.PaymentDTO; import java.util.*;



public interface PaymentBO extends SuperBO {


    boolean save(PaymentDTO d);
    boolean update(PaymentDTO d);
    List<PaymentDTO> getAll();
    List<PaymentDTO> findByRegistrationId(String id);
    String generateNextId();



}
