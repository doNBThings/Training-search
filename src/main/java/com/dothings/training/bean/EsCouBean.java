package com.dothings.training.bean;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "gaoshiqing",type = "cource")
@Data
public class EsCouBean  {
    @Id
    private String id;
    @Field(analyzer = "ik_smart", searchAnalyzer = "ik_smart", store = true, type = FieldType.text)
    private String cource;
    @Field(type = FieldType.text)
    private String title;
}
