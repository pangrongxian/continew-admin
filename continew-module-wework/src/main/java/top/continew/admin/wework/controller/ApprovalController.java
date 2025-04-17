/*
 * Copyright (c) 2022-present Charles7c Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package top.continew.admin.wework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.continew.admin.wework.model.req.ApprovalApplyReq;
import top.continew.admin.wework.model.resp.ApprovalDetailResp;
import top.continew.admin.wework.model.resp.ApprovalTemplateResp;
import top.continew.admin.wework.service.ApprovalService;
import top.continew.admin.wework.service.ApprovalTemplateService;
import top.continew.starter.web.model.R;

import java.util.List;

/**
 * 审批管理 Controller
 *
 * @author Charles7c
 * @since 2023/1/1 00:00
 */
@Tag(name = "企业微信-审批管理", description = "企业微信审批流程相关接口")
@Validated
@RestController
@RequestMapping("/wework/approval")
@RequiredArgsConstructor
public class ApprovalController {

    private final ApprovalService approvalService;
    private final ApprovalTemplateService templateService;

    @Operation(summary = "获取审批模板列表", description = "获取企业微信审批模板列表")
    @GetMapping("/templates")
    public R<List<ApprovalTemplateResp>> listTemplates() {
        return R.ok(templateService.listTemplates());
    }

    @Operation(summary = "获取审批模板详情", description = "根据模板ID获取企业微信审批模板详情")
    @GetMapping("/template/{id}")
    public R<ApprovalTemplateResp> getTemplate(@Parameter(description = "模板ID", required = true) @PathVariable String id) {
        return R.ok(templateService.getTemplate(id));
    }
    

    @Operation(summary = "创建审批申请", description = "创建企业微信审批申请")
    @PostMapping("/apply")
    public R<String> createApproval(@Valid @RequestBody ApprovalApplyReq req) {
        return R.ok(approvalService.createApproval(req));
    }

    @Operation(summary = "获取审批详情", description = "根据审批单号获取审批详情")
    @GetMapping("/detail/{spNo}")
    public R<ApprovalDetailResp> getApprovalDetail(@Parameter(description = "审批单号", required = true) @PathVariable String spNo) {
        return R.ok(approvalService.getApprovalDetail(spNo));
    }

    @Operation(summary = "获取审批状态", description = "根据审批单号获取审批状态")
    @GetMapping("/status/{spNo}")
    public R<Integer> getApprovalStatus(@Parameter(description = "审批单号", required = true) @PathVariable String spNo) {
        return R.ok(approvalService.getApprovalStatus(spNo));
    }
}