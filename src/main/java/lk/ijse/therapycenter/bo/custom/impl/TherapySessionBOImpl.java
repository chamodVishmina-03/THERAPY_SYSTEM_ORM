package lk.ijse.therapycenter.bo.custom.impl;

import lk.ijse.therapycenter.bo.custom.TherapySessionBO;
import lk.ijse.therapycenter.bo.exception.NotFoundException;
import lk.ijse.therapycenter.bo.exception.SchedulingConflictException;
import lk.ijse.therapycenter.config.FactoryConfiguration;
import lk.ijse.therapycenter.dao.DAOFactory;
import lk.ijse.therapycenter.dao.DAOTypes;
import lk.ijse.therapycenter.dao.custom.PatientDAO;
import lk.ijse.therapycenter.dao.custom.TherapistDAO;
import lk.ijse.therapycenter.dao.custom.TherapyProgramDAO;
import lk.ijse.therapycenter.dao.custom.TherapySessionDAO;
import lk.ijse.therapycenter.dto.TherapySessionDTO;
import lk.ijse.therapycenter.entity.Patient;
import lk.ijse.therapycenter.entity.Therapist;
import lk.ijse.therapycenter.entity.TherapyProgram;
import lk.ijse.therapycenter.entity.TherapySession;
import lk.ijse.therapycenter.util.IdGeneratorUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.stream.Collectors;



public class TherapySessionBOImpl implements TherapySessionBO {


    private final TherapySessionDAO sessionDAO  = DAOFactory.getInstance().getDAO(DAOTypes.THERAPY_SESSION);
    private final PatientDAO        patientDAO  = DAOFactory.getInstance().getDAO(DAOTypes.PATIENT);
    private final TherapistDAO      therapistDAO = DAOFactory.getInstance().getDAO(DAOTypes.THERAPIST);
    private final TherapyProgramDAO programDAO  = DAOFactory.getInstance().getDAO(DAOTypes.THERAPY_PROGRAM);


    private TherapySessionDTO toDTO(TherapySession session) {
        return new TherapySessionDTO(
                session.getId(),
                session.getPatient().getId(),
                session.getPatient().getName(),
                session.getTherapist().getId(),
                session.getTherapist().getName(),
                session.getTherapyProgram().getId(),
                session.getTherapyProgram().getName(),
                session.getStatus().name(),
                session.getSessionDate(),
                session.getSessionTime()
        );
    }


    @Override
    public boolean save(TherapySessionDTO dto) {
        Session session = FactoryConfiguration.getInstance().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            // Scheduling conflict check — same therapist, same date, same time
            if (sessionDAO.hasConflict(dto.getTherapistId(), dto.getSessionDate(), dto.getSessionTime())) {
                tx.rollback();
                throw new SchedulingConflictException("Therapist already has a session at that date and time.");
            }

            Patient patient = patientDAO.findById(dto.getPatientId())
                    .orElseThrow(() -> new NotFoundException("Patient not found."));

            Therapist therapist = therapistDAO.findById(dto.getTherapistId())
                    .orElseThrow(() -> new NotFoundException("Therapist not found."));

            TherapyProgram program = programDAO.findById(dto.getProgramId())
                    .orElseThrow(() -> new NotFoundException("Therapy program not found."));

            String status = dto.getStatus() != null ? dto.getStatus() : "SCHEDULED";
            TherapySession newSession = new TherapySession(
                    dto.getId(), patient, therapist, program,
                    dto.getSessionDate(), dto.getSessionTime(),
                    TherapySession.Status.valueOf(status)
            );
            sessionDAO.save(newSession);
            tx.commit();
            return true;

        } catch (SchedulingConflictException | NotFoundException e) {
            throw e;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw new NotFoundException(e.getMessage());
        }
    }



    @Override
    public boolean update(TherapySessionDTO dto) {
        Session session = FactoryConfiguration.getInstance().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Patient patient = patientDAO.findById(dto.getPatientId())
                    .orElseThrow(() -> new NotFoundException("Patient not found."));

            Therapist therapist = therapistDAO.findById(dto.getTherapistId())
                    .orElseThrow(() -> new NotFoundException("Therapist not found."));

            TherapyProgram program = programDAO.findById(dto.getProgramId())
                    .orElseThrow(() -> new NotFoundException("Therapy program not found."));

            TherapySession updatedSession = new TherapySession(
                    dto.getId(), patient, therapist, program,
                    dto.getSessionDate(), dto.getSessionTime(),
                    TherapySession.Status.valueOf(dto.getStatus())
            );
            sessionDAO.update(updatedSession);
            tx.commit();
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
    public boolean delete(String id) {
        Session session = FactoryConfiguration.getInstance().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            boolean result = sessionDAO.delete(id);
            tx.commit();
            if (!result) throw new NotFoundException("Session not found.");
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
    public List<TherapySessionDTO> getAll() {

        Session session = FactoryConfiguration.getInstance().getCurrentSession();

        Transaction t = null;
        try {
            t = session.beginTransaction();
            List<TherapySession> sessions = sessionDAO.getAll();


            List<TherapySessionDTO> dtoList = sessions.stream()
                    .map(this::toDTO)
                    .collect(Collectors.toList());

            t.commit();
            return dtoList;

        } catch (Exception e) {
            if (t != null && t.isActive()) t.rollback();
            throw new NotFoundException(e.getMessage());
        }
    }


    @Override
    public List<TherapySessionDTO> findByPatientId(String patientId) {
        Session session = FactoryConfiguration.getInstance().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List<TherapySession> sessions = sessionDAO.findByPatientId(patientId);

            List<TherapySessionDTO> dtoList = sessions.stream()
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
    public String generateNextId() {
        Session session = FactoryConfiguration.getInstance().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String lastId = sessionDAO.getLastId();
            tx.commit();
            return IdGeneratorUtil.generateNextId("SES", lastId);
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            return "SES001";
        }
    }



}










