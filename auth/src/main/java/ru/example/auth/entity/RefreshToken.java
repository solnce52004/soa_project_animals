package ru.example.auth.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

@Table(name = "refresh_tokens")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Accessors(chain = true)
@DynamicUpdate
@DynamicInsert
public class RefreshToken implements Serializable {
    private static final long serialVersionUID = 7256066343928666315L;

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
        RefreshToken that = (RefreshToken) o;
        return getToken().equals(that.getToken()) &&
                getExpiresAt().equals(that.getExpiresAt());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getToken(), getExpiresAt());
    }

    @Override
    public String toString() {
        return "RefreshToken{" +
                "token='" + token + '\'' +
                ", expiresAt=" + expiresAt +
                '}';
    }
}
