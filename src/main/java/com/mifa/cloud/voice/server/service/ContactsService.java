package com.mifa.cloud.voice.server.service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mifa.cloud.voice.server.commons.dto.*;
import com.mifa.cloud.voice.server.commons.enums.SexEnum;
import com.mifa.cloud.voice.server.commons.enums.StatusEnum;
import com.mifa.cloud.voice.server.component.properties.AppProperties;
import com.mifa.cloud.voice.server.dao.CustomerTaskContactGroupDAO;
import com.mifa.cloud.voice.server.dao.CustomerTaskUserContactsDAO;
import com.mifa.cloud.voice.server.dao.UploadFileLogMapper;
import com.mifa.cloud.voice.server.exception.BaseBizException;
import com.mifa.cloud.voice.server.pojo.CustomerTaskContactGroupDO;
import com.mifa.cloud.voice.server.pojo.CustomerTaskUserContactsDO;
import com.mifa.cloud.voice.server.pojo.CustomerTaskUserContactsDOExample;
import com.mifa.cloud.voice.server.pojo.UploadFileLog;
import com.mifa.cloud.voice.server.utils.BaseBeanUtils;
import com.mifa.cloud.voice.server.utils.EncodesUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

/**
 * @author: songxm
 * @date: 2018/4/12 10:13
 * @version: v1.0.0
 */
@Service
@Slf4j
public class ContactsService {
    @Autowired
    CustomerTaskUserContactsDAO contactsDAO;
    @Autowired
    UploadFileLogMapper uploadFileLogMapper;
    @Autowired
    AppProperties appProperties;
    @Autowired
    CustomerTaskContactGroupDAO taskContactGroupDAO;
    /**
     * 查询号码
     *
     * @return
     */
    public PageDTO<ContactRspDTO> queryContactList(ContactQueryDTO contactQueryDTO, Integer pageNum, Integer pageSize) {
        CustomerTaskUserContactsDO contactDo = BaseBeanUtils.convert(contactQueryDTO, CustomerTaskUserContactsDO.class);
            CustomerTaskUserContactsDOExample example = new CustomerTaskUserContactsDOExample();
            CustomerTaskUserContactsDOExample.Criteria criteria = example.createCriteria();
            criteria.andContractNoEqualTo(contactQueryDTO.getContractNo());
            criteria.andTaskIdEqualTo(contactQueryDTO.getTaskId());
            criteria.andStatusEqualTo(StatusEnum.NORMAL.getCode().toString());
            if (StringUtils.isNotEmpty(contactQueryDTO.getUserName())) {
                criteria.andUserNameEqualTo(contactQueryDTO.getUserName());
            }
            if (StringUtils.isNotEmpty(contactQueryDTO.getUserPhone())) {
                criteria.andUserPhoneEqualTo(EncodesUtils.selfEncrypt(contactQueryDTO.getUserPhone(),appProperties.getSalt()));
            }
            if (StringUtils.isNotEmpty(contactQueryDTO.getOrgName())) {
                criteria.andOrgNameEqualTo(contactQueryDTO.getOrgName());
            }
            example.setOrderByClause("created_at desc");
            PageHelper.startPage(pageNum, pageSize);
            List<CustomerTaskUserContactsDO> contactListDOs = contactsDAO.selectByExample(example);
            PageInfo<CustomerTaskUserContactsDO> pageInfo = new PageInfo<>(contactListDOs);
            if (pageInfo != null && CollectionUtils.isNotEmpty(pageInfo.getList())) {
                List<ContactRspDTO> contactDtos = new ArrayList<>();

                pageInfo.getList().stream().forEach(contact -> {
                    ContactRspDTO contactDto = BaseBeanUtils.convert(contact, ContactRspDTO.class);
                    contactDto.setUserSex(contactDto.getUserSex());
                    try {
                        contactDto.setUserPhone(EncodesUtils.selfDecrypt(contactDto.getUserPhone(),appProperties.getSalt()));
                    } catch (Exception e) {
                        log.info("查询敏感数据解密失败:{}", e);
                    }
                    contactDtos.add(contactDto);
                });
                PageDTO<ContactRspDTO> pageResult = BaseBeanUtils.convert(pageInfo, PageDTO.class);
                pageResult.setList(contactDtos);

                return pageResult;
            }

        return null;
    }

    /**
     * 新增号码
     *
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean insertContact(ContactDTO contactDTO) {
        CustomerTaskContactGroupDO taskContactGroupDO =  taskContactGroupDAO.selectByPrimaryKey(contactDTO.getGroupId());
        if (taskContactGroupDO==null){
            log.warn("不存在的号码组信息 groupId = {},插入号码无效", contactDTO.getGroupId());
            return Boolean.FALSE;
        }
        CustomerTaskUserContactsDO contactDo = BaseBeanUtils.convert(contactDTO, CustomerTaskUserContactsDO.class);
        contactDo.setTaskId(taskContactGroupDO.getTaskId());

        CustomerTaskUserContactsDO queryContactDo = new CustomerTaskUserContactsDO();
        queryContactDo.setContractNo(contactDTO.getContractNo());
        queryContactDo.setUserPhone(EncodesUtils.selfEncrypt(contactDTO.getUserPhone(),appProperties.getSalt()));
        queryContactDo.setTaskId(taskContactGroupDO.getTaskId());
        queryContactDo.setStatus(StatusEnum.NORMAL.getCode().toString());
        if (contactsDAO.selectOne(queryContactDo)!=null){
            throw new BaseBizException("400","已存在用户号,不允许重复添加");
        }
        contactDo.setCreatedBy(contactDTO.getContractNo());
        contactDo.setCreatedAt(new Date());
        contactDo.setStatus(StatusEnum.NORMAL.getCode().toString());
        contactDo.setUserPhone(EncodesUtils.selfEncrypt(contactDTO.getUserPhone(),appProperties.getSalt()));
        contactDo.setSalt(appProperties.getSalt());
        contactDo.setUserSex(SexEnum.getDesc(contactDo.getUserSex()));
        int cnt = contactsDAO.insert(contactDo);

        int groupCnt = taskContactGroupDO.getGroupCnt() + 1;
        taskContactGroupDO.setGroupCnt(groupCnt);
        taskContactGroupDO.setUpdatedAt(new Date());
        taskContactGroupDO.setUpdatedBy(contactDTO.getContractNo());
        int groupUpdateCnt = taskContactGroupDAO.updateByPrimaryKeySelective(taskContactGroupDO);
        return cnt > 0 && groupUpdateCnt>0? Boolean.TRUE : Boolean.FALSE;
    }

    /**
     * 号码修改
     *
     * @return
     */
    public Boolean alterContact(ContactAlterReqDTO contactQueryDto) {
        CustomerTaskUserContactsDO pre_ContactsDo = contactsDAO.selectByPrimaryKey(contactQueryDto.getId());
        BaseBeanUtils.copyNoneNullProperties(contactQueryDto, pre_ContactsDo);
        pre_ContactsDo.setUpdatedBy(contactQueryDto.getContractNo());
        pre_ContactsDo.setUpdatedAt(new Date());
        try {
            pre_ContactsDo.setUserPhone(EncodesUtils.selfEncrypt(contactQueryDto.getUserPhone(),appProperties.getSalt()));
            pre_ContactsDo.setUserSex(SexEnum.getDesc(contactQueryDto.getUserSex()));
            pre_ContactsDo.setUpdatedAt(new Date());
            pre_ContactsDo.setUpdatedBy(contactQueryDto.getContractNo());
        }catch (Exception e){
            log.error("数据修改 加密失败:{}",e);
        }
        int cnt = contactsDAO.updateByPrimaryKeySelective(pre_ContactsDo);
        return cnt > 0 ? Boolean.TRUE : Boolean.FALSE;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean deleteByContactNoAndId(String contactNo,Long id,Long groupId){
        CustomerTaskContactGroupDO taskContactGroupDO =  taskContactGroupDAO.selectByPrimaryKey(groupId);
        if (taskContactGroupDO==null){
            log.warn("该号码id={} ,不存在这个组groupId ={}下",id,groupId);
            return Boolean.FALSE;
        }
        CustomerTaskUserContactsDO contactsDO =  contactsDAO.selectByPrimaryKey(id);
        contactsDO.setUpdatedAt(new Date());
        if (contactsDO!=null&&contactsDO.getCreatedBy() != null && contactsDO.getCreatedBy().equals(contactNo)) {
            contactsDO.setStatus(StatusEnum.BLOCK.getCode().toString());
            int cnt = contactsDAO.updateByPrimaryKeySelective(contactsDO);

            int groupCnt = taskContactGroupDO.getGroupCnt()-1;
            taskContactGroupDO.setGroupCnt(groupCnt);
            taskContactGroupDO.setUpdatedAt(new Date());
            taskContactGroupDO.setUpdatedBy(contactNo);
            int groupUpdateCnt = taskContactGroupDAO.updateByPrimaryKeySelective(taskContactGroupDO);
            return cnt > 0 && groupUpdateCnt>0 ? Boolean.TRUE : Boolean.FALSE;
        }
        log.warn("该组groupID = {},不存在的号码id={}",groupId,id);
        return Boolean.FALSE;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteByContactNoAndGroupId(String contactNo,Long groupId){
        CustomerTaskContactGroupDO taskContactGroupDO =  taskContactGroupDAO.selectByPrimaryKey(groupId);
        if (taskContactGroupDO==null){
            log.warn("该号码id={} ,不存在这个组groupId ={}下",id,groupId);
            return Boolean.FALSE;
        }
        CustomerTaskUserContactsDOExample example =  new CustomerTaskUserContactsDOExample();
        example.createCriteria().andContractNoEqualTo(contactNo).andTaskIdEqualTo(taskContactGroupDO.getTaskId());
        int cnt = contactsDAO.updateByExampleSelective(CustomerTaskUserContactsDO.builder().status(String.valueOf(StatusEnum.BLOCK.getCode())).updatedAt(new Date()).updatedBy(contactNo).build(),example);
        return cnt > 0 ? Boolean.TRUE : Boolean.FALSE;
    }
    /**
     * 解析号码入库
     *
     * @param list
     * @param contractNo
     * @param taskId
     */
    @Transactional(rollbackFor = Exception.class)
    public Integer addContancts(List<Map<String, Object>> list, String contractNo, String taskId, Long fileId) {
        log.info("list size:{},contractNo:{},taskId:{},salt:{}", list.size(), contractNo, taskId,appProperties.getSalt());
        if (CollectionUtils.isNotEmpty(list)) {
            List<CustomerTaskUserContactsDO> contactsDOs = new ArrayList<>();
            list.forEach(map -> {
                CustomerTaskUserContactsDO taskUserContactsDO = BaseBeanUtils.convert(map, CustomerTaskUserContactsDO.class);
                taskUserContactsDO.setTaskId(taskId);
                taskUserContactsDO.setContractNo(contractNo);
                taskUserContactsDO.setSalt(appProperties.getSalt());
                taskUserContactsDO.setStatus(StatusEnum.NORMAL.getCode().toString());
                String phone = taskUserContactsDO.getUserPhone();
                try {
                    taskUserContactsDO.setUserPhone(EncodesUtils.selfEncrypt(phone,appProperties.getSalt()));
                    if (null!=contactsDAO.selectOne(taskUserContactsDO)){
                        list.remove(map);
                        log.warn("批次taskId:{},重复号码记录:{},时间戳:{}",taskId, JSON.toJSONString(map),System.currentTimeMillis());
                        return;
                    }
                } catch (Exception e) {
                    log.info("手机号:{}加密失败:{}", phone, e);
                }
                taskUserContactsDO.setCreatedAt(new Date());
                taskUserContactsDO.setCreatedBy(contractNo);
                contactsDOs.add(taskUserContactsDO);
            });

            Set<CustomerTaskUserContactsDO> contactSet = new TreeSet<>((o1, o2) -> o1.getUserPhone().compareTo(o2.getUserPhone()));
            contactSet.addAll(contactsDOs);
            List<CustomerTaskUserContactsDO> unique = new ArrayList<>(contactSet);
            log.info("去重情况:{}",unique);
            int cnt = contactsDAO.insertBatch(unique);
            log.info("实际批量保存 size: {}", cnt);
            if (cnt > 0) {
                UploadFileLog uploadFileLog = uploadFileLogMapper.selectByPrimaryKey(fileId);
                uploadFileLog.setFileStatus(StatusEnum.BLOCK.getCode().toString());
                uploadFileLog.setUpdateBy("system");
                uploadFileLog.setUpdateAt(new Date());
                uploadFileLogMapper.updateByPrimaryKeySelective(uploadFileLog);
                log.info("上传文件日志更新");
                return cnt;
            }

        }
        return 0;
    }

}


