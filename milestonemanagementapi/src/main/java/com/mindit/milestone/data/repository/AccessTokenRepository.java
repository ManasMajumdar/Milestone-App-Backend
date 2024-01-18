package com.mindit.milestone.data.repository;

import com.mindit.milestone.data.entity.AccessToken;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccessTokenRepository extends JpaRepository<AccessToken, Integer> {

  List<AccessToken> findByUserIdAndIsActive(String userId, Boolean isActive);

  AccessToken findByToken(String token);
}
