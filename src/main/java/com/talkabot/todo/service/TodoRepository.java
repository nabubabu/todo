package com.talkabot.todo.service;

import com.talkabot.todo.persistence.Todo;
import com.talkabot.todo.util.TodoFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.data.relational.core.query.Update;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.talkabot.todo.persistence.BaseEntity.Fields.created;
import static com.talkabot.todo.persistence.Todo.Fields.*;
import static org.springframework.data.relational.core.query.Criteria.where;

@Component
@RequiredArgsConstructor
public class TodoRepository {

    private final R2dbcEntityTemplate template;

    Mono<Todo> findById(Long id) {
        return template.selectOne(Query.query(where("id").is(id)), Todo.class);
    }

    Mono<Todo> save(Todo todo) {
        return template.insert(Todo.class)
                .using(todo);
    }

    Mono<Integer> update(Long id, Todo todo) {
        return template.update(
                Query.query(where("id").is(id)),
                Update.update(name, todo.getName())
                        .set(deadline, todo.getDeadline())
                        .set(priority, todo.getPriority()),
                Todo.class);
    }

    Mono<Integer> deleteById(Long id) {
        return template.delete(Query.query(where("id").is(id)), Todo.class);
    }

    Flux<Todo> findByFilter(TodoFilter filter) {
        Criteria criteria = Criteria.empty();
        if (filter.getName() != null) {
            criteria = criteria.and(where(name).like("%" + filter.getName() + "%"));
        }
        if (filter.getCreatedFrom() != null) {
            criteria = criteria.and(where(created).greaterThanOrEquals(filter.getCreatedFrom()));
        }
        if (filter.getCreatedTo() != null) {
            criteria = criteria.and(where(created).lessThanOrEquals(filter.getCreatedTo()));
        }
        if (filter.getDeadlineFrom() != null) {
            criteria = criteria.and(where(deadline).greaterThanOrEquals(filter.getDeadlineFrom()));
        }
        if (filter.getDeadlineTo() != null) {
            criteria = criteria.and(where(deadline).lessThanOrEquals(filter.getDeadlineTo()));
        }
        if (filter.getPriority() != null) {
            criteria = criteria.and(where(priority).is(filter.getPriority().name()));
        }

        Query query = Query.query(criteria)
                .limit(filter.getLimit()).offset(filter.getOffset())
                .sort(Sort.by(Sort.Direction.fromString(filter.getDir().toUpperCase()), filter.getSort()));

        return template.select(Todo.class)
                .matching(query)
                .all();
    }

}
