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
@Table(name="therapists")

public class  Therapist {

    @Id
    private String id;

    @Column(nullable=false)
    private String name;

    @Column(unique=true,nullable=false)
    private String email;

    @Column(nullable=false)
    private String phone;

    private String specialization;

    @OneToMany(mappedBy="therapist",cascade=CascadeType.ALL,fetch=FetchType.LAZY)
    private List<TherapySession> sessions;



}
