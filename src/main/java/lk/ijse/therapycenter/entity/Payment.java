package lk.ijse.therapycenter.entity;
import jakarta.persistence.*;
import lombok.*;
import java.sql.Date;

@Cacheable
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="payments")



public class Payment {
    @Id
    private String id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="registration_id",nullable=false)
    private Registration registration;

    @Column(nullable=false)
    private double amount;

    @Column(name="payment_date",nullable=false)
    private Date paymentDate;

    @Enumerated(EnumType.STRING)
    private Status status;


    public enum Status {
        PENDING,
        COMPLETED,
        FAILED
    }

}

