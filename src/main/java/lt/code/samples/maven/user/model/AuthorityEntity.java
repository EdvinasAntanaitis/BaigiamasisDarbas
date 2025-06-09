package lt.code.samples.maven.user.model;

import org.springframework.security.core.GrantedAuthority;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "authority")
@Table(name = "authority")
public class AuthorityEntity implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String name;
    private String description;

    @Override
    public String getAuthority() {
        return name;
    }
}
