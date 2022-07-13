package ru.example.auth.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

@Table(name = "access_tokens")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Accessors(chain = true)
@DynamicUpdate
@DynamicInsert
public class AccessToken implements Serializable {
    private static final long serialVersionUID = -7317292848716365029L;

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(name = "token", nullable = false, unique = true)
    private String token;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccessToken that = (AccessToken) o;
        return getToken().equals(that.getToken()) &&
                getExpiresAt().equals(that.getExpiresAt());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getToken(), getExpiresAt());
    }

    @Override
    public String toString() {
        return "AccessToken{" +
                "token='" + token + '\'' +
                ", expiresAt=" + expiresAt +
                '}';
    }
}