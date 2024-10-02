package com.practice.journalApp.controller;

import com.practice.journalApp.entity.JournalEntry;
import com.practice.journalApp.service.JournalEntryService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/journal")
public class JournalEntryControllerV2 {

    @Autowired
    private JournalEntryService journalEntryService;

    @GetMapping
    public List<JournalEntry> getJournals(){
        return journalEntryService.getAll();
    }

    @PostMapping
    public JournalEntry createEntry(@RequestBody JournalEntry entry){
        entry.setDate(LocalDateTime.now());
        journalEntryService.saveEntry(entry);
        return entry;
    }

    @GetMapping("/{id}")
    public JournalEntry getJournalById(@PathVariable ObjectId id){
        return journalEntryService.findById(id).orElse(null);
    }

    @DeleteMapping("/{id}")
    public boolean deleteJournalById(@PathVariable ObjectId id){
        journalEntryService.deleteById(id);
        return true;
    }

    @PutMapping("/{id}")
    public JournalEntry updateJournalById(@PathVariable ObjectId id, @RequestBody JournalEntry entry){
        JournalEntry journalEntry = journalEntryService.findById(id).orElse(null);
        if(journalEntry != null){
            journalEntry.setTitle(entry.getTitle() != null && !entry.getTitle().equals("") ? entry.getTitle() : journalEntry.getTitle());
            journalEntry.setContent(entry.getContent() != null && !entry.getContent().equals("") ? entry.getContent() : journalEntry.getContent());
            journalEntryService.saveEntry(journalEntry);
            return journalEntry;
        }
        journalEntryService.saveEntry(entry);
        return entry;
    }
}
