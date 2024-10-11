package com.erdi.todoapp.model.entity;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.id.GeneratedValue;
import org.springframework.data.couchbase.core.mapping.id.GenerationStrategy;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationStrategy.UNIQUE)
    private String id;

    @Field
    private String userId;

    @Field
    private String title;

    @Field
    private String content;

    @Field
    private boolean deleted;

    @Field
    private Date createDate;

    @Field
    private Date completeDate;

    @Field
    private Date updateDate;
}