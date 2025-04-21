package top.continew.admin.wework.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.continew.admin.common.model.entity.BaseDO;

/**
 * 审批申请
 *
 * @author Charles7c
 * @since 2023/1/1 00:00
 */
@Data
@TableName("ww_approval_apply")
@EqualsAndHashCode(callSuper = true)
public class ApprovalApplyDO extends BaseDO {

    /**
     * 审批单号
     */
    private String spNo;

    /**
     * 模板ID
     */
    private String templateId;

    /**
     * 申请标题
     */
    // @TableField("title")
    private String title;

    /**
     * 申请人ID
     */
    private String creatorUserid;

 /**
     创建人名字
     */
    private String creatorName;
    

    /**
     * 审批状态：1-审批中，2-已通过，3-已驳回，4-已撤销
     */
    private Integer status;

    /**
     * 表单数据JSON
     */
    @TableField("apply_data")
    private String applyData;


    @TableField("process")
    private String process;
}