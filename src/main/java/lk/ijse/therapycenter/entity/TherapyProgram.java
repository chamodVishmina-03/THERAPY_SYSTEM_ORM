package lk.ijse.therapycenter.entity;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;


@Cacheable
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="therapy_programs")

public class TherapyProgram {

    @Id
    private String id;

    @Column(nullable=false)
    private String name;

    @Column(nullable=false)
    private String duration;

    @Column(nullable=false)
    private double fee;

    private String description;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="therapist_id")
    private Therapist therapist;

    @OneToMany(mappedBy="therapyProgram",cascade=CascadeType.ALL,fetch=FetchType.LAZY)
    private List<Registration> registrations;
}
