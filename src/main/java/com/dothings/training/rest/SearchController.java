package com.dothings.training.rest;


import com.dothings.training.bean.EsAggBean;
import com.dothings.training.bean.EsCouBean;
import com.dothings.training.bean.EsPicBean;
import com.dothings.training.service.EsCouService;
import com.dothings.training.utils.JSONUtils;
import com.dothings.training.utils.result.RestData;
import com.dothings.training.utils.result.RestResp;
import io.swagger.annotations.*;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@Path("/")
@RestController
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Api(tags = {"搜索模块"})


public class SearchController {
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;
    @Autowired
    private EsCouService esCouService;


    @Path("/search/aggregation")
    @GET
    @ApiOperation(value = "对es进行词频分析返回top N", notes = "搜索模块")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "成功", response = RestResp.class),
            @ApiResponse(code = 500, message = "失败"),
            @ApiResponse(code = 1, message = "top N", response = EsAggBean.class)
    })
    public RestResp<EsAggBean> AggtopN(@ApiParam(value = "top N", required = true) @QueryParam("topN") int topN) {
        TermsAggregationBuilder field = AggregationBuilders.terms("title_count").field("title");

        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchAllQuery())
                .withSearchType(SearchType.DEFAULT)
                .withIndices("qiqi").withTypes("meimei")
                .addAggregation(field)
                .build();
        List<EsAggBean> esAggBeans = new ArrayList<>();
        // when
        Aggregations aggregations = elasticsearchTemplate.query(searchQuery, new ResultsExtractor<Aggregations>() {
            @Override
            public Aggregations extract(SearchResponse response) {
                return response.getAggregations();
            }
        });
        Map<String, Aggregation> map = aggregations.asMap();
        for (String s : map.keySet()) {
            StringTerms a = (StringTerms) map.get(s);
            List<StringTerms.Bucket> list = a.getBuckets();
            for (Terms.Bucket b : list) {
                System.out.println("key is " + b.getKeyAsString() + "---and value is " + b.getDocCount());
                EsAggBean esAggBean = new EsAggBean();
                esAggBean.setName(b.getKeyAsString());
                esAggBean.setNumber(b.getDocCount());
                if (esAggBeans.size() <= topN) {
                    esAggBeans.add(esAggBean);
                }
            }

        }
        return new RestResp<EsAggBean>(esAggBeans, null);
    }


    @Path("/search/save")
    @POST
    @ApiOperation(value = "保存课程库", notes = "搜索模块")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "成功", response = RestResp.class),
            @ApiResponse(code = 500, message = "失败"),
            @ApiResponse(code = 1, message = "top N", response = EsCouBean.class)
    })
    public RestResp<EsCouBean> sava(@ApiParam(value="bean")@QueryParam("bean") String bean) {
        EsCouBean esCouBean = JSONUtils.fromJson(bean, EsCouBean.class);
        esCouService.saveEsCource(esCouBean);
        return  new RestResp<EsCouBean>(esCouBean,null);
    }


    @Path("/search/data")
    @POST
    @ApiOperation(value = "搜索美女的图片", notes = "美女模块")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "成功", response = RestResp.class),
            @ApiResponse(code = 500, message = "失败"),
            @ApiResponse(code = 1, message = "代理人", response = EsPicBean.class)
    })
    public RestResp<EsPicBean> searchdata(@ApiParam(value = "搜索词条", required = true) @FormParam(value = "keyword") @DefaultValue("柳岩")  String keyword, @ApiParam(value = "当前页", required = true) @FormParam("page") int page, @ApiParam(value = "返回条数", required = true) @FormParam("size") int size) {
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchQuery("title", keyword))
                .withSearchType(SearchType.DEFAULT)
                .withHighlightFields(new HighlightBuilder.Field("title"))
                .withIndices("qiqi").withTypes("meimei").withPageable(new PageRequest(page - 1, size))
                .build();
        //List<Picture> pictures = elasticsearchTemplate.queryForList(searchQuery, Picture.class);
        ArrayList<EsPicBean> esPicBeans = new ArrayList<EsPicBean>();
        final long[] totalHits = {new Long(0L)};
        AggregatedPage<EsPicBean> pictures = elasticsearchTemplate.queryForPage(searchQuery, EsPicBean.class, new SearchResultMapper() {

            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse searchResponse, Class<T> aClass, Pageable pageable) {

                SearchHits hits = searchResponse.getHits();
                totalHits[0] = hits.getTotalHits();
                for (SearchHit searchHit : hits) {
                    if (hits.getHits().length <= 0) {
                        return null;
                    }
                    EsPicBean esPicBean = new EsPicBean();
                    String highLightMessage = searchHit.getHighlightFields().get("title").fragments()[0].toString();
                    esPicBean.setTitle(String.valueOf(searchHit.getSource().get("title")));
                    esPicBean.setUrl(String.valueOf(searchHit.getSource().get("url")));
                    // 反射调用set方法将高亮内容设置进去
                    try {
                        String setMethodName = parSetName("title");
                        Class<? extends EsPicBean> poemClazz = esPicBean.getClass();
                        Method setMethod = poemClazz.getMethod(setMethodName, String.class);
                        setMethod.invoke(esPicBean, highLightMessage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    esPicBeans.add(esPicBean);
                }
                if (esPicBeans.size() > 0) {
                    return new AggregatedPageImpl<T>((List<T>) esPicBeans);
                }
                return null;
            }
        });
        List<EsPicBean> content = pictures.getContent();
        RestData<EsPicBean> objectRestData = new RestData<>();
        objectRestData.setRsCount(totalHits[0]);
        objectRestData.setRsData(content);
        return new RestResp<EsPicBean>(objectRestData, null);
    }


    private static String parSetName(String fieldName) {
        if (null == fieldName || "".equals(fieldName)) {
            return null;
        }
        int startIndex = 0;
        if (fieldName.charAt(0) == '_')
            startIndex = 1;
        return "set" + fieldName.substring(startIndex, startIndex + 1).toUpperCase()
                + fieldName.substring(startIndex + 1);
    }
}
