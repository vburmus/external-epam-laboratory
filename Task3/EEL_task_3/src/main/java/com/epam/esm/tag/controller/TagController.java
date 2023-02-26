package com.epam.esm.tag.controller;

import com.epam.esm.order.model.Order;
import com.epam.esm.tag.model.Tag;
import com.epam.esm.tag.service.TagService;
import com.epam.esm.utils.datavalidation.ParamsValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(value = "/tag")
public class TagController {
    public static final String TAGS = "Tags";
    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping({"/{page}",""})
    public ResponseEntity<?> showAllTags(@PathVariable(required = false) Optional<Integer> page) {
       List<Tag> tags = tagService.getAllTags(ParamsValidation.isValidPage(page));
            if (tags.isEmpty())
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            return ResponseEntity.ok(Map.of(TAGS, tags));
    }

    @GetMapping("/search/byId/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") long id) {
        return new ResponseEntity<>(Map.of("tag", tagService.getTagById(id)), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createTag(@RequestBody Tag tag) {
        tagService.createTag(tag);
        return new ResponseEntity<>(Map.of("status", HttpStatus.CREATED), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") long id) {
        tagService.deleteTag(id);
        return new ResponseEntity<>(Map.of("status", HttpStatus.NO_CONTENT), HttpStatus.NO_CONTENT);

    }

}
