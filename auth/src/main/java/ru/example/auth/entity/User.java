package ru.example.auth.entity;


import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.*;
import org.springframework.transaction.annotation.Transactional;
import ru.example.auth.converter.UserPasswordAttributeConverter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Table(name = "users")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Accessors(chain = true)
@DynamicUpdate
@DynamicInsert
public class User implements Serializable {
    private static final long serialVersionUID = 6558029016755221069L;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return getUsername().equals(user.getUsername()) &&
                getPassword().equals(user.getPassword());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername(), getPassword());
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
