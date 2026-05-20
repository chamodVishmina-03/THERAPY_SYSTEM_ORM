package lk.ijse.therapycenter.bo.custom.impl;

import lk.ijse.therapycenter.bo.custom.PatientBO;
import lk.ijse.therapycenter.bo.exception.DuplicateException;
import lk.ijse.therapycenter.bo.exception.NotFoundException;
import lk.ijse.therapycenter.config.FactoryConfiguration;
import lk.ijse.therapycenter.dao.DAOFactory;
import lk.ijse.therapycenter.dao.DAOTypes;
import lk.ijse.therapycenter.dao.custom.PatientDAO;
import lk.ijse.therapycenter.dto.PatientDTO;
import lk.ijse.therapycenter.entity.Patient;
import lk.ijse.therapycenter.util.IdGeneratorUtil;
import lk.ijse.therapycenter.util.ValidationUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class PatientBOImpl implements PatientBO {

    private final PatientDAO patientDAO = DAOFactory.getInstance().getDAO(DAOTypes.PATIENT);

    private PatientDTO toDTO(Patient patient) {
        return new PatientDTO(
                patient.getId(),
                patient.getName(),
                patient.getEmail(),
                patient.getPhone(),
                patient.getAddress(),
                patient.getMedicalHistory()
        );
    }


    private Patient toEntity(PatientDTO dto) {
        return new Patient(
                dto.getId(),
                dto.getName(),
                dto.getEmail(),
                dto.getPhone(),
                dto.getAddress(),
                dto.getMedicalHistory(),
                null,
                null
        );
    }






    @Override
    public boolean save(PatientDTO dto) {
        if (!ValidationUtil.isValidName(dto.getName())) throw new DuplicateException("Invalid name.");
        if (!ValidationUtil.isValidEmail(dto.getEmail())) throw new DuplicateException("Invalid email.");
        if (!ValidationUtil.isValidPhone(dto.getPhone())) throw new DuplicateException("Invalid phone.");

        Session session = FactoryConfiguration.getInstance().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            patientDAO.save(toEntity(dto));
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw new DuplicateException(e.getMessage());
        }
    }


    @Override
    public boolean update(PatientDTO dto) {
        Session session = FactoryConfiguration.getInstance().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            patientDAO.update(toEntity(dto));
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw new NotFoundException(e.getMessage());
        }
    }



    @Override
    public boolean delete(String id) {
        Session session = FactoryConfiguration.getInstance().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            boolean result = patientDAO.delete(id);
            tx.commit();
            if (!result) throw new NotFoundException("Patient not found.");
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
    public Optional<PatientDTO> findById(String id) {
        Session session = FactoryConfiguration.getInstance().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Optional<Patient> patient = patientDAO.findById(id);
            tx.commit();
            return patient.map(this::toDTO);
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw new NotFoundException(e.getMessage());
        }
    }



    @Override
    public List<PatientDTO> getAll() {
        Session session = FactoryConfiguration.getInstance().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List<Patient> patients = patientDAO.getAll();
            tx.commit();
            return patients.stream().map(this::toDTO).collect(Collectors.toList());
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw new NotFoundException(e.getMessage());
        }
    }




    @Override
    public List<String> getAllIds() {
        Session session = FactoryConfiguration.getInstance().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List<String> ids = patientDAO.getAllIds();
            tx.commit();
            return ids;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            return new ArrayList<>();
        }
    }




    @Override
    public String generateNextId() {
        Session session = FactoryConfiguration.getInstance().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List<String> ids = patientDAO.getAllIds();
            tx.commit();
            String lastId = ids.isEmpty() ? null : ids.stream().sorted().reduce((a, b) -> b).orElse(null);
            return IdGeneratorUtil.generateNextId("PAT", lastId);
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            return "PAT001";
        }
    }




    @Override
    public List<PatientDTO> getPatientsEnrolledInAllPrograms() {
        Session session = FactoryConfiguration.getInstance().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List<Patient> patients = patientDAO.getPatientsEnrolledInAllPrograms();
            tx.commit();
            return patients.stream().map(this::toDTO).collect(Collectors.toList());
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw new NotFoundException(e.getMessage());
        }
    }



    @Override
    public List<PatientDTO> getPatientsWithPrograms() {
        Session session = FactoryConfiguration.getInstance().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List<Patient> patients = patientDAO.getPatientsWithPrograms();
            tx.commit();
            return patients.stream().map(this::toDTO).collect(Collectors.toList());
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw new NotFoundException(e.getMessage());
        }
    }





}
