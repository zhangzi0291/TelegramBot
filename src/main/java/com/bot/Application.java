package com.bot;

import com.bot.bots.MsgBot;
import com.bot.data.Constant;
import com.bot.util.CsvParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.ApiContext;
import org.telegram.telegrambots.meta.TelegramBotsApi;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.glassfish.jersey.client.ClientProperties.PROXY_PASSWORD;

@SpringBootApplication
@EnableScheduling
public class Application {
    @Value("${proxy.enable}")
    private Boolean PROXY_ENABLE ;
    @Value("${proxy.host}")
    private String PROXY_HOST ;
    @Value("${proxy.port}")
    private Integer PROXY_PORT ;
    @Value("${proxy.user}")
    private String PROXY_USER ;
    @Value("${proxy.password}")
    private String PROXY_PASSWORD ;

    public static void main(String[] args) {
        //Add this line to initialize bots context
        ApiContextInitializer.init();
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public TelegramLongPollingBot msgBot(){


        // Set up Http proxy
        DefaultBotOptions botOptions = ApiContext.getInstance(DefaultBotOptions.class);

        if(PROXY_ENABLE) {
            Authenticator.setDefault(new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(PROXY_USER, PROXY_PASSWORD.toCharArray());
                }
            });
            botOptions.setProxyHost(PROXY_HOST);
            botOptions.setProxyPort(PROXY_PORT);
            // Select proxy type: [HTTP|SOCKS4|SOCKS5] (default: NO_PROXY)
            botOptions.setProxyType(DefaultBotOptions.ProxyType.SOCKS5);
        }


        return new MsgBot(botOptions);
    }
}
