package com.ryonday.automation.twitch.repo;

import com.ryonday.automation.twitch.domain.Nickname;

import java.util.Optional;

public interface NicknameRepository extends TwitchRepository<Nickname, Long> {
    Optional<Nickname> findByNicknameIgnoreCase(String nickname );
}
