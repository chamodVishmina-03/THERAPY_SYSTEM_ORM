package lk.ijse.therapycenter.dao.custom.impl;

import lk.ijse.therapycenter.config.FactoryConfiguration;
import lk.ijse.therapycenter.dao.custom.PatientDAO;
import lk.ijse.therapycenter.entity.Patient;
import org.hibernate.Session;

import java.util.List;
import java.util.Optional;


public class PatientDAOImpl implements PatientDAO {


    private Session getSession() {

        return FactoryConfiguration.getInstance().getCurrentSession();
    }


    @Override
    public boolean save(Patient patient) {

        getSession().persist(patient);
        return true;
    }


    @Override
    public boolean update(Patient patient) {
        getSession().merge(patient);
        return true;
    }


    @Override
    public boolean delete(String id) {
        Patient patient = getSession().find(Patient.class, id);
        if (patient != null) {
            getSession().remove(patient);
            return true;
        }
        return false;
    }


    @Override
    public Optional<Patient> findById(String id) {

        return Optional.ofNullable(getSession().find(Patient.class, id));
    }


    @Override
    public List<Patient> getAll() {

        return getSession().createQuery("FROM Patient", Patient.class).list();
    }



    @Override
    public List<String> getAllIds() {

        return getSession().createQuery("SELECT p.id FROM Patient p", String.class).list();
    }


    @Override
    public List<Patient> getPatientsEnrolledInAllPrograms() {
        String hql = "FROM Patient p WHERE " +
                "(SELECT COUNT(DISTINCT r.therapyProgram.id) FROM Registration r WHERE r.patient = p) = " +
                "(SELECT COUNT(tp.id) FROM TherapyProgram tp)";
        return getSession().createQuery(hql, Patient.class).list();
    }


    @Override
    public List<Patient> getPatientsWithPrograms() {
        String hql = "SELECT DISTINCT p FROM Patient p " +
                "LEFT JOIN FETCH p.registrations r " +
                "LEFT JOIN FETCH r.therapyProgram";
        return getSession().createQuery(hql, Patient.class).list();
    }
}
