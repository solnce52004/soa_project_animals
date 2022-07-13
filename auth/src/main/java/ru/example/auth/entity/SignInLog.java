package ru.example.auth.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Table(name = "sign_in_logs")
@Entity
@NoArgsConstructor
@Getter @Setter
@DynamicUpdate
@DynamicInsert
public class SignInLog implements Serializable {
    private static final long serialVersionUID = -5690018859592138949L;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ip", nullable = false)
    private String ip;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", columnDefinition = "TIMESTAMP")
    @ColumnDefault("CURRENT_TIMESTAMP")
    private Date createdAt;

    public SignInLog(String ip) {
        this.ip = ip;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SignInLog signInLog = (SignInLog) o;
        return getIp().equals(signInLog.getIp()) &&
                getCreatedAt().equals(signInLog.getCreatedAt());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIp(), getCreatedAt());
    }

    @Override
    public String toString() {
        return "SignInLog{" +
                "ip='" + ip + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}