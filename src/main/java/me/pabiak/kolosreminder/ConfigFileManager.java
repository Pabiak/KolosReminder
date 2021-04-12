package me.pabiak.kolosreminder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Configuration
@PropertySource("classpath:bot.properties")
public class ConfigFileManager {

    private final String timeZone;
    private final String pattern;
    private final String secretToken;
    private final String botName;
    private final String reminderChannelName;
    private final String roleChannelName;

    @Autowired
    public ConfigFileManager(@Value("${pabiakbot.timeZone}") String timeZone,
                             @Value("${pabiakbot.datePattern}") String pattern,
                             @Value("${pabiakbot.token}") String secretToken,
                             @Value("${pabiakbot.botName}") String botName,
                             @Value("${pabiakbot.reminderChannelName}") String reminderChannelName,
                             @Value("${pabiakbot.roleChannelName}") String roleChannelName) {
        this.timeZone = timeZone;
        this.pattern = pattern;
        this.secretToken = secretToken;
        this.botName = botName;
        this.reminderChannelName = reminderChannelName;
        this.roleChannelName = roleChannelName;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public String getPattern() {
        return pattern;
    }

    public String getSecretToken() {
        return secretToken;
    }

    public String getBotName() {
        return botName;
    }

    public String getReminderChannelName() {
        return reminderChannelName;
    }

    public String getRoleChannelName() {
        return roleChannelName;
    }
}
