package ru.example.auth.entity;

import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.Instant;

@Table(name = "access_tokens", catalog = "auth_db", schema = "public")
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
public class AccessToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(name = "access_token_data", nullable = false, unique = true)
    private String accessTokenData;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;
}