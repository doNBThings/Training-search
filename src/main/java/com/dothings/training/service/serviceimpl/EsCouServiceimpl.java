package com.dothings.training.service.serviceimpl;

import com.dothings.training.bean.EsCouBean;
import com.dothings.training.dao.EsCouDao;
import com.dothings.training.service.EsCouService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EsCouServiceimpl implements EsCouService {
    @Autowired
    EsCouDao esCouDao;

    @Override
    public void saveEsCource(EsCouBean esCouBean) {
       esCouDao.save(esCouBean);
    }
}
