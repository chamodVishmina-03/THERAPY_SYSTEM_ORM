package lk.ijse.therapycenter.bo.custom.impl;

import lk.ijse.therapycenter.bo.custom.RegistrationBO;
import lk.ijse.therapycenter.bo.exception.DuplicateException;
import lk.ijse.therapycenter.bo.exception.NotFoundException;
import lk.ijse.therapycenter.config.FactoryConfiguration;
import lk.ijse.therapycenter.dao.DAOFactory;
import lk.ijse.therapycenter.dao.DAOTypes;
import lk.ijse.therapycenter.dao.custom.PatientDAO;
import lk.ijse.therapycenter.dao.custom.RegistrationDAO;
import lk.ijse.therapycenter.dao.custom.TherapyProgramDAO;
import lk.ijse.therapycenter.dto.RegistrationDTO;
import lk.ijse.therapycenter.entity.Patient;
import lk.ijse.therapycenter.entity.Registration;
import lk.ijse.therapycenter.entity.TherapyProgram;
import lk.ijse.therapycenter.util.IdGeneratorUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.stream.Collectors;

public class RegistrationBOImpl implements RegistrationBO {

    private final RegistrationDAO   registrationDAO = DAOFactory.getInstance().getDAO(DAOTypes.REGISTRATION);
    private final PatientDAO        patientDAO      = DAOFactory.getInstance().getDAO(DAOTypes.PATIENT);
    private final TherapyProgramDAO programDAO      = DAOFactory.getInstance().getDAO(DAOTypes.THERAPY_PROGRAM);



    private RegistrationDTO toDTO(Registration registration) {
        double fee = registration.getTherapyProgram() != null ? registration.getTherapyProgram().getFee() : 0;
        return new RegistrationDTO(

                registration.getId(),
                registration.getPatient().getId(),
                registration.getPatient().getName(),
                registration.getTherapyProgram().getId(),
                registration.getTherapyProgram().getName(),
                registration.getRegistrationDate(),
                fee

        );
    }


    @Override
    public boolean save(RegistrationDTO dto) {
        Session session = FactoryConfiguration.getInstance().getCurrentSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();

            Patient patient = patientDAO.findById(dto.getPatientId())
                    .orElseThrow(() -> new NotFoundException("Patient not found."));

            TherapyProgram program = programDAO.findById(dto.getProgramId())
                    .orElseThrow(() -> new NotFoundException("Therapy program not found."));

            Registration registration = new Registration(
                    dto.getId(), patient, program, dto.getRegistrationDate(), null
            );
            registrationDAO.save(registration);
            tx.commit();
            return true;


        } catch (NotFoundException e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw e;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw new DuplicateException(e.getMessage());
        }
    }



    @Override
    public boolean delete(String id) {
        Session session = FactoryConfiguration.getInstance().getCurrentSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            boolean result = registrationDAO.delete(id);
            tx.commit();
            if (!result) throw new NotFoundException("Registration not found.");
            return true;


        } catch (NotFoundException e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw e;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw new NotFoundException(e.getMessage());
        }
    }

    @Override
    public List<RegistrationDTO> getAll() {
        Session session = FactoryConfiguration.getInstance().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List<Registration> registrations = registrationDAO.getAll();
            tx.commit();
            return registrations.stream().map(this::toDTO).collect(Collectors.toList());
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw new NotFoundException(e.getMessage());
        }
    }


    @Override
    public List<RegistrationDTO> findByPatientId(String patientId) {
        Session session = FactoryConfiguration.getInstance().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List<Registration> registrations = registrationDAO.findByPatientId(patientId);
            tx.commit();
            return registrations.stream().map(this::toDTO).collect(Collectors.toList());
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw new NotFoundException(e.getMessage());
        }
    }


    @Override
    public String generateNextId() {
        Session session = FactoryConfiguration.getInstance().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String lastId = registrationDAO.getLastId();
            tx.commit();
            return IdGeneratorUtil.generateNextId("REG", lastId);
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            return "REG001";
        }
    }

}


































