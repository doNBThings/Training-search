package com.dothings.training.bean;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @author linzhiqiang
 * @date 2018/5/16
 */
@Document(indexName = "qiqi",type = "meimei")
@Data
public class EsPicBean {
    @Id
    private String id;
    @Field(analyzer = "ik_smart", searchAnalyzer = "ik_smart", store = true, type = FieldType.text)
    private String title;
    @Field(type = FieldType.text)
    private String url;

}
