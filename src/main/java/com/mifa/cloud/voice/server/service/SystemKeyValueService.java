package com.mifa.cloud.voice.server.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mifa.cloud.voice.server.commons.dto.PageDTO;
import com.mifa.cloud.voice.server.commons.dto.SystemKeyValueEditDTO;
import com.mifa.cloud.voice.server.commons.dto.SystemKeyValueQueryDTO;
import com.mifa.cloud.voice.server.commons.dto.SystemKeyValueVO;
import com.mifa.cloud.voice.server.dao.SystemKeyValueMapper;
import com.mifa.cloud.voice.server.pojo.SystemKeyValue;
import com.mifa.cloud.voice.server.pojo.SystemKeyValueExample;
import com.mifa.cloud.voice.server.utils.BaseBeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/12.
 */
@Service
public class SystemKeyValueService {

    @Autowired
    private SystemKeyValueMapper mapper;

    /**
     * 插入记录（只插入参数非空字段）
     */
    @Transactional(rollbackFor = Exception.class)
    public int insert(SystemKeyValue record) {
        return mapper.insert(record);
    }

    /**
     * 根据字典类型和key获得字典集合
     * */
    public List<SystemKeyValue> getKeyValueListByType(String keyValueType, String key, String contractNo) {
        SystemKeyValueExample example = new SystemKeyValueExample();
        SystemKeyValueExample.Criteria criteria = example.createCriteria();
        if(StringUtils.isNotEmpty(key)) {
            criteria.andParamKeyEqualTo(key);
        }
        if (StringUtils.isNotEmpty(contractNo)) {
            criteria.andCreatedByEqualTo(contractNo);
        }
        criteria.andBizTypeEqualTo(keyValueType);
        return mapper.selectByExample(example);
    }

    /**
     * 根据主键id删除数据
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteByPrimaryKey(Long id) {
        return mapper.deleteByPrimaryKey(id) > 0 ? Boolean.TRUE : Boolean.FALSE;
    }

    /**
     * 分页查询字典列表
     */
    public PageDTO<SystemKeyValueVO> getSystemKeyValuePageList(SystemKeyValueQueryDTO query, Integer page, Integer rows) {
        SystemKeyValueExample example = new SystemKeyValueExample();
        SystemKeyValueExample.Criteria criteria = example.createCriteria();
        if(StringUtils.isNotEmpty(query.getContractNo())) {
            criteria.andCreatedByEqualTo(query.getContractNo());
        }
        if(StringUtils.isNotEmpty(query.getParamValue())) {
            criteria.andParamKeyLike("%" + query.getParamValue() + "%");
        }
        if(StringUtils.isNotEmpty(query.getRemark())) {
            criteria.andRemarkLike("%" + query.getRemark() + "%");
        }
        // 加入分页
        PageHelper.startPage(page, rows);
        example.setOrderByClause("created_at DESC");
        PageInfo<SystemKeyValue> pageInfo = new PageInfo<>(mapper.selectByExample(example));
        List<SystemKeyValueVO> voList = new ArrayList<>();
        pageInfo.getList().forEach(item -> voList.add(BaseBeanUtils.convert(item, SystemKeyValueVO.class)));
        PageDTO<SystemKeyValueVO> pageDTO = BaseBeanUtils.convert(pageInfo,PageDTO.class);
        pageDTO.setList(voList);
        return pageDTO;
    }

    /**
     * 编辑
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateByPrimaryKeySelective(SystemKeyValueEditDTO record) {
        SystemKeyValue systemKeyValue = BaseBeanUtils.convert(record, SystemKeyValue.class);
        return mapper.updateByPrimaryKeySelective(systemKeyValue) > 0 ? Boolean.TRUE : Boolean.FALSE;
    }

    public SystemKeyValue selectByPrimaryKey(Long id) {
        return mapper.selectByPrimaryKey(id);
    }

    /**
     * 根据key查询字典
     */
    public SystemKeyValue selectByKey(String key) {
        if(StringUtils.isEmpty(key)) {
            return null;
        }
        SystemKeyValueExample example = new SystemKeyValueExample();
        SystemKeyValueExample.Criteria criteria = example.createCriteria();
        criteria.andParamKeyEqualTo(key);
        List<SystemKeyValue> values = mapper.selectByExample(example);
        if (values.isEmpty()) return null;
        return values.get(0);
    }











}
