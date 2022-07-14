package ru.example.auth.service.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.example.auth.entity.SignInLog;
import ru.example.auth.exception.custom_exception.auth.TooManySignInAttemptsException;
import ru.example.auth.repo.SignInLogRepository;

import java.util.Calendar;

@Service
public class SignInLogService {
    private final SignInLogRepository signInLogRepository;
    int maxTries;
    int expireTimeMs;

    @Autowired
    public SignInLogService(
            SignInLogRepository signInLogRepository,
            @Value("${sign-in.tries}") int maxTries,
            @Value("${sign-in.expire}") int expireTimeMs
    ) {
        this.signInLogRepository = signInLogRepository;
        this.maxTries = maxTries;
        this.expireTimeMs = expireTimeMs;
    }

    public void checkTriesSignIn(String ip) {
        Calendar inst = Calendar.getInstance();
        inst.set(Calendar.MILLISECOND,
                (inst.get(Calendar.MILLISECOND) - expireTimeMs));

        if (signInLogRepository.getByIpAndCreatedAtAfterExpireTime(ip, inst.getTime()) >= maxTries) {
            throw new TooManySignInAttemptsException();
        }

        signInLogRepository.save(new SignInLog(ip));
    }

    public void deleteByIp(String ip) {
        signInLogRepository.deleteByIp(ip);
    }
}
