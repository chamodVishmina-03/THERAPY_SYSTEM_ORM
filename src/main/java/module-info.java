module lk.ijse.therapycenter {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires static lombok;
    requires org.hibernate.orm.core;
    requires jakarta.persistence;
    requires java.naming;
    requires jbcrypt;

    opens lk.ijse.therapycenter.entity to org.hibernate.orm.core;
    opens lk.ijse.therapycenter.config to jakarta.persistence;
    opens lk.ijse.therapycenter.controller to javafx.fxml;
    opens lk.ijse.therapycenter.dto.tm to javafx.base;

    exports lk.ijse.therapycenter;
}
