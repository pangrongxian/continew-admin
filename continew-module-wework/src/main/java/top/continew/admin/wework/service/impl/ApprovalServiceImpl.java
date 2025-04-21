package top.continew.admin.wework.service.impl;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.continew.starter.core.exception.BusinessException;
import top.continew.admin.wework.mapper.ApprovalApplyMapper;
import top.continew.admin.wework.model.entity.ApprovalApplyDO;
import top.continew.admin.wework.model.req.ApprovalApplyReq;
import top.continew.admin.wework.model.resp.ApprovalDetailResp;
import top.continew.admin.wework.service.ApprovalService;
import top.continew.admin.wework.util.WeWorkClient;
import cn.hutool.json.JSONUtil;

/**
 * 审批服务实现
 *
 * @author Charles7c
 * @since 2023/1/1 00:00
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ApprovalServiceImpl implements ApprovalService {

    private final WeWorkClient weWorkClient;
    private final ApprovalApplyMapper applyMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createApproval(ApprovalApplyReq req) {
        try {
            // 调用企业微信API提交审批申请
            String spNo = weWorkClient.applyApproval(req);
            
            // 保存审批申请记录到数据库
            ApprovalApplyDO apply = new ApprovalApplyDO();
            apply.setSpNo(spNo);
            apply.setTitle(req.getTitle());
            apply.setTemplateId(req.getTemplateId());
            apply.setCreatorUserid(req.getCreatorUserid());
            apply.setCreatorName(req.getCreatorName());
            apply.setStatus(1);
            apply.setApplyData(JSONUtil.toJsonStr(req.getApplyData()));
            apply.setProcess(JSONUtil.toJsonStr(req.getProcess()));
            applyMapper.insert(apply);
            
            return spNo;
        } catch (Exception e) {
            log.error("创建审批申请失败", e);
            throw new BusinessException("创建审批申请失败: " + e.getMessage());
        }
    }

    @Override
    public ApprovalDetailResp getApprovalDetail(String spNo) {
        JSONObject detail = weWorkClient.getApprovalDetail(spNo);
        ApprovalDetailResp resp = new ApprovalDetailResp();
        // 转换审批详情
        resp.setSpNo(spNo);
        resp.setTitle(detail.getStr("title"));
        resp.setStatus(detail.getInt("sp_status"));
        // ... 设置其他字段
        return resp;
    }

    @Override
    public Integer getApprovalStatus(String spNo) {
        ApprovalApplyDO apply = applyMapper.selectOne(
            new LambdaQueryWrapper<ApprovalApplyDO>()
                .eq(ApprovalApplyDO::getSpNo, spNo)
        );
        return apply != null ? apply.getStatus() : null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handleStatusChange(String spNo, Integer status) {
        // 更新本地审批状态
        ApprovalApplyDO apply = applyMapper.selectOne(
            new LambdaQueryWrapper<ApprovalApplyDO>()
                .eq(ApprovalApplyDO::getSpNo, spNo)
        );
        
        if (apply != null) {
            apply.setStatus(status);
            applyMapper.updateById(apply);
            log.info("审批状态已更新: spNo={}, status={}", spNo, status);
        } else {
            log.warn("未找到对应的审批申请记录: spNo={}", spNo);
        }
    }
}