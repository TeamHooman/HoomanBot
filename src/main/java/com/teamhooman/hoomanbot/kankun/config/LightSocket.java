package com.teamhooman.hoomanbot.kankun.config;

import com.google.common.base.MoreObjects;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.Objects;

@Component
public class LightSocket {

    private String name;
    private URL url;


    public String getName() {
        return name;
    }

    public LightSocket setName(String name) {
        this.name = name;
        return this;
    }

    public URL getUrl() {
        return url;
    }

    public LightSocket setUrl(URL url) {
        this.url = url;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LightSocket)) return false;
        LightSocket that = (LightSocket) o;
        return Objects.equals(name, that.name) &&
            Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, url);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(LightSocket.class)
            .add("name", name)
            .add("url", url)
            .toString();
    }
}
