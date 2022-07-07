package ru.example.auth.entity;


import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.validator.internal.util.stereotypes.Immutable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Table(name = "sign_in_logs", catalog = "auth_db", schema = "public")
@Entity
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = {"id"})
@ToString(exclude = {"id"})
@DynamicUpdate
@DynamicInsert
public class SignInLog implements Serializable {
    private static final Long serialVersionUID = 223L;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Immutable
    @Column(name = "ip", nullable = false)
    private String ip;

    @Temporal(TemporalType.TIMESTAMP)
    @org.hibernate.annotations.Generated(GenerationTime.ALWAYS)
    @Column(name = "created_at", updatable = false, insertable = false, columnDefinition = "TIMESTAMP")
    private Date createdAt;

    public SignInLog(String ip) {
        this.ip = ip;
    }
}