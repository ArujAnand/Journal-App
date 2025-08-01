package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.UserEntity;
import net.engineeringdigest.journalApp.repository.JournalEntryRepository;
import org.apache.catalina.User;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserService userService;

    @Transactional
    public boolean saveEntry(JournalEntry journalEntry, String username) {
        UserEntity user = userService.findByUsername(username);

        journalEntry.setDate(LocalDateTime.now());

        try {
            journalEntryRepository.save(journalEntry);
            user.getJournalEntries().add(journalEntry);
            userService.saveUser(user);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            throw new RuntimeException("An error occured while saving the entry." + exception);
        }
        return true;
    }

//    public List<JournalEntry> getAll() {
//        return journalEntryRepository.findAll();
//    }

    public Optional<JournalEntry> findById(ObjectId myId, String username) {
        UserEntity user = userService.findByUsername(username);

        return user.getJournalEntries().stream().
                filter(journalEntry -> journalEntry.getId().equals(myId)).findAny();
    }

    @Transactional
    public boolean deleteById (ObjectId myId, String username) {
        UserEntity user = userService.findByUsername(username);
        try {
            if(user.getJournalEntries().removeIf(x -> x.getId().equals(myId))) {
                //update user
                userService.saveUser(user);

                //delete from journals db as well after removing reference from user db
                journalEntryRepository.deleteById(myId);
                return true;
            }
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            throw new RuntimeException("An error occured while deleting the entry. " + exception);
        }

        return false;
    }

    @Transactional
    public JournalEntry updateById(ObjectId myId, JournalEntry newEntry, String username) {
        UserEntity user = userService.findByUsername(username);

        Optional<JournalEntry> entry = findById(myId, username);

        if(entry.isPresent()) {
            JournalEntry oldEntry = entry.get();
            oldEntry.setTitle(!newEntry.getTitle().isEmpty() ? newEntry.getTitle() : oldEntry.getTitle());
            oldEntry.setContent(newEntry.getContent() != null && !newEntry.getContent().isEmpty() ? newEntry.getContent() : oldEntry.getContent());
            try {
                journalEntryRepository.save(oldEntry);
            } catch (Exception exception) {
                System.out.println(exception.getMessage());
                throw new RuntimeException("Error while updating the entry. " + exception);
            }
            return oldEntry;
        }
        return null;
    }
}
