package ru.example.auth.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.example.auth.entity.SignInLog;

import java.util.Date;

@Repository
@Transactional
public interface SignInLogRepository extends JpaRepository<SignInLog, Long> {

    @Query("select count(sa) from SignInLog sa where sa.createdAt > :start and sa.ip = :ip")
    int getByIpAndCreatedAtAfterExpireTime(@Param("ip") String ip, @Param("start") Date start );

    void deleteByIp(String ip);
}
