package com.teamhooman.hoomanbot.kankun.config;

import com.google.common.base.MoreObjects;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Objects;

@Configuration
@ConfigurationProperties(prefix = "kankun")
public class KankunConfig {

    @NestedConfigurationProperty
    private List<LightSocket> sockets;

    public List<LightSocket> getSockets() {
        return sockets;
    }

    public KankunConfig setSockets(List<LightSocket> sockets) {
        this.sockets = sockets;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof KankunConfig)) return false;
        KankunConfig that = (KankunConfig) o;
        return Objects.equals(sockets, that.sockets);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sockets);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(KankunConfig.class)
            .add("sockets", sockets)
            .toString();
    }
}
