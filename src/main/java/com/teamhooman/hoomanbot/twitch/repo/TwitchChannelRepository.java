package com.teamhooman.hoomanbot.twitch.repo;

import com.teamhooman.hoomanbot.twitch.domain.TwitchChannel;
import org.springframework.cache.annotation.Cacheable;

import java.util.Optional;

public interface TwitchChannelRepository extends TwitchRepository<TwitchChannel, Long> {

    @Cacheable(cacheNames = "twitch")
    Optional<TwitchChannel> findByNameIgnoreCase(String name);
}
