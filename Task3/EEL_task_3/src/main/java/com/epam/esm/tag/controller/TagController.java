package com.epam.esm.tag.controller;


import com.epam.esm.tag.model.Tag;
import com.epam.esm.tag.service.TagService;
import com.epam.esm.utils.datavalidation.ParamsValidation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        return ParamsValidation.isNotFound(tagService.getAllTags(page, size));
    }

    @GetMapping("/search/byId/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") long id) {
        return new ResponseEntity<>(Map.of(TAG, tagService.getTagById(id)), HttpStatus.OK);
    }

    @GetMapping("search/mostUsed")
    public ResponseEntity<?> getMostUsed() {
        return new ResponseEntity<>(Map.of(TAG, tagService.getMostUsedTag()), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createTag(@RequestBody Tag tag) {
        return new ResponseEntity<>(Map.of(TAG, tagService.createTag(tag)), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") long id) {
        return new ResponseEntity<>(Map.of(RESULT,tagService.deleteTag(id)),HttpStatus.OK);
    }


}
