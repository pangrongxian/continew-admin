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

package top.continew.admin.wework.model.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import top.continew.admin.common.model.resp.BaseDetailResp;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * 审批申请响应信息
 *
 * @author Charles7c
 * @since 2023/1/1 00:00
 */
@Data
@Schema(description = "审批申请响应信息")
public class ApprovalApplyResp extends BaseDetailResp {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 审批单号
     */
    @Schema(description = "审批单号", example = "202401010001")
    private String spNo;

    /**
     * 模板ID
     */
    @Schema(description = "模板ID", example = "3TkZDXxUY9yQMmFhpAAiDMuLvfnia")
    private String templateId;

    /**
     * 模板名称
     */
    @Schema(description = "模板名称", example = "请假申请")
    private String templateName;

    /**
     * 申请标题
     */
    @Schema(description = "申请标题", example = "请假申请")
    private String title;

    /**
     * 申请人ID
     */
    @Schema(description = "申请人ID", example = "1")
    private Long creatorId;

    /**
     * 申请人姓名
     */
    @Schema(description = "申请人姓名", example = "张三")
    private String creatorName;

    /**
     * 申请时间
     */
    @Schema(description = "申请时间")
    private LocalDateTime applyTime;

    /**
     * 审批状态
     */
    @Schema(description = "审批状态", example = "1")
    private Integer status;

    /**
     * 审批状态描述
     */
    @Schema(description = "审批状态描述", example = "审批中")
    private String statusDesc;

    /**
     * 表单数据JSON
     */
    @Schema(description = "表单数据JSON")
    private String formData;
}