package me.pabiak.kolosreminder;


import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class ReminderListener extends ListenerAdapter {
    private final ReminderService reminderService;

    private final ConfigFileManager configFileManager;

    @Autowired
    public ReminderListener(ReminderService reminderService, ConfigFileManager configFileManager) {
        this.reminderService = reminderService;
        this.configFileManager = configFileManager;
    }

    public void onMessageReceived(MessageReceivedEvent e) {
        if (e.getChannel().getName().equalsIgnoreCase(configFileManager.getReminderChannelName())) {
            TextChannel reminderChannel = e.getGuild().getTextChannelsByName(configFileManager.getReminderChannelName(), true).get(0);
            User user = e.getGuild().getMembersByName(configFileManager.getBotName(), true).get(0).getUser();
            String avatarURL = user.getAvatarUrl();
            Message message = e.getMessage();
            String id = message.getId();
            String content = message.getContentDisplay();
            String[] args = content.split(" ");
            String commandType = args[0].toLowerCase();

            if (!e.getMessage().getAuthor().isBot())
                switch (commandType) {
                    case "!addreminder":
                        addReminder(content, reminderChannel);
                        break;
                    case "!reminders":
                        remindMe(avatarURL, reminderChannel);
                        break;
                    case "!deletereminder":
                        deleteReminder(content, reminderChannel);
                        break;
                    default:
                        reminderChannel.sendMessage("Use the given commands!").queue();
                        break;
                }
            deleteMessages(reminderChannel, message, id);
        }
    }

    private void deleteMessages(TextChannel textChannel, Message message, String id) {
        if (message != null)
            textChannel.deleteMessageById(id).queueAfter(15, TimeUnit.SECONDS);
    }

    private void deleteReminder(String message, TextChannel textChannel) {
        String[] array = message.split(" ");
        String messageToSend = "To delete a remidner, type !deletereminder {id}";
        if (array.length == 2) {
            String reminderId = array[1];
            if (reminderService.isNumeric(reminderId) && reminderService.reminderExists(Long.parseLong(reminderId))) {
                reminderService.deleteReminderById(Long.parseLong(reminderId));
                messageToSend = "Reminder id " + reminderId + " has been removed";
            }
        }
        textChannel.sendMessage(messageToSend).queue();
    }

    private void addReminder(String message, TextChannel textChannel) {
        message = message.replaceAll("!addreminder", "");
        String[] array = message.split(",");
        if (array.length == 4) {
            String type = array[0].toUpperCase().trim();
            String subject = array[1].trim();
            String description = array[2].trim();
            String date = array[3].trim();

            if (reminderService.isValidDate(date)) {
                reminderService.createReminder(new Reminder(type, subject, description, date));
                message = "Reminder has been added to the database";
            } else {
                message = "Wrong date format, use " + configFileManager.getPattern() + " e.g. " + reminderService.todaysDate();
            }
        } else {
            message = "Wrong number of arguments";
        }
        textChannel.sendMessage(message).queue();
    }

    private void remindMe(String avatarURL, TextChannel textChannel) {
        List<Reminder> reminders = reminderService.getAllReminders();
        String message = "The database is empty";
        if (reminders.isEmpty()) {
            textChannel.sendMessage(message).queue();
        } else {
            displayReminders(reminders, avatarURL, textChannel);
        }
    }

    private void displayReminders(List<Reminder> reminders, String avatarURL, TextChannel textChannel) {
        String date = reminderService.todaysDate();
        double maxRemindersOnBoard = 8;
        double numOfReminders = reminderService.getNumOfReminders();
        double numOfBoards = Math.ceil(numOfReminders / maxRemindersOnBoard);

        int reminderCounter = 0;
        int currentBoard = 0;
        for (int i = 0; i < numOfBoards; i++) {
            EmbedBuilder embedBuilder = new EmbedBuilder()
                    .setColor(Color.GREEN)
                    .setTitle("Reminders")
                    .setFooter(date + " " + configFileManager.getBotName() + " Bot")
                    .setThumbnail(avatarURL);
            for (int j = (8 * currentBoard); j < (currentBoard > 0 ? 16 * currentBoard : 8); j++) {
                if (reminderCounter == numOfReminders) break;
                embedBuilder.addField(reminders.get(j).getType(), reminders.get(j).getSubject(), true);
                embedBuilder.addField("Subject", reminders.get(j).getDescription(), true);
                embedBuilder.addField("Date & ID", reminders.get(j).getDate() + " " + "ID:"
                        + reminders.get(j).getId(), true);
                reminderCounter++;
            }
            textChannel.sendMessage(embedBuilder.build()).queue();
            currentBoard++;
        }
    }
}
