package top.continew.admin.wework.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.continew.admin.common.model.entity.BaseDO;

@Data
@TableName("ww_user")
@EqualsAndHashCode(callSuper = true)
public class WeWorkUserDO extends BaseDO {

    /**
     * 企业微信用户ID
     */
    private String userId;

    /**
     * 用户名称
     */
    private String name;

    /**
     * 部门ID，多个部门用逗号分隔
     */
    private String departments;

    /**
     * 职位信息
     */
    private String position;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 状态：1-启用，0-禁用
     */
    private Integer status;
}