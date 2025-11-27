package com.lamarfishing.core.user.dummy;

import com.lamarfishing.core.user.domain.Grade;
import com.lamarfishing.core.user.domain.Provider;
import com.lamarfishing.core.user.domain.User;
import com.lamarfishing.core.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserDummy {

    private final UserRepository userRepository;

    public void init(){
        if (userRepository.count() > 0) {
            return;
        }

        userRepository.save(User.create("김승규", "승규천사", Grade.BASIC, "01056604120", "1234", Provider.KAKAO));
        userRepository.save(User.create("김영희", "영희악마", Grade.ADMIN, "01012345679", "1234", Provider.KAKAO));
        userRepository.save(User.create("김지오", "지오천사", Grade.VIP, "01062647243", "1234", Provider.KAKAO));
        userRepository.save(User.create("원종윤", "종윤천사", Grade.BASIC, "01063461851", "1234", Provider.KAKAO));
        userRepository.save(User.create("김철수", "철수악마", Grade.ADMIN, "01012345678", "1234", Provider.KAKAO));
        userRepository.save(User.create("장창엽", "창엽천사", Grade.VIP, "01045105619", "1234", Provider.KAKAO));
        userRepository.save(User.create("김준수", "준수악마", Grade.GUEST, "01044966580", "1234", Provider.KAKAO));
        userRepository.save(User.create("김경우", "경우악마", Grade.BASIC, "01039975917", "1234", Provider.KAKAO));
        userRepository.save(User.create("서혜원", "혜원천사", Grade.BASIC, "01062330155", "1234", Provider.KAKAO));
    }

}
