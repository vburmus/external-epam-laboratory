package com.epam.esm.tag.controller;


import com.epam.esm.tag.model.TagDTO;
import com.epam.esm.tag.service.TagService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.epam.esm.utils.Constants.*;

@RestController
@RequestMapping(value = "/tag", produces = MediaType.APPLICATION_JSON_VALUE)
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @PostMapping
    public ResponseEntity<?> createTag(@RequestBody TagDTO tagDTO) {
        return new ResponseEntity<>(Map.of(TAG, tagService.createTag(tagDTO)), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<?> showAllTags(@RequestParam(required = false, defaultValue = DEFAULT_PAGE) Integer page,
                                         @RequestParam(required = false, defaultValue = DEFAULT_SIZE) Integer size) {
        return new ResponseEntity<>(Map.of(OBJECTS, tagService.getAllTags(--page, size)), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") long id) {
        return new ResponseEntity<>(Map.of(TAG, tagService.getTagById(id)), HttpStatus.OK);
    }

    @GetMapping("/search/most-used")
    public ResponseEntity<?> getMostUsed() {
        return new ResponseEntity<>(Map.of(TAG, tagService.getMostUsedTag()), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") long id) {
        return new ResponseEntity<>(Map.of(RESULT, tagService.deleteTag(id)), HttpStatus.OK);
    }


}
