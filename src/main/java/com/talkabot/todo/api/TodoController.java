package com.talkabot.todo.api;

import com.talkabot.todo.persistence.Todo;
import com.talkabot.todo.service.TodoService;
import com.talkabot.todo.util.MapperUtil;
import com.talkabot.todo.util.TodoFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/api/todos")
public class TodoController {

    private final TodoService service;

    @Autowired
    public TodoController(TodoService todoService) {
        this.service = todoService;
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Todo>> findById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Todo> create(@RequestBody Todo todo) {
        return service.save(todo);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Integer>> update(@PathVariable Long id, @RequestBody Todo todo) {
        return service.update(id, todo)
                .map(e -> e > 0 ? ResponseEntity.ok(e) : ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Integer>> delete(@PathVariable Long id) {
        return service.delete(id)
                .map(e -> e > 0 ? ResponseEntity.ok(e) : ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public Flux<Todo> search(@RequestParam Map<String, Object> params) {
        TodoFilter filter = MapperUtil.get().convertValue(params, TodoFilter.class);
        return service.search(filter);
    }

}
