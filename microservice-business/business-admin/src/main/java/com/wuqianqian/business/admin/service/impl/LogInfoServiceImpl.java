package com.wuqianqian.business.admin.service.impl;

import com.querydsl.core.types.Predicate;
import com.wuqianqian.business.admin.domain.LogInfo;
import com.wuqianqian.business.admin.domain.QLogInfo;
import com.wuqianqian.business.admin.repository.LogInfoRepository;
import com.wuqianqian.business.admin.service.LogInfoService;
import com.wuqianqian.business.commons.utils.PageUtils;
import com.wuqianqian.springcloud.StringHelper;
import com.wuqianqian.springcloud.page.PageBean;
import com.wuqianqian.springcloud.page.PageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 日志信息接口实现类
 * @author liupeqing
 * @date 2018/9/28 10:47
 */
@Service
public class LogInfoServiceImpl implements LogInfoService {

    @Autowired
    private LogInfoRepository logInfoRepository;

    @Override
    @Transactional
    public void saveOrUpdate(LogInfo logInfo) {

        if (null == logInfo) return;

        logInfoRepository.save(logInfo);
    }

    @Override
    public PageBean<LogInfo> findAll(PageParams pageParams, LogInfo logInfo) {
        QLogInfo qLogInfo = QLogInfo.logInfo;
        // 用户名查询条件
        Predicate qServiceIdPredicate = null;
        if (null != logInfo) {
            if (StringHelper.isNotBlank(logInfo.getServiceId())) {
                qServiceIdPredicate = qLogInfo.serviceId.like("%" + logInfo.getServiceId().trim()
                        + "%");
            }
        }

        Predicate predicate = qLogInfo.statu.eq(0).and(qServiceIdPredicate);

        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "id"));
        PageRequest pageRequest = PageUtils.of(pageParams, sort);
        Page<LogInfo> pageList = logInfoRepository.findAll(predicate, pageRequest);

        List<LogInfo> cList = pageList.getContent();
        if (null != cList && cList.size() > 0) {
            cList.forEach(log -> {
                log.setIdView(log.getId() + "");
            });
        }
        return PageUtils.of(pageList);
    }

    @Override
    public boolean delById(Long id) {
        if (null == id) return false;
        this.logInfoRepository.delete(id);
        return Boolean.TRUE;
    }
}
