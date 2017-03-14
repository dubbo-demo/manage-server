package com.myph.manage.service.billContract.service;

import com.myph.manage.service.billContract.dto.RepayPlanRequestVo;

import java.util.List;

/**
 * @Title: BillRestService
 * @Package: com.myph.manage.service.billContract.service 
 * @company: 麦芽金服
 * @Description: TODO (用一句话描述该文件做什么)
 * @author heyx
 * @date 2017/3/9
 * @version V1.0
 */
public interface BillRestService {
    
    public List<String> restCS(List<RepayPlanRequestVo> successDatas) throws Exception;
}
