package lk.ijse.therapycenter.bo.custom.impl;

import lk.ijse.therapycenter.bo.custom.PaymentBO;
import lk.ijse.therapycenter.bo.exception.NotFoundException;
import lk.ijse.therapycenter.bo.exception.PaymentException;
import lk.ijse.therapycenter.config.FactoryConfiguration;
import lk.ijse.therapycenter.dao.DAOFactory;
import lk.ijse.therapycenter.dao.DAOTypes;
import lk.ijse.therapycenter.dao.custom.PaymentDAO;
import lk.ijse.therapycenter.dao.custom.RegistrationDAO;
import lk.ijse.therapycenter.dto.PaymentDTO;
import lk.ijse.therapycenter.entity.Payment;
import lk.ijse.therapycenter.entity.Registration;
import lk.ijse.therapycenter.util.IdGeneratorUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.stream.Collectors;



public class PaymentBOImpl implements PaymentBO {


    private final PaymentDAO      paymentDAO      = DAOFactory.getInstance().getDAO(DAOTypes.PAYMENT);
    private final RegistrationDAO registrationDAO = DAOFactory.getInstance().getDAO(DAOTypes.REGISTRATION);

    private PaymentDTO toDTO(Payment payment) {
        return new PaymentDTO(
                payment.getId(),
                payment.getRegistration().getId(),
                payment.getRegistration().getPatient().getName(),
                payment.getRegistration().getTherapyProgram().getName(),
                payment.getStatus().name(),
                payment.getAmount(),
                payment.getPaymentDate()
        );
    }



    @Override
    public boolean save(PaymentDTO dto) {
        if (dto.getAmount() <= 0) {
            throw new PaymentException("Invalid payment amount.");
        }

        Session session = FactoryConfiguration.getInstance().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Registration registration = registrationDAO.findById(dto.getRegistrationId())
                    .orElseThrow(() -> new NotFoundException("Registration not found."));

            String status = dto.getStatus() != null ? dto.getStatus() : "COMPLETED";
            Payment payment = new Payment(
                    dto.getId(), registration, dto.getAmount(),
                    dto.getPaymentDate(), Payment.Status.valueOf(status)
            );
            paymentDAO.save(payment);
            tx.commit();
            return true;

        } catch (NotFoundException | PaymentException e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw e;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw new PaymentException(e.getMessage());
        }
    }


    @Override
    public boolean update(PaymentDTO dto) {
        Session session = FactoryConfiguration.getInstance().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Registration registration = registrationDAO.findById(dto.getRegistrationId())
                    .orElseThrow(() -> new NotFoundException("Registration not found."));

            Payment payment = new Payment(
                    dto.getId(), registration, dto.getAmount(),
                    dto.getPaymentDate(), Payment.Status.valueOf(dto.getStatus())
            );
            paymentDAO.update(payment);
            tx.commit();
            return true;

        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw new PaymentException(e.getMessage());
        }
    }


    @Override
    public List<PaymentDTO> getAll() {
        Session session = FactoryConfiguration.getInstance().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List<Payment> payments = paymentDAO.getAll();

            List<PaymentDTO> dtoList = payments.stream()
                    .map(this::toDTO)
                    .collect(Collectors.toList());

            tx.commit();
            return dtoList;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw new PaymentException(e.getMessage());
        }
    }

    @Override
    public List<PaymentDTO> findByRegistrationId(String registrationId) {
        Session session = FactoryConfiguration.getInstance().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List<Payment> payments = paymentDAO.findByRegistrationId(registrationId);

            List<PaymentDTO> dtoList = payments.stream()
                    .map(this::toDTO)
                    .collect(Collectors.toList());

            tx.commit();
            return dtoList;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw new PaymentException(e.getMessage());
        }
    }



    @Override
    public String generateNextId() {
        Session session = FactoryConfiguration.getInstance().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String lastId = paymentDAO.getLastId();
            tx.commit();
            return IdGeneratorUtil.generateNextId("PAY", lastId);
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            return "PAY001";
        }
    }



}



