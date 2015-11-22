package com.ryonday.automation.twitch.repo;

import com.ryonday.automation.twitch.domain.TwitchChannel;
import org.springframework.cache.annotation.Cacheable;

import java.util.Optional;

public interface TwitchChannelRepository extends TwitchRepository<TwitchChannel, Long> {

    @Cacheable(cacheNames = "twitch")
    Optional<TwitchChannel> findByNameIgnoreCase(String name);
}
