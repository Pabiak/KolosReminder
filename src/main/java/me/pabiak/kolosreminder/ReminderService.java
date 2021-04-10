package me.pabiak.kolosreminder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReminderService {

    private final ReminderRepository reminderRepository;
    private final ConfigFileManager configFileManager;

    @Autowired
    public ReminderService(ReminderRepository reminderRepository, ConfigFileManager configFileManager) {
        this.reminderRepository = reminderRepository;
        this.configFileManager = configFileManager;
    }

    public List<Reminder> getAllReminders() {
        List<Reminder> reminderList = reminderRepository.findAll();
        if (reminderList.isEmpty()) {
            return new ArrayList<>();
        } else {
            return reminderList;
        }
    }

    public void createReminder(Reminder reminder) {
        reminderRepository.save(new Reminder(reminder.getType(), reminder.getSubject(), reminder.getDescription(), reminder.getDate()));
    }

    public void deleteReminderById(Long id) {
        if(reminderRepository.existsById(id))
            reminderRepository.deleteById(id);
    }

    public boolean isNumeric(String text) {
        if (text == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(text);
            if (d <= 0)
                return false;
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public boolean isValidDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(configFileManager.getPattern());
        try {
            LocalDate.parse(date, formatter);
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }

    public boolean reminderExists(Long id) {
        return reminderRepository.existsById(id);
    }

    public double getNumOfReminders(){
       return reminderRepository.count();
    }
}
