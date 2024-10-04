package com.practice.journalApp.controller;

import com.practice.journalApp.entity.JournalEntry;
import com.practice.journalApp.service.JournalEntryService;
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
public class JournalEntryControllerV2 {

    @Autowired
    private JournalEntryService journalEntryService;

    @GetMapping
    public ResponseEntity<List<JournalEntry>> getJournals(){
        return new ResponseEntity<>(journalEntryService.getAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry entry){
        entry.setDate(LocalDateTime.now());
        journalEntryService.saveEntry(entry);
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

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteJournalById(@PathVariable ObjectId id){
        journalEntryService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<JournalEntry> updateJournalById(@PathVariable ObjectId id, @RequestBody JournalEntry entry){
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
