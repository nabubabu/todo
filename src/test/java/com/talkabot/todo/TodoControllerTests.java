package com.talkabot.todo;

import com.talkabot.todo.api.TodoController;
import com.talkabot.todo.persistence.Priority;
import com.talkabot.todo.persistence.Todo;
import com.talkabot.todo.service.TodoService;
import com.talkabot.todo.util.TodoFilter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = TodoController.class)
public class TodoControllerTests {

    @Autowired
    private WebTestClient webClient;

    @MockBean
    private TodoService todoService;

    @Test
    void testCreateTodo() {
        Todo todo = new Todo();
        todo.setId(1L);
        todo.setName("Test");
        todo.setPriority(Priority.LOW);

        Mockito.when(todoService.save(any(Todo.class)))
                .thenReturn(Mono.just(todo));

        WebTestClient.ResponseSpec response = webClient.post().uri("/api/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(todo), Todo.class)
                .exchange();

        response.expectStatus().isCreated()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.id").isEqualTo(todo.getId())
                .jsonPath("$.name").isEqualTo(todo.getName())
                .jsonPath("$.priority").isEqualTo(todo.getPriority().name());
    }

    @Test
    void testUpdateTodo() {
        Todo todo = new Todo();
        todo.setId(1L);
        todo.setName("Test");
        todo.setPriority(Priority.LOW);

        Mockito.when(todoService.update(eq(todo.getId()), any(Todo.class)))
                .thenReturn(Mono.just(1));

        webClient.put().uri("/api/todos/{id}", Collections.singletonMap("id", todo.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(todo), Todo.class)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void testSearch() {

        Mockito.when(todoService.search(any(TodoFilter.class)))
                .thenReturn(Flux.fromIterable(Arrays.asList(new Todo("name", null, Priority.LOW), new Todo("name2", LocalDateTime.now().plusMonths(1), Priority.HIGH))));

        WebTestClient.ResponseSpec response = webClient.get().uri("/api/todos/search")
                .accept(MediaType.APPLICATION_JSON)
                .exchange();

        response.expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Todo.class)
                .consumeWith(System.out::println);
    }

    @Test
    void testDeleteTodo() {
        Mockito.when(todoService.delete(1L))
                .thenReturn(Mono.just(1));

        webClient.delete().uri("/api/todos/{id}", 1)
                .exchange()
                .expectStatus().isOk();
    }

}
