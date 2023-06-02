package com.surveine.memberservice.repository;

import com.surveine.memberservice.security.RefreshToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
}
