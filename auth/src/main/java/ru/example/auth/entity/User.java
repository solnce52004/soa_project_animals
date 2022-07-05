package ru.example.auth.entity;


import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.*;
import org.springframework.transaction.annotation.Transactional;
import ru.example.auth.converter.UserPasswordAttributeConverter;
import ru.example.auth.enums.UserStatusEnum;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Accessors(chain = true)
@EqualsAndHashCode(exclude = {"id", "roles"})
@ToString(exclude = {"id", "roles"})
@DynamicUpdate
@DynamicInsert
public class User implements Serializable {
    private static final Long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password")
    @Convert(converter = UserPasswordAttributeConverter.class)
    private String password;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST //не будем удалять роль при удалении юзера
    )
    @JoinTable(name = "user_role",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")})
    @Fetch(value = FetchMode.JOIN)
    private Set<Role> roles = new HashSet<>();

//    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private String status = UserStatusEnum.NOT_CONFIRMED.name();

    @Column(name = "token")
    private String token;

//    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "expiry_date", nullable = false, columnDefinition = "TIMESTAMP")
    private Instant expiryDate;

    @Temporal(TemporalType.TIMESTAMP)
    @org.hibernate.annotations.Generated(GenerationTime.ALWAYS)
    @Column(name="created_at", updatable = false, insertable = false, columnDefinition = "TIMESTAMP")
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    @Column(name="updated_at", insertable = false, columnDefinition = "TIMESTAMP")
    private Date updatedAt;

    /**
     * Смешанный (но уникальный) список ROLES, PERMISSIONS
     * для получения списка SimpleGrantedAuthority
     * сможем делать ограничения по ролям, либо по authority
     */
    @Transactional
    public Set<String> getAuthorities() {
        Set<String> authorities = new HashSet<>();
        for (Role role : getRoles()) {
            authorities.add(role.getTitle());
            for (Permission permission : role.getPermissions()) {
                authorities.add(permission.getTitle());
            }
        }

        return authorities;
    }

    public User(
            String username,
            String password
    ) {
        this.username = username;
        this.password = password;
    }
}
