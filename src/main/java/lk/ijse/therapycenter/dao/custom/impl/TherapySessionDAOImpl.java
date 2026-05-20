package lk.ijse.therapycenter.dao.custom.impl;

import lk.ijse.therapycenter.config.FactoryConfiguration;
import lk.ijse.therapycenter.dao.custom.TherapySessionDAO;
import lk.ijse.therapycenter.entity.TherapySession;
import org.hibernate.Session;

import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.Optional;

public class TherapySessionDAOImpl implements TherapySessionDAO {

    private Session getSession() {

        return FactoryConfiguration.getInstance().getCurrentSession();
    }



    @Override
    public boolean save(TherapySession therapySession) {
        getSession().persist(therapySession);
        return true;
    }


    @Override
    public boolean update(TherapySession therapySession) {
        getSession().merge(therapySession);
        return true;
    }



    @Override
    public boolean delete(String id) {
        TherapySession therapySession = getSession().find(TherapySession.class, id);
        if (therapySession != null) {
            getSession().remove(therapySession);
            return true;
        }
        return false;
    }




    @Override
    public Optional<TherapySession> findById(String id) {
        return Optional.ofNullable(getSession().find(TherapySession.class, id));
    }



    @Override
    public List<TherapySession> getAll() {
        return getSession().createQuery("FROM TherapySession", TherapySession.class).list();
    }


    @Override
    public List<String> getAllIds() {
        return getSession().createQuery("SELECT s.id FROM TherapySession s", String.class).list();
    }


    @Override
    public List<TherapySession> findByPatientId(String patientId) {
        return getSession()
                .createQuery("FROM TherapySession s WHERE s.patient.id = :patientId", TherapySession.class)
                .setParameter("patientId", patientId)
                .list();
    }



    // Therapist  same date and time confit check
    @Override
    public boolean hasConflict(String therapistId, Date date, Time time) {

        Long count = getSession()
                .createQuery(
                        "SELECT COUNT(s) FROM TherapySession s " +
                        "WHERE s.therapist.id = :therapistId " +
                        "AND s.sessionDate = :date " +
                        "AND s.sessionTime = :time " +
                        "AND s.status != 'CANCELLED'",
                        Long.class)

                .setParameter("therapistId", therapistId)
                .setParameter("date", date)
                .setParameter("time", time)
                .uniqueResult();

        return count > 0;

    }



    @Override
    public String getLastId() {
        return getSession()
                .createQuery("SELECT s.id FROM TherapySession s ORDER BY s.id DESC", String.class)
                .setMaxResults(1)
                .uniqueResult();

    }





}












