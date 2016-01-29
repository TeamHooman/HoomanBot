package com.teamhooman.hoomanbot.kankun;


import com.google.common.collect.ImmutableMap;
import com.teamhooman.hoomanbot.kankun.config.KankunConfig;
import com.teamhooman.hoomanbot.kankun.config.LightSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import java.net.URL;
import java.util.Map;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

//@Controller
//@Profile("kankun")
public class KankunController {
    private final static Logger logger = LoggerFactory.getLogger(KankunController.class);

    private final RestTemplate restTemplate = new RestTemplate();

    private final Map<String, URL> sockets;

    public KankunController(KankunConfig config) {

        checkNotNull(config);

        this.sockets = config.getSockets()
            .stream()
            .collect(
                Collectors.collectingAndThen(
                    Collectors.toMap(
                        LightSocket::getName,
                        LightSocket::getUrl),
                    ImmutableMap::copyOf)
            );
    }

    public void on(String name) {
//        restTemplate.

    }

    public void off(String name) {

    }
}
