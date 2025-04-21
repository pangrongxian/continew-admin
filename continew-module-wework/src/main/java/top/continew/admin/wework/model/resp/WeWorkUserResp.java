package top.continew.admin.wework.model.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "企业微信用户信息响应")
public class WeWorkUserResp {

    @Schema(description = "用户ID", example = "zhangsan")
    private String userId;

    @Schema(description = "用户名称", example = "张三")
    private String name;

    @Schema(description = "部门ID列表")
    private Long[] department;

    @Schema(description = "职位信息", example = "产品经理")
    private String position;

    @Schema(description = "手机号", example = "13800138000")
    private String mobile;

    @Schema(description = "邮箱", example = "zhangsan@example.com")
    private String email;

    @Schema(description = "状态：1-启用，0-禁用", example = "1")
    private Integer status;
}