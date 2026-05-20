package lk.ijse.therapycenter.entity;
import jakarta.persistence.*;
import lombok.*;
import java.sql.Date;
import java.sql.Time;


@Cacheable
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity @Table(name="therapy_sessions")

public class TherapySession {

    @Id
    private String id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="patient_id",nullable=false)
    private Patient patient;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="therapist_id",nullable=false)
    private Therapist therapist;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="program_id",nullable=false)
    private TherapyProgram therapyProgram;

    @Column(name="session_date",nullable=false)
    private Date sessionDate;

    @Column(name="session_time",nullable=false)
    private Time sessionTime;

    @Enumerated(EnumType.STRING)
    private Status status;

    public enum Status {

        SCHEDULED,
        COMPLETED,
        CANCELLED,
        RESCHEDULED
    }
}
