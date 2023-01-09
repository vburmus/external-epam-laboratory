package com.epam.esm.tag.controller;

import com.epam.esm.tag.model.Tag;
import com.epam.esm.tag.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.rmi.ServerException;
import java.util.Map;

@RestController
@RequestMapping(value = "/tags", produces = MediaType.APPLICATION_JSON_VALUE)
public class TagController {
    private final TagService tagService;
    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }
    @GetMapping
    public ResponseEntity<?> showAllTags() {
        return new ResponseEntity<>(Map.of("tags", tagService.getAllTags()), HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") long id) throws Exception {
            return new ResponseEntity<>(Map.of("tag", tagService.getTagById(id)), HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<?> createTag(@RequestBody Tag tag) throws ServerException {
            tagService.createTag(tag);
                return new ResponseEntity<>(Map.of("status", HttpStatus.CREATED), HttpStatus.CREATED);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") long id) throws Exception {

            tagService.deleteTag(id);
            return new ResponseEntity<>(Map.of("status", HttpStatus.NO_CONTENT), HttpStatus.NO_CONTENT);

    }

}
