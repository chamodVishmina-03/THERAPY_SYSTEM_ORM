package lk.ijse.therapycenter.bo.custom.impl;

import lk.ijse.therapycenter.bo.custom.TherapyProgramBO;
import lk.ijse.therapycenter.bo.exception.DuplicateException;
import lk.ijse.therapycenter.bo.exception.NotFoundException;
import lk.ijse.therapycenter.config.FactoryConfiguration;
import lk.ijse.therapycenter.dao.DAOFactory;
import lk.ijse.therapycenter.dao.DAOTypes;
import lk.ijse.therapycenter.dao.custom.TherapistDAO;
import lk.ijse.therapycenter.dao.custom.TherapyProgramDAO;
import lk.ijse.therapycenter.dto.TherapyProgramDTO;
import lk.ijse.therapycenter.entity.Therapist;
import lk.ijse.therapycenter.entity.TherapyProgram;
import lk.ijse.therapycenter.util.IdGeneratorUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;




public class TherapyProgramBOImpl implements TherapyProgramBO {

    private final TherapyProgramDAO programDAO   = DAOFactory.getInstance().getDAO(DAOTypes.THERAPY_PROGRAM);
    private final TherapistDAO      therapistDAO = DAOFactory.getInstance().getDAO(DAOTypes.THERAPIST);



    private TherapyProgramDTO toDTO(TherapyProgram program) {
        String therapistId   = program.getTherapist() != null ? program.getTherapist().getId()   : null;
        String therapistName = program.getTherapist() != null ? program.getTherapist().getName() : "N/A";
        return new TherapyProgramDTO(
                program.getId(),
                program.getName(),
                program.getDuration(),
                program.getDescription(),
                therapistId,
                therapistName,
                program.getFee()
        );
    }



    @Override
    public boolean save(TherapyProgramDTO dto) {
        Session session = FactoryConfiguration.getInstance().getCurrentSession();

        Transaction tx = null;
        try {

            tx = session.beginTransaction();
            Therapist therapist = dto.getTherapistId() != null
                    ? therapistDAO.findById(dto.getTherapistId()).orElse(null)
                    : null;
            TherapyProgram program = new TherapyProgram(
                    dto.getId(),
                    dto.getName(),
                    dto.getDuration(),
                    dto.getFee(),
                    dto.getDescription(), therapist, null
            );
            programDAO.save(program);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw new DuplicateException(e.getMessage());
        }
    }



    @Override
    public boolean update(TherapyProgramDTO dto) {
        Session session = FactoryConfiguration.getInstance().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Therapist therapist = dto.getTherapistId() != null
                    ? therapistDAO.findById(dto.getTherapistId()).orElse(null)
                    : null;
            TherapyProgram program = new TherapyProgram(
                    dto.getId(), dto.getName(), dto.getDuration(),
                    dto.getFee(), dto.getDescription(), therapist, null
            );
            programDAO.update(program);
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
            boolean result = programDAO.delete(id);
            tx.commit();
            if (!result) throw new NotFoundException("Program not found.");
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
    public Optional<TherapyProgramDTO> findById(String id) {
        Session session = FactoryConfiguration.getInstance().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Optional<TherapyProgram> program = programDAO.findById(id);


            Optional<TherapyProgramDTO> dto = program.map(this::toDTO);

            tx.commit();
            return dto;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw new NotFoundException(e.getMessage());
        }
    }

    @Override
    public List<TherapyProgramDTO> getAll() {
        Session session = FactoryConfiguration.getInstance().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List<TherapyProgram> programs = programDAO.getAll();


            List<TherapyProgramDTO> dtoList = programs.stream()
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
            List<String> ids = programDAO.getAllIds();
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
            List<String> ids = programDAO.getAllIds();
            tx.commit();
            String lastId = ids.isEmpty() ? null : ids.stream().sorted().reduce((a, b) -> b).orElse(null);
            return IdGeneratorUtil.generateNextId("P", lastId);
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            return "P001";
        }
    }




}








































