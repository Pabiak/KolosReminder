package me.pabiak;

import me.pabiak.kolosreminder.ConfigFileManager;
import me.pabiak.kolosreminder.ReminderListener;
import me.pabiak.rolemanager.RoleListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.security.auth.login.LoginException;

@Service
public class DiscordBuilder extends ListenerAdapter {
    private final ReminderListener reminderListener;
    private final ConfigFileManager configFileManager;
    private final RoleListener roleListener;

    @Autowired
    public DiscordBuilder(ReminderListener reminderListener, ConfigFileManager configFileManager, RoleListener roleListener) {
        this.reminderListener = reminderListener;
        this.configFileManager = configFileManager;
        this.roleListener = roleListener;
    }

    @PostConstruct
    public void buildDiscordBot() throws LoginException {
        JDA jda = JDABuilder.createDefault(configFileManager.getSecretToken()).build();
        jda.getPresence().setActivity(Activity.playing("IntelliJ IDE"));
        jda.addEventListener(reminderListener,roleListener);
    }
}
