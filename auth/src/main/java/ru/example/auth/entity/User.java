package ru.example.auth.entity;


import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.*;
import org.springframework.transaction.annotation.Transactional;
import ru.example.auth.converter.UserPasswordAttributeConverter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.*;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Table(name = "users", catalog = "auth_db", schema = "public")
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
public class User implements Serializable {
    private static final Long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password")
    @Convert(converter = UserPasswordAttributeConverter.class)
    private String password;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST)
    @JoinTable(name = "user_role",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")})
    @Fetch(value = FetchMode.JOIN)
    private Set<Role> roles = new HashSet<>();

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "id", referencedColumnName = "user_id")
    private AccessToken accessToken;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "id", referencedColumnName = "user_id")
    private RefreshToken refreshToken;


    @Temporal(TemporalType.TIMESTAMP)
    @org.hibernate.annotations.Generated(GenerationTime.ALWAYS)
    @Column(name = "created_at", updatable = false, insertable = false, columnDefinition = "TIMESTAMP")
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    @Column(name = "updated_at", insertable = false, columnDefinition = "TIMESTAMP")
    private Date updatedAt;

    /**
     * Смешанный (но уникальный) список ROLES, PERMISSIONS
     * для получения списка SimpleGrantedAuthority
     * сможем делать ограничения по ролям, либо по authority
     */
    @Transactional
    public Set<String> getAuthorities() {
        Set<String> authorities = new HashSet<>();
        for (Role role : this.getRoles()) {
            authorities.add(role.getTitle());
            for (Permission permission : role.getPermissions()) {
                authorities.add(permission.getTitle());
            }
        }

        return authorities;
    }
}
