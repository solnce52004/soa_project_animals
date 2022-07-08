package ru.example.auth.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

@Table(name = "refresh_tokens", catalog = "auth_db", schema = "public")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode(exclude = {"id", "user"})
@ToString(exclude = {"id", "user"})
@DynamicUpdate
@DynamicInsert
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RefreshToken implements Serializable {
    @Transient
    private static final long serialVersionUID = 45667L;

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(name = "refresh_token_data", nullable = false, unique = true)
    private String refreshTokenData;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;
}
