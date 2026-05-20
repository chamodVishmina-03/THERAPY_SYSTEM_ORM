package lk.ijse.therapycenter.dao.custom.impl;

import lk.ijse.therapycenter.config.FactoryConfiguration;
import lk.ijse.therapycenter.dao.custom.TherapistDAO;
import lk.ijse.therapycenter.entity.Therapist;
import org.hibernate.Session;

import java.util.List;
import java.util.Optional;

public class TherapistDAOImpl implements TherapistDAO {

    private Session getSession() {

        return FactoryConfiguration.getInstance().getCurrentSession();
    }

    @Override
    public boolean save(Therapist therapist) {
        getSession().persist(therapist);
        return true;
    }

    @Override
    public boolean update(Therapist therapist) {
        getSession().merge(therapist);
        return true;
    }

    @Override
    public boolean delete(String id) {
        Therapist therapist = getSession().find(Therapist.class, id);
        if (therapist != null) {
            getSession().remove(therapist);
            return true;
        }
        return false;
    }

    @Override
    public Optional<Therapist> findById(String id) {
        return Optional.ofNullable(getSession().find(Therapist.class, id));
    }

    @Override
    public List<Therapist> getAll() {

        return getSession().createQuery("FROM Therapist", Therapist.class).list();
    }

    @Override
    public List<String> getAllIds() {

        return getSession().createQuery("SELECT t.id FROM Therapist t", String.class).list();
    }

}

















