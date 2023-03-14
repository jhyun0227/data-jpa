package study.datajpa.member.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;

public interface UsernameOnly {

    @Value("#{target.username + ' ' + target.age}")
    String getUsername();
}
