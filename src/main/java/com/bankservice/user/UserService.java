package com.bankservice.user;

import com.bankservice.auth.dto.SignupRequest;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final BCryptPasswordEncoder encoder;

    public UserService(UserRepository userRepository,
                       UserProfileRepository userProfileRepository,
                       BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
        this.encoder = encoder;
    }

    public void signup(SignupRequest request) {

        // 1️⃣ 이메일 중복 체크
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("이미 존재하는 이메일");
        }

        // 2️⃣ 사용자 생성
        User user = new User(
                request.getEmail(),
                encoder.encode(request.getPassword())
        );
        userRepository.save(user);

        // 3️⃣ 사용자 프로필 생성
        UserProfile profile = new UserProfile(
                user,
                request.getName(),
                encrypt(request.getResidentNumber()),
                request.getPhone(),
                request.getAddress()
        );
        userProfileRepository.save(profile);
        System.out.println("🔥 signup() 진입");
    }

    // 🔹 지금은 더미 (나중에 AES로 교체)
    private String encrypt(String value) {
        return value;
    }
}
