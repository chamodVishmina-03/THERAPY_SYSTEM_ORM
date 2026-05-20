package lk.ijse.therapycenter.dao;
import lk.ijse.therapycenter.dao.custom.impl.*;


public class DAOFactory {

    private static DAOFactory instance;
    private DAOFactory() {}



    public static DAOFactory getInstance() {
        return instance==null ? instance=new DAOFactory() : instance;
    }




    public <T extends SuperDAO> T getDAO(DAOTypes e) {

        switch (e) {
            case USER:
                return (T) new UserDAOImpl();

            case THERAPIST:
                return (T) new TherapistDAOImpl();

            case THERAPY_PROGRAM:
                return (T) new TherapyProgramDAOImpl();

            case PATIENT:
                return (T) new PatientDAOImpl();

            case REGISTRATION:
                return (T) new RegistrationDAOImpl();

            case THERAPY_SESSION:
                return (T) new TherapySessionDAOImpl();

            case PAYMENT:
                return (T) new PaymentDAOImpl();

            default:
                return null;
        }

    }





}
















