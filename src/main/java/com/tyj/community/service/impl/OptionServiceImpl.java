package com.tyj.community.service.impl;

import com.tyj.community.dao.OptionVoMapper;
import com.tyj.community.entity.OptionVo;
import com.tyj.community.entity.OptionVoExample;
import com.tyj.community.service.OptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by tyj on 2018/11/28.
 */
@Service
public class OptionServiceImpl implements OptionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OptionServiceImpl.class);

    @Resource
    private OptionVoMapper optionDao;

    @Override
    public void insertOption(OptionVo optionVo) {
        LOGGER.debug("Enter insertOption method:optionVo={}" ,optionVo);
        optionDao.insertSelective(optionVo);
        LOGGER.debug("Exit insertOption method.");
    }

    @Override
    public void insertOption(String name, String value) {
        LOGGER.debug("Enter insertOption method:name={},value={}",name,value );
        OptionVo optionVo = new OptionVo();
        optionVo.setName(name);
        optionVo.setValue(value);
        if(optionDao.selectByExample(new OptionVoExample()).size()==0){
            optionDao.insertSelective(optionVo);
        }else{
            optionDao.updateByPrimaryKeySelective(optionVo);
        }
        LOGGER.debug("Exit insertOption method.");
    }

    @Override
    public void saveOptions(Map<String, String> options) {
        if (null != options && !options.isEmpty()) {
            options.forEach(this::insertOption);
        }
    }

    @Override
    public List<OptionVo> getOptions(){
        return optionDao.selectByExample(new OptionVoExample());
    }
}

