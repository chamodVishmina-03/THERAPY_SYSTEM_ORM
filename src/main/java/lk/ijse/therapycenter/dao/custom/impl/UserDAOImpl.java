package lk.ijse.therapycenter.dao.custom.impl;

import lk.ijse.therapycenter.config.FactoryConfiguration;
import lk.ijse.therapycenter.dao.custom.UserDAO;
import lk.ijse.therapycenter.entity.User;
import org.hibernate.Session;

import java.util.List;
import java.util.Optional;

public class UserDAOImpl implements UserDAO {

    private Session getSession() {

        return FactoryConfiguration.getInstance().getCurrentSession();
    }


    @Override
    public boolean save(User user) {
        getSession().persist(user);
        return true;
    }



    @Override
    public boolean update(User user) {
        getSession().merge(user);
        return true;
    }



    @Override
    public boolean delete(String id) {
        User user = getSession().find(User.class, Integer.parseInt(id));
        if (user != null) {
            getSession().remove(user);
            return true;
        }
        return false;
    }



    @Override
    public Optional<User> findById(String id) {
        return Optional.ofNullable(getSession().find(User.class, Integer.parseInt(id)));
    }



    @Override
    public List<User> getAll() {

        return getSession().createQuery("FROM User", User.class).list();
    }



    @Override
    public List<String> getAllIds() {

        return getSession().createQuery("SELECT CAST(u.id AS string) FROM User u", String.class).list();
    }


    @Override
    public Optional<User> findByUsername(String username) {
        return getSession()
                .createQuery("FROM User u WHERE u.username = :username", User.class)
                .setParameter("username", username)
                .uniqueResultOptional();

    }



    @Override
    public boolean updateCredentials(String currentUsername, String newUsername, String newPassword) {

        int updatedRows = getSession()

                .createQuery("UPDATE User u SET u.username = :newUsername, u.password = :newPassword WHERE u.username = :currentUsername")
                .setParameter("newUsername", newUsername)
                .setParameter("newPassword", newPassword)
                .setParameter("currentUsername", currentUsername)
                .executeUpdate();

        return updatedRows > 0;

    }









}












