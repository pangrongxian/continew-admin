package top.continew.admin.wework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.continew.admin.wework.model.resp.WeWorkUserResp;
import top.continew.admin.wework.service.WeWorkUserService;
import top.continew.starter.web.model.R;

import java.util.List;

@Tag(name = "企业微信-用户管理", description = "企业微信用户相关接口")
@RestController
@RequestMapping("/wework/user")
@RequiredArgsConstructor
public class WeWorkUserController {

    private final WeWorkUserService userService;

    @Operation(summary = "同步企业微信用户", description = "同步企业微信用户数据到本地")
    @PostMapping("/sync")
    public R<Void> syncUsers() {
        userService.syncUsers();
        return R.ok();
    }

    @Operation(summary = "获取部门成员列表", description = "获取企业微信部门成员列表")
    @GetMapping("/list")
    public R<List<WeWorkUserResp>> listDepartmentUsers(
        @Parameter(description = "部门ID，默认为1获取所有员工") 
        @RequestParam(defaultValue = "1") Long departmentId) {
        return R.ok(userService.listDepartmentUsers(departmentId));
    }
}