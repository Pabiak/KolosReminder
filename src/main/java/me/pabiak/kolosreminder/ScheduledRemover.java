package me.pabiak.kolosreminder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ScheduledRemover {

    private final ReminderService reminderService;
    private final ConfigFileManager configFileManager;

    @Autowired
    public ScheduledRemover(ReminderService reminderService, ConfigFileManager configFileManager) {
        this.reminderService = reminderService;
        this.configFileManager = configFileManager;
    }

    @Scheduled(fixedRate = 1200000) //20 minutes
    public void deleteReminderFromDataBase() {
        List<Reminder> reminderList = reminderService.getAllReminders();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(configFileManager.getPattern());
        String todaysDate = reminderService.todaysDate();

        reminderList.forEach(reminder -> {
            LocalDate reminderDate = LocalDate.parse(reminder.getDate(), formatter);
            if (reminderDate.isBefore(LocalDate.parse(todaysDate, formatter))) {
                reminderService.deleteReminderById(reminder.getId());
            }
        });
    }
}
