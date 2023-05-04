package com.talkabot.todo.persistence;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.Id;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
@JsonIgnoreProperties(ignoreUnknown = true)
public class Todo extends BaseEntity {

    @Column
    @NotNull
    private String name;

    @Column
    private LocalDateTime deadline;

    @Column
    @NotNull
    @Enumerated(value = EnumType.STRING)
    private Priority priority;

}
