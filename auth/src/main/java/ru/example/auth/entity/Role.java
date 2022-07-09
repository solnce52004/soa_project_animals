package ru.example.auth.entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.internal.util.stereotypes.Immutable;
import ru.example.auth.enums.RoleEnum;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Table(name = "roles", catalog = "auth_db", schema = "public")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = {"id", "permissions"})
@ToString(exclude = {"id", "permissions"})
@DynamicUpdate
@DynamicInsert
public class Role implements Serializable {
    private static final long serialVersionUID = 3279343351032951253L;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Immutable
    @Column(name = "title", nullable = false)
    private String title = RoleEnum.ROLE_USER.name();

    @ManyToMany(
            fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST
    )
    @JoinTable(
            name = "role_permission",
            joinColumns = {@JoinColumn(name = "role_id")},
            inverseJoinColumns = {@JoinColumn(name = "permission_id")}
    )
    @Fetch(value = FetchMode.JOIN)
    private Set<Permission> permissions = new HashSet<>();
}
