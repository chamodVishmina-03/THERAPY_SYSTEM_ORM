package lk.ijse.therapycenter.dao.custom.impl;

import lk.ijse.therapycenter.config.FactoryConfiguration;
import lk.ijse.therapycenter.dao.custom.PaymentDAO;
import lk.ijse.therapycenter.entity.Payment;
import org.hibernate.Session;

import java.util.List;
import java.util.Optional;

public class PaymentDAOImpl implements PaymentDAO {

    private Session getSession() {
        return FactoryConfiguration.getInstance().getCurrentSession();
    }

    @Override
    public boolean save(Payment payment) {
        getSession().persist(payment);
        return true;
    }

    @Override
    public boolean update(Payment payment) {
        getSession().merge(payment);
        return true;
    }

    @Override
    public boolean delete(String id) {
        Payment payment = getSession().find(Payment.class, id);
        if (payment != null) {
            getSession().remove(payment);
            return true;
        }
        return false;
    }

    @Override
    public Optional<Payment> findById(String id) {
        return Optional.ofNullable(getSession().find(Payment.class, id));
    }

    @Override
    public List<Payment> getAll() {

        return getSession()
                .createQuery(
                        "SELECT p FROM Payment p " +
                                "JOIN FETCH p.registration r " +
                                "JOIN FETCH r.patient " +
                                "JOIN FETCH r.therapyProgram",
                        Payment.class)
                .list();
    }

    @Override
    public List<String> getAllIds() {
        return getSession()
                .createQuery("SELECT p.id FROM Payment p", String.class)
                .list();
    }

    @Override
    public List<Payment> findByRegistrationId(String registrationId) {

        return getSession()
                .createQuery(
                        "SELECT p FROM Payment p " +
                                "JOIN FETCH p.registration r " +
                                "JOIN FETCH r.patient " +
                                "JOIN FETCH r.therapyProgram " +
                                "WHERE r.id = :registrationId",
                        Payment.class)
                .setParameter("registrationId", registrationId)
                .list();
    }

    @Override
    public String getLastId() {
        return getSession()
                .createQuery("SELECT p.id FROM Payment p ORDER BY p.id DESC", String.class)
                .setMaxResults(1)
                .uniqueResult();
    }
}