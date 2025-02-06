package com.example.todospring.config;

import com.example.todospring.model.Todo;
import com.example.todospring.repository.TodoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Configuration
public class DataInitializer {

    @Bean
    @Profile("!test") // Don't run this for test profile
    CommandLineRunner initDatabase(TodoRepository repository) {
        return args -> {
            if (repository.count() == 0) { // Only populate if DB is empty
                List<Todo> todos = new ArrayList<>();
                Random random = new Random();
                
                for (int i = 1; i <= 1000; i++) {
                    Todo todo = new Todo();
                    todo.setTitle("Todo " + i);
                    todo.setDescription("Description for todo " + i);
                    todo.setCompleted(random.nextBoolean());
                    todos.add(todo);
                }
                
                repository.saveAll(todos);
            }
        };
    }
}