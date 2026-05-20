package lk.ijse.therapycenter.config;

import lk.ijse.therapycenter.entity.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.Properties;

public class FactoryConfiguration {


    private static FactoryConfiguration instance;
    private final SessionFactory sessionFactory;


    private FactoryConfiguration() {
        Configuration configuration = new Configuration();

        //property file configuration
        configuration.addProperties(loadHibernateProperties());

        //   entity classes
        configuration.addAnnotatedClass(User.class);
        configuration.addAnnotatedClass(Therapist.class);
        configuration.addAnnotatedClass(TherapyProgram.class);
        configuration.addAnnotatedClass(Patient.class);
        configuration.addAnnotatedClass(Registration.class);
        configuration.addAnnotatedClass(TherapySession.class);
        configuration.addAnnotatedClass(Payment.class);

        sessionFactory = configuration.buildSessionFactory();


    }


    private Properties loadHibernateProperties() {
        Properties props = new Properties();

        try {

            props.load(
                getClass().getClassLoader().getResourceAsStream("hibernate.properties")
            );

            System.out.println(" hibernate.properties loaded successfully!");



        } catch (Exception e) {
            System.err.println(" Failed to load hibernate.properties: " + e.getMessage());
        }
        return props;
    }


    public static FactoryConfiguration getInstance() {
        if (instance == null) {
            instance = new FactoryConfiguration();
        }
        return instance;
    }



    public Session getCurrentSession() {

        return sessionFactory.getCurrentSession();
    }


}






