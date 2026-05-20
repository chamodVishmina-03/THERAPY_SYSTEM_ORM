package lk.ijse.therapycenter.bo;

import lk.ijse.therapycenter.bo.custom.impl.*;

public class BOFactory {

    private static BOFactory instance;

    private BOFactory() {}

    public static BOFactory getInstance() {
        if (instance == null) {
            instance = new BOFactory();
        }
        return instance;
    }



    public <T extends SuperBO> T getBO(BOTypes type) {

        switch (type) {
            case USER:
                return (T) new UserBOImpl();
            case THERAPIST:
                return (T) new TherapistBOImpl();
            case THERAPY_PROGRAM:
                return (T) new TherapyProgramBOImpl();
            case PATIENT:
                return (T) new PatientBOImpl();
            case REGISTRATION:
                return (T) new RegistrationBOImpl();
            case THERAPY_SESSION:
                return (T) new TherapySessionBOImpl();
            case PAYMENT:
                return (T) new PaymentBOImpl();
            default:
                return null;
        }

    }
}






