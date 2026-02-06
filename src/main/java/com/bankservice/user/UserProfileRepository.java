package com.bankservice.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository
        extends JpaRepository<UserProfile, String> {
    boolean existsByResidentNumberEncrypted(String residentNumberEncrypted);
}

