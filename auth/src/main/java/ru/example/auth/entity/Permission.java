package ru.example.auth.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.internal.util.stereotypes.Immutable;
import ru.example.auth.enums.PermissionEnum;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Table(name = "permissions", catalog = "auth_db", schema = "public")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode(exclude = {"id", "roles"})
@ToString(exclude = {"id", "roles"})
@DynamicUpdate
@DynamicInsert
public class Permission implements Serializable {
    private static final long serialVersionUID = -681869902302420104L;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Immutable
    @Column(name = "title", nullable = false)
    private String title = PermissionEnum.WRITE.name();

    @JsonIgnore
    @ManyToMany(
            fetch = FetchType.LAZY,
            mappedBy = "permissions",
            cascade = CascadeType.PERSIST
    )
    @Fetch(value = FetchMode.JOIN)
    private Set<Role> roles = new HashSet<>();
}
