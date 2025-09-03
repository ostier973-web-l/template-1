package com.example.demo.controller;

import com.example.demo.model.Item;
import com.example.demo.service.ItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService service;

    public ItemController(ItemService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Item> create(@RequestBody Item item) {
        return ResponseEntity.ok(service.create(item));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> get(@PathVariable String id) {
        Optional<Item> item = service.get(id);
        return item.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        }

    @PutMapping("/{id}")
    public ResponseEntity<Item> update(@PathVariable String id, @RequestBody Item item) {
        return ResponseEntity.ok(service.update(id, item));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/external/status")
    public ResponseEntity<String> externalStatus() {
        return ResponseEntity.ok(service.getExternalStatus());
    }
}
