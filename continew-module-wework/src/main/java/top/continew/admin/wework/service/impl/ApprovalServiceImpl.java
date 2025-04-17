package top.continew.admin.wework.service.impl;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.continew.admin.common.context.UserContextHolder;
import top.continew.admin.wework.mapper.ApprovalApplyMapper;
import top.continew.admin.wework.model.entity.ApprovalApplyDO;
import top.continew.admin.wework.model.req.ApprovalApplyReq;
import top.continew.admin.wework.model.resp.ApprovalApplyResp;
import top.continew.admin.wework.model.resp.ApprovalDetailResp;
import top.continew.admin.wework.service.ApprovalService;
import top.continew.admin.wework.service.WeWorkUserService;
import top.continew.admin.wework.util.WeWorkClient;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApprovalServiceImpl implements ApprovalService {

    private final WeWorkClient weWorkClient;
    private final WeWorkUserService weWorkUserService;
    private final ApprovalApplyMapper applyMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createApproval(ApprovalApplyReq req) {
        // 获取当前用户
        Long userId = UserContextHolder.getUserId();
        String username = UserContextHolder.getUsername();
        
        // 获取审批人的企业微信用户ID
        List<String> approverUserIds = weWorkUserService.getWeWorkUserIds(req.getApprovers());
        
        // 提交审批申请
        String spNo = weWorkClient.applyApproval(req, approverUserIds);
        
        // 保存审批记录
        // 保存审批记录时使用上下文中的用户名
        ApprovalApplyDO apply = new ApprovalApplyDO();
        apply.setSpNo(spNo);
        apply.setTemplateId(req.getTemplateId());
        apply.setTitle(req.getTitle());
        apply.setCreatorId(userId);
        apply.setCreatorName(username);  // 使用上下文中的用户名
        apply.setStatus(0); // 待审批
        apply.setFormData(req.getFormData().toString());
        
        applyMapper.insert(apply);
        return spNo;
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
        ApprovalApplyDO apply = new ApprovalApplyDO();
        apply.setStatus(status);
        applyMapper.update(apply, 
            new LambdaQueryWrapper<ApprovalApplyDO>()
                .eq(ApprovalApplyDO::getSpNo, spNo)
        );
    }

    private ApprovalApplyResp convertToResp(ApprovalApplyDO apply) {
        ApprovalApplyResp resp = new ApprovalApplyResp();
        resp.setId(apply.getId());
        resp.setSpNo(apply.getSpNo());
        resp.setTitle(apply.getTitle());
        resp.setStatus(apply.getStatus());
        resp.setCreatorName(apply.getCreatorName());
        resp.setCreateTime(apply.getCreateTime());
        return resp;
    }
}