package com.dothings.training.dao;


import com.dothings.training.bean.EsCouBean;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public  interface EsCouDao extends ElasticsearchRepository<EsCouBean,String> {
}
