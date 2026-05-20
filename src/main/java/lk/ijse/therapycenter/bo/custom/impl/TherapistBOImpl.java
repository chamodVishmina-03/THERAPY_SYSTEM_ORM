package lk.ijse.therapycenter.bo.custom.impl;

import lk.ijse.therapycenter.bo.custom.TherapistBO;
import lk.ijse.therapycenter.bo.exception.DuplicateException;
import lk.ijse.therapycenter.bo.exception.NotFoundException;
import lk.ijse.therapycenter.config.FactoryConfiguration;
import lk.ijse.therapycenter.dao.DAOFactory;
import lk.ijse.therapycenter.dao.DAOTypes;
import lk.ijse.therapycenter.dao.custom.TherapistDAO;
import lk.ijse.therapycenter.dto.TherapistDTO;
import lk.ijse.therapycenter.entity.Therapist;
import lk.ijse.therapycenter.util.IdGeneratorUtil;
import lk.ijse.therapycenter.util.ValidationUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TherapistBOImpl implements TherapistBO {

    private final TherapistDAO therapistDAO = DAOFactory.getInstance().getDAO(DAOTypes.THERAPIST);

    private TherapistDTO toDTO(Therapist therapist) {
        return new TherapistDTO(
                therapist.getId(),
                therapist.getName(),
                therapist.getEmail(),
                therapist.getPhone(),
                therapist.getSpecialization()
        );
    }


    private Therapist toEntity(TherapistDTO dto) {
        return new Therapist(
                dto.getId(),
                dto.getName(),
                dto.getEmail(),
                dto.getPhone(),
                dto.getSpecialization(),
                null
        );
    }



    @Override
    public boolean save(TherapistDTO dto) {
        if (!ValidationUtil.isValidName(dto.getName())) throw new DuplicateException("Invalid name.");
        if (!ValidationUtil.isValidEmail(dto.getEmail())) throw new DuplicateException("Invalid email.");
        if (!ValidationUtil.isValidPhone(dto.getPhone())) throw new DuplicateException("Invalid phone.");

        Session session = FactoryConfiguration.getInstance().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            therapistDAO.save(toEntity(dto));
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw new DuplicateException(e.getMessage());
        }
    }


    @Override
    public boolean update(TherapistDTO dto) {
        Session session = FactoryConfiguration.getInstance().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            therapistDAO.update(toEntity(dto));
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
            boolean result = therapistDAO.delete(id);
            tx.commit();
            if (!result) throw new NotFoundException("Therapist not found: " + id);
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
    public Optional<TherapistDTO> findById(String id) {
        Session session = FactoryConfiguration.getInstance().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Optional<Therapist> therapist = therapistDAO.findById(id);


            Optional<TherapistDTO> dto = therapist.map(this::toDTO);

            tx.commit();
            return dto;

        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw new NotFoundException(e.getMessage());
        }
    }



    @Override
    public List<TherapistDTO> getAll() {
        Session session = FactoryConfiguration.getInstance().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List<Therapist> therapists = therapistDAO.getAll();


            List<TherapistDTO> dtoList = therapists.stream()
                    .map(this::toDTO)
                    .collect(Collectors.toList());

            tx.commit();
            return dtoList;

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
            List<String> ids = therapistDAO.getAllIds();
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
            List<String> ids = therapistDAO.getAllIds();
            tx.commit();
            String lastId = ids.isEmpty() ? null : ids.stream().sorted().reduce((a, b) -> b).orElse(null);
            return IdGeneratorUtil.generateNextId("T", lastId);
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            return "T001";
        }
    }


}

























