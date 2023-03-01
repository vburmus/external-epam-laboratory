package com.epam.esm.tag.controller;


import com.epam.esm.tag.model.Tag;
import com.epam.esm.tag.service.TagService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.epam.esm.utils.Constants.*;

@RestController
@RequestMapping(value = "/tag")
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    public ResponseEntity<?> showAllTags(@RequestParam(required = false, defaultValue = DEFAULT_PAGE) Integer page, @RequestParam(required = false, defaultValue = DEFAULT_SIZE) Integer size) {
        List<Tag> tags = tagService.getAllTags(page, size);
        return new ResponseEntity<>(tags.isEmpty()? HttpStatus.NOT_FOUND:Map.of(TAGS, tags),HttpStatus.OK);
    }

    @GetMapping("/search/byId/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") long id) {
        return new ResponseEntity<>(Map.of(TAG, tagService.getTagById(id)), HttpStatus.OK);
    }

    @GetMapping("search/mostUsed")
    public ResponseEntity<?> getMostUsed() {
        return ResponseEntity.ok(Map.of(TAG, tagService.getMostUsedTag()));
    }

    @PostMapping
    public ResponseEntity<?> createTag(@RequestBody Tag tag) {
        return new ResponseEntity<>(tagService.createTag(tag)?HttpStatus.CREATED:HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") long id) {
        tagService.deleteTag(id);
        return new ResponseEntity<>(Map.of(STATUS, HttpStatus.NO_CONTENT), HttpStatus.NO_CONTENT);
    }

}
