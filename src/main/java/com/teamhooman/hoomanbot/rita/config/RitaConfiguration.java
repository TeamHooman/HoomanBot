package com.teamhooman.hoomanbot.rita.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import rita.RiMarkov;

import javax.validation.constraints.NotNull;

@ConfigurationProperties("rita")
@Configuration
@Profile("rita")
public class RitaConfiguration {

    @NotNull(message = "The Markov nFactor must be specified.")
    private int nFactor;

    public int getnFactor() {
        return nFactor;
    }

    public RitaConfiguration setnFactor(int nFactor) {
        this.nFactor = nFactor;
        return this;
    }

    @Bean
    public RiMarkov mkRitaMarkov() {
        return new RiMarkov(nFactor, true);
    }
}
