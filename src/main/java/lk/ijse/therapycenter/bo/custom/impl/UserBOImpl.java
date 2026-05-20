package lk.ijse.therapycenter.bo.custom.impl;

import lk.ijse.therapycenter.bo.custom.UserBO;
import lk.ijse.therapycenter.bo.exception.LoginException;
import lk.ijse.therapycenter.config.FactoryConfiguration;
import lk.ijse.therapycenter.dao.DAOFactory;
import lk.ijse.therapycenter.dao.DAOTypes;
import lk.ijse.therapycenter.dao.custom.UserDAO;
import lk.ijse.therapycenter.dto.UserDTO;
import lk.ijse.therapycenter.entity.User;
import lk.ijse.therapycenter.util.PasswordUtil;
import lk.ijse.therapycenter.util.ValidationUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Optional;



public class UserBOImpl implements UserBO {


    private final UserDAO userDAO = DAOFactory.getInstance().getDAO(DAOTypes.USER);




    @Override
    public Optional<UserDTO> login(String username, String password) {
        if (!ValidationUtil.isNotEmpty(username) || !ValidationUtil.isNotEmpty(password)) {
            throw new LoginException("Username and password required.");
        }

        Session session = FactoryConfiguration.getInstance().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Optional<User> optUser = userDAO.findByUsername(username);
            tx.commit();

            if (optUser.isEmpty()) {
                throw new LoginException("Invalid credentials. User not found.");
            }
            if (!PasswordUtil.verifyPassword(password, optUser.get().getPassword())) {
                throw new LoginException("Invalid credentials. Wrong password.");
            }

            User user = optUser.get();
            return Optional.of(new UserDTO(user.getId(), user.getUsername(), null, user.getRole().name()));

        } catch (LoginException e) {
            throw e;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw new LoginException("Login failed: " + e.getMessage());
        }
    }



    @Override
    public boolean register(UserDTO dto) {
        if (!ValidationUtil.isValidUsername(dto.getUsername())) {
            throw new LoginException("Invalid username. Use 4-30 alphanumeric characters.");
        }
        if (!ValidationUtil.isValidPassword(dto.getPassword())) {
            throw new LoginException("Password must be 8+ chars with uppercase, lowercase and digit.");
        }

        Session session = FactoryConfiguration.getInstance().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Optional<User> existing = userDAO.findByUsername(dto.getUsername());
            if (existing.isPresent()) {
                tx.rollback();
                throw new LoginException("Username already exists.");
            }
            String hashedPassword = PasswordUtil.hashPassword(dto.getPassword());
            User newUser = new User(null, dto.getUsername(), hashedPassword, User.Role.valueOf(dto.getRole()));
            userDAO.save(newUser);
            tx.commit();
            return true;

        } catch (LoginException e) {
            throw e;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw new LoginException("Register failed: " + e.getMessage());
        }
    }


    @Override
    public boolean updateCredentials(String currentUsername, String newUsername, String newPassword) {
        if (!ValidationUtil.isValidUsername(newUsername)) {
            throw new LoginException("Invalid new username.");
        }
        if (!ValidationUtil.isValidPassword(newPassword)) {
            throw new LoginException("Password must be 8+ chars with uppercase, lowercase and digit.");
        }

        Session session = FactoryConfiguration.getInstance().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String hashedPassword = PasswordUtil.hashPassword(newPassword);
            boolean result = userDAO.updateCredentials(currentUsername, newUsername, hashedPassword);
            tx.commit();
            return result;

        } catch (LoginException e) {
            throw e;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw new LoginException("Update failed: " + e.getMessage());
        }
    }




}















































