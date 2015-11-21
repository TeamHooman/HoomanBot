package com.ryonday.automation.twitch.repo;

import com.ryonday.automation.twitch.domain.TwitchChannel;

import java.util.Optional;

public interface TwitchChannelRepository extends TwitchRepository<TwitchChannel, Long> {

    Optional<TwitchChannel> findByNameIgnoreCase(String name);
}
