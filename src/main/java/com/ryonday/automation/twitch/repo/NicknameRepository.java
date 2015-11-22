package com.ryonday.automation.twitch.repo;

import com.ryonday.automation.twitch.domain.Nickname;
import org.springframework.cache.annotation.Cacheable;

import java.util.Optional;

public interface NicknameRepository extends TwitchRepository<Nickname, Long> {

    @Cacheable
    Optional<Nickname> findByNicknameIgnoreCase(String nickname );
}
