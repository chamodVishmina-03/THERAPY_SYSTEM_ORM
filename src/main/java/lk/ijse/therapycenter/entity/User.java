package lk.ijse.therapycenter.entity;
import jakarta.persistence.*;
import lombok.*;

@Cacheable
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="users")

public class User{
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @Column(unique=true,nullable=false)
    private String username;

    @Column(nullable=false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private Role role;

    public enum Role {

        ADMIN,
        RECEPTIONIST

    }
}
