package com.bankservice.token;

import com.bankservice.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "리프레시_토큰")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "토큰_ID")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "사용자_ID", unique = true)
    private User user;

    @Column(name = "토큰", nullable = false)
    private String token;

    @Column(name = "만료일시")
    private LocalDateTime expiredAt;
}
