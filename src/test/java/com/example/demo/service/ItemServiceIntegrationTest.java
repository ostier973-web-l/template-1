package com.example.demo.service;

import com.example.demo.model.Item;
import com.example.demo.repository.ItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest
public class ItemServiceIntegrationTest {

    @Container
    static MongoDBContainer mongo = new MongoDBContainer("mongo:7.0.9");

    @Container
    static GenericContainer<?> redis = new GenericContainer<>("redis:7-alpine").withExposedPorts(6379);

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongo::getReplicaSetUrl);
        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port", () -> redis.getMappedPort(6379));
    }

    @Autowired
    private ItemService service;

    @Autowired
    private ItemRepository repository;

    @Test
    void shouldCreateAndRetrieveItem() {
        Item item = new Item(null, "test", "desc");
        Item saved = service.create(item);

        Optional<Item> found = service.get(saved.getId());
        assertTrue(found.isPresent());
        assertEquals("test", found.get().getName());
    }

    @Test
    void shouldReturnItemFromCacheAfterDeletionFromDatabase() {
        Item item = new Item(null, "cache", "desc");
        Item saved = service.create(item);

        repository.deleteById(saved.getId());

        Optional<Item> fromCache = service.get(saved.getId());
        assertTrue(fromCache.isPresent());
        assertEquals("cache", fromCache.get().getName());
    }
}
