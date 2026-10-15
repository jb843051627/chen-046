package com.fc.v2.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fc.v2.mapper.auto.THvacCleanLogMapper;
import com.fc.v2.model.auto.THvacCleanLog;
import com.fc.v2.service.ITHvacCleanLogService;

/**
 * 集中空调清洗消毒作业单 Service业务层处理（state-machine 形状：单据流转）
 *
 * @author fuce
 * @date 2026-09-14
 */
@Service
public class THvacCleanLogServiceImpl implements ITHvacCleanLogService {

    private static final int MAX_STAGE = 3;
    private static final int STATUS_ACTIVE = 1;
    private static final int STATUS_TERMINAL = 2;

    @javax.annotation.Resource
    private THvacCleanLogMapper hvacCleanLogMapper;

    @Override
    public THvacCleanLog selectTHvacCleanLogById(Long id) {
        return this.hvacCleanLogMapper.selectById(id);
    }

    @Override
    public List<THvacCleanLog> selectTHvacCleanLogList(QueryWrapper<THvacCleanLog> queryWrapper) {
        return this.hvacCleanLogMapper.selectList(queryWrapper);
    }

    @Override
    public THvacCleanLog advance(Long id, String remark) {
        THvacCleanLog r = this.hvacCleanLogMapper.selectById(id);
        if (r == null) {
            return null;
        }
        int st = r.getStage() == null ? 0 : r.getStage();
        r.setStage(Math.min(st + 2, MAX_STAGE));
        r.setStatus(STATUS_ACTIVE);
        r.setLastAction(remark);
        this.hvacCleanLogMapper.updateById(r);
        return r;
    }

    @Override
    public THvacCleanLog rollback(Long id, String remark) {
        THvacCleanLog r = this.hvacCleanLogMapper.selectById(id);
        if (r == null) {
            return null;
        }
        r.setStage(0);
        r.setStatus(STATUS_ACTIVE);
        r.setLastAction(remark);
        this.hvacCleanLogMapper.updateById(r);
        return r;
    }

    @Override
    public boolean updateContent(Long id, String remark) {
        THvacCleanLog r = this.hvacCleanLogMapper.selectById(id);
        if (r == null) {
            return false;
        }
        r.setContent(remark);
        return this.hvacCleanLogMapper.updateById(r) > 0;
    }

    @Override
    public boolean remove(Long id) {
        THvacCleanLog r = this.hvacCleanLogMapper.selectById(id);
        if (r == null) {
            return false;
        }
        return this.hvacCleanLogMapper.deleteById(id) > 0;
    }

}
