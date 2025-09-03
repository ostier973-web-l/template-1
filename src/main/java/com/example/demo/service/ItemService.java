package com.example.demo.service;

import com.example.demo.client.ExternalServiceClient;
import com.example.demo.model.Item;
import com.example.demo.repository.ItemRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ItemService {
    private final ItemRepository repository;
    private final RedisTemplate<String, Item> redisTemplate;
    private final ExternalServiceClient externalServiceClient;

    public ItemService(ItemRepository repository,
                       RedisTemplate<String, Item> redisTemplate,
                       ExternalServiceClient externalServiceClient) {
        this.repository = repository;
        this.redisTemplate = redisTemplate;
        this.externalServiceClient = externalServiceClient;
    }

    public Item create(Item item) {
        Item saved = repository.save(item);
        redisTemplate.opsForValue().set(buildKey(saved.getId()), saved);
        return saved;
    }

    public Optional<Item> get(String id) {
        Item cached = redisTemplate.opsForValue().get(buildKey(id));
        if (cached != null) {
            return Optional.of(cached);
        }
        Optional<Item> found = repository.findById(id);
        found.ifPresent(i -> redisTemplate.opsForValue().set(buildKey(id), i));
        return found;
    }

    public Item update(String id, Item item) {
        item.setId(id);
        Item saved = repository.save(item);
        redisTemplate.opsForValue().set(buildKey(id), saved);
        return saved;
    }

    public void delete(String id) {
        repository.deleteById(id);
        redisTemplate.delete(buildKey(id));
    }

    public String getExternalStatus() {
        return externalServiceClient.getStatus();
    }

    private String buildKey(String id) {
        return "item:" + id;
    }
}
