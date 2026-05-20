package lk.ijse.therapycenter.dao.custom.impl;

import lk.ijse.therapycenter.config.FactoryConfiguration;
import lk.ijse.therapycenter.dao.custom.RegistrationDAO;
import lk.ijse.therapycenter.entity.Registration;
import org.hibernate.Session;

import java.util.List;
import java.util.Optional;

public class RegistrationDAOImpl implements RegistrationDAO {

    private Session getSession() {
        return FactoryConfiguration.getInstance().getCurrentSession();
    }

    @Override
    public boolean save(Registration registration) {
        getSession().persist(registration);
        return true;
    }

    @Override
    public boolean update(Registration registration) {
        getSession().merge(registration);
        return true;
    }

    @Override
    public boolean delete(String id) {
        Registration registration = getSession().find(Registration.class, id);
        if (registration != null) {
            getSession().remove(registration);
            return true;
        }
        return false;
    }

    @Override
    public Optional<Registration> findById(String id) {
        return Optional.ofNullable(getSession().find(Registration.class, id));
    }

    @Override
    public List<Registration> getAll() {

        return getSession()
                .createQuery(
                        "SELECT r FROM Registration r " +
                                "JOIN FETCH r.patient " +
                                "JOIN FETCH r.therapyProgram",
                        Registration.class)
                .list();
    }

    @Override
    public List<String> getAllIds() {
        return getSession()
                .createQuery("SELECT r.id FROM Registration r", String.class)
                .list();
    }

    @Override
    public List<Registration> findByPatientId(String patientId) {

        return getSession()
                .createQuery(
                        "SELECT r FROM Registration r " +
                                "JOIN FETCH r.patient " +
                                "JOIN FETCH r.therapyProgram " +
                                "WHERE r.patient.id = :patientId",
                        Registration.class)
                .setParameter("patientId", patientId)
                .list();
    }

    @Override
    public String getLastId() {
        return getSession()
                .createQuery("SELECT r.id FROM Registration r ORDER BY r.id DESC", String.class)
                .setMaxResults(1)
                .uniqueResult();
    }
}