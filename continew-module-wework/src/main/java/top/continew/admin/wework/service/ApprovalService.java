package top.continew.admin.wework.service;

import top.continew.admin.wework.model.req.ApprovalApplyReq;
import top.continew.admin.wework.model.resp.ApprovalDetailResp;

/**
 * 审批服务接口
 *
 * @author Charles7c
 * @since 2023/1/1 00:00
 */
public interface ApprovalService {

    /**
     * 创建审批申请
     *
     * @param req 申请参数
     * @return 审批单号
     */
    String createApproval(ApprovalApplyReq req);

    /**
     * 获取审批详情
     *
     * @param spNo 审批单号
     * @return 审批详情
     */
    ApprovalDetailResp getApprovalDetail(String spNo);

    /**
     * 获取审批状态
     *
     * @param spNo 审批单号
     * @return 审批状态
     */
    Integer getApprovalStatus(String spNo);

    /**
     * 处理审批状态变更
     *
     * @param spNo   审批单号
     * @param status 审批状态
     */
    void handleStatusChange(String spNo, Integer status);

}