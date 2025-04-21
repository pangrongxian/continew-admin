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

package top.continew.admin.wework.model.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 审批申请请求参数
 *
 * @author Charles7c
 * @since 2023/1/1 00:00
 */
@Data
@Schema(description = "审批申请请求参数")
public class ApprovalApplyReq implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 模板ID
     */
    @NotBlank(message = "模板ID不能为空")
    @Schema(description = "模板ID", example = "3TkZDXxUY9yQMmFhpAAiDMuLvfnia", required = true)
    private String templateId;

    /**
     * 申请标题
     */
    @Schema(description = "申请标题", example = "用车申请")
    private String title;

    /**
     * 申请人userid
     */
    @Schema(description = "申请人userid")
    private String creatorUserid;

    /**
     * 申请人姓名
     */
    @Schema(description = "申请人姓名")
    private String creatorName;

    /**
     * 审批流程信息，当use_template_approver为0时生效
     */
    @Schema(description = "审批流程信息")
    private ProcessInfo process;

    /**
     * 部门id
     */
    @Schema(description = "提单者提单部门id")
    private Integer chooseDepartment;

    /**
     * 表单数据
     */
    @NotNull(message = "表单数据不能为空")
    @Schema(description = "表单数据", required = true)
    private ApplyData applyData;

    /**
     * 摘要信息
     */
    @Schema(description = "摘要信息")
    private List<SummaryItem> summaryList;

    @Data
    @Schema(description = "审批流程信息")
    public static class ProcessInfo {
        private List<NodeInfo> node_list;
    }

    @Data
    @Schema(description = "审批节点信息")
    public static class NodeInfo {
        /**
         * 节点类型：1-审批节点
         */
        private Integer type;
        
        /**
         * 审批方式：1-依次审批，2-会签
         */
        private Integer apv_rel;
        
        /**
         * 审批人userid列表
         */
        private List<String> userid;
    }

    @Data
    @Schema(description = "表单数据")
    public static class ApplyData {
        private List<FormContent> contents;
    }

    @Data
    @Schema(description = "表单内容")
    public static class FormContent {
        private String control;
        private String id;
        private Map<String, Object> value;
    }

    @Data
    @Schema(description = "摘要项")
    public static class SummaryItem {
        private List<SummaryInfo> summaryInfo;
    }

    @Data
    @Schema(description = "摘要信息")
    public static class SummaryInfo {
        private String text;
        private String lang = "zh_CN";
    }
}