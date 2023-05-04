package com.talkabot.todo.service;

import com.talkabot.todo.persistence.Todo;
import com.talkabot.todo.util.TodoFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository repository;

    public Mono<Todo> findById(Long id) {
        return repository.findById(id);
    }

    public Mono<Integer> update(Long id, Todo todo) {
        return repository.update(id, todo);
    }

    public Mono<Integer> delete(Long id) {
        return repository.deleteById(id);
    }

    public Mono<Todo> save(Todo todo) {
        todo.setCreated(LocalDateTime.now());
        return repository.save(todo);
    }

    public Flux<Todo> search(TodoFilter filter) {
        return repository.findByFilter(filter);
    }
}
