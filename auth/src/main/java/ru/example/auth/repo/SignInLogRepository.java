package ru.example.auth.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.example.auth.entity.SignInLog;

import java.util.Date;
import java.util.List;

@Repository
@Transactional
public interface SignInLogRepository extends JpaRepository<SignInLog, Long> {

    @Query("select count(sa) from sign_in_logs sa where sa.createdAt > :start and sa.ip = :ip")
    List<SignInLog> getByIpAndCreatedAtAfterExpireTime(@Param("ip") String ip, @Param("start") Date start );

    @Query("delete from sign_in_logs s where s.ip = :ip")
    void deleteByIp(String ip);
}
