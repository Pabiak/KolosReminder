package me.pabiak.rolemanager;

import me.pabiak.kolosreminder.ConfigFileManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class RoleListener extends ListenerAdapter {

    private final ConfigFileManager configFileManager;

    @Autowired
    public RoleListener(ConfigFileManager configFileManager) {
        this.configFileManager = configFileManager;
    }

    private final List<String> blacklist = Arrays.asList("root","bot","@everyone");

    public void onMessageReceived(MessageReceivedEvent e) {
        if (!e.getMessage().getAuthor().isBot()) {
            if (e.getChannel().getName().equalsIgnoreCase(configFileManager.getRoleChannelName())) {
                TextChannel textChannel = e.getGuild().getTextChannelsByName(configFileManager.getRoleChannelName(), true).get(0);
                Message message = e.getMessage();
                Member member = message.getMember();
                String id = message.getId();
                String content = message.getContentDisplay().trim();

                Role role = e.getGuild().getRoles().stream()
                        .filter(r -> r.getName().equalsIgnoreCase(content))
                        .filter(r -> !blacklist.contains(r.getName().toLowerCase()))
                        .findFirst()
                        .orElse(null);

                if (role != null && member != null)
                    try {
                        toggleRole(member, role);
                        message.addReaction("U+2705").queue();
                    } catch (HierarchyException he) {
                        message.addReaction("U+274C").queue();
                    }
                else
                    message.addReaction("U+274C").queue();
                deleteMessages(textChannel, message, id);
            }
        }
    }

    private void toggleRole(Member member, Role role) {
        List<Role> roles = member.getRoles();
        if (roles.contains(role))
            member.getGuild().removeRoleFromMember(member, role).queue();
        else
            member.getGuild().addRoleToMember(member, role).queue();
    }

    private void deleteMessages(TextChannel textChannel, Message message, String id) {
        if (message != null)
            textChannel.deleteMessageById(id).queueAfter(5, TimeUnit.SECONDS);
    }
}