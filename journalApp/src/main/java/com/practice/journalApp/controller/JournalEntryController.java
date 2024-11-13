package com.practice.journalApp.controller;

import com.practice.journalApp.entity.JournalEntry;
import com.practice.journalApp.entity.User;
import com.practice.journalApp.service.JournalEntryService;
import com.practice.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    @GetMapping("/{userName}")
    public ResponseEntity<List<JournalEntry>> getJournalsByUserName(@PathVariable String userName){
        User user = userService.findByUserName(userName);
        List<JournalEntry> journalEntries = user.getJournalEntries();
        if(journalEntries != null && !journalEntries.isEmpty()){
            return new ResponseEntity<>(journalEntries, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/{userName}")
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry entry, @PathVariable String userName){
        journalEntryService.saveEntry(entry, userName);
        return new ResponseEntity<>(entry, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JournalEntry> getJournalById(@PathVariable ObjectId id){
        Optional<JournalEntry> journalEntry = journalEntryService.findById(id);
        if(journalEntry.isPresent()){
            return new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{userName}/{id}")
    public ResponseEntity<?> deleteJournalById(@PathVariable ObjectId id, @PathVariable String userName){
        journalEntryService.deleteById(id, userName);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{userName}/{id}")
    public ResponseEntity<JournalEntry> updateJournalById(@PathVariable ObjectId id, @RequestBody JournalEntry entry, @PathVariable String userName){
        JournalEntry journalEntry = journalEntryService.findById(id).orElse(null);
        if(journalEntry != null){
            journalEntry.setTitle(entry.getTitle() != null && !entry.getTitle().equals("") ? entry.getTitle() : journalEntry.getTitle());
            journalEntry.setContent(entry.getContent() != null && !entry.getContent().equals("") ? entry.getContent() : journalEntry.getContent());
            journalEntryService.saveEntry(journalEntry);
            return new ResponseEntity<>(journalEntry, HttpStatus.CREATED);
        }
        journalEntryService.saveEntry(entry);
        return new ResponseEntity<>(entry, HttpStatus.CREATED);
    }
}
