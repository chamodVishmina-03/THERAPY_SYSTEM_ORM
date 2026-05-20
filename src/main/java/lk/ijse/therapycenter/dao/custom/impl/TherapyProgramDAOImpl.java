package lk.ijse.therapycenter.dao.custom.impl;

import lk.ijse.therapycenter.config.FactoryConfiguration;
import lk.ijse.therapycenter.dao.custom.TherapyProgramDAO;
import lk.ijse.therapycenter.entity.TherapyProgram;
import org.hibernate.Session;

import java.util.List;
import java.util.Optional;

public class TherapyProgramDAOImpl implements TherapyProgramDAO {


    private Session getSession() {

        return FactoryConfiguration.getInstance().getCurrentSession();
    }



    @Override
    public boolean save(TherapyProgram program) {
        getSession().persist(program);
        return true;
    }



    @Override
    public boolean update(TherapyProgram program) {
        getSession().merge(program);
        return true;
    }



    @Override
    public boolean delete(String id) {
        TherapyProgram program = getSession().find(TherapyProgram.class, id);
        if (program != null) {
            getSession().remove(program);
            return true;
        }
        return false;
    }



    @Override
    public Optional<TherapyProgram> findById(String id) {
        return Optional.ofNullable(getSession().find(TherapyProgram.class, id));
    }




    @Override
    public List<TherapyProgram> getAll() {
        return getSession().createQuery("FROM TherapyProgram", TherapyProgram.class).list();
    }


    @Override
    public List<String> getAllIds() {
        return getSession().createQuery("SELECT p.id FROM TherapyProgram p", String.class).list();
    }




}
















