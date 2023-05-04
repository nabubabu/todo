package com.talkabot.todo.util;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.talkabot.todo.persistence.Priority;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TodoFilter {

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate createdFrom;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate createdTo;
    private String name;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate deadlineFrom;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate deadlineTo;
    private Priority priority;
    private int offset = 0;
    private int limit = 100;
    private String sort = "created";
    private String dir = "asc";

}
