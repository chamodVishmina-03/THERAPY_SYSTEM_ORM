package lk.ijse.therapycenter;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lk.ijse.therapycenter.config.FactoryConfiguration;
import lk.ijse.therapycenter.entity.User;
import lk.ijse.therapycenter.util.PasswordUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;


public class AppInitializer extends Application {
    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        initDefaultUsers();


        primaryStage.setScene(new Scene(
                new FXMLLoader(getClass().getResource("/view/LoginPage.fxml")).load()
        ));


        primaryStage.setTitle("Serenity Mental Health Therapy Center");
        primaryStage.setResizable(false);
        primaryStage.show();

    }




    private void initDefaultUsers() {
        Session session = null;
        Transaction transaction = null;


        try {
            session = FactoryConfiguration.getInstance().getCurrentSession();
            transaction = session.beginTransaction();


            //    count existing users
            Long count = session.createQuery("SELECT COUNT(u) FROM User u", Long.class).uniqueResult();

            if (count == 0) {

                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(PasswordUtil.hashPassword("Admin@1234"));
                admin.setRole(User.Role.ADMIN);
                session.persist(admin);


                User recep = new User();
                recep.setUsername("receptionist");
                recep.setPassword(PasswordUtil.hashPassword("Recep@1234"));
                recep.setRole(User.Role.RECEPTIONIST);
                session.persist(recep);


                transaction.commit();
                System.out.println(" Default users created: admin / Admin@1234");


            } else {
                transaction.commit();
                System.out.println(" Users already exist: " + count);
            }


        } catch (Exception e) {

            if (transaction != null && transaction.isActive()) transaction.rollback();
            System.err.println(" User init error: " + e.getMessage());


        }


    }







}










































