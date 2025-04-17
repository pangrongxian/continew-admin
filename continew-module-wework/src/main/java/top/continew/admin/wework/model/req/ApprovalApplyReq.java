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
import jakarta.validation.constraints.NotEmpty;
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
    @Schema(description = "模板ID", example = "3TkZDXxUY9yQMmFhpAAiDMuLvfnia")
    private String templateId;

    /**
     * 申请标题
     */
    @NotBlank(message = "申请标题不能为空")
    @Schema(description = "申请标题", example = "请假申请")
    private String title;

    /**
     * 审批人ID列表
     */
    @NotEmpty(message = "审批人不能为空")
    @Schema(description = "审批人ID列表")
    private List<Long> approvers;

    /**
     * 表单数据
     */
    @NotNull(message = "表单数据不能为空")
    @Schema(description = "表单数据")
    private Map<String, Object> formData;
}