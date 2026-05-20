package lk.ijse.therapycenter.entity;
import jakarta.persistence.*;
import lombok.*;
import java.sql.Date;
import java.util.List;

@Cacheable
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="registrations")

public class Registration {

    @Id
    private String id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="patient_id",nullable=false)
    private Patient patient;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="program_id",nullable=false)
    private TherapyProgram therapyProgram;

    @Column(name="registration_date",nullable=false)
    private Date registrationDate;

    @OneToMany(mappedBy="registration",cascade=CascadeType.ALL,fetch=FetchType.LAZY)
    private List<Payment> payments;

}
