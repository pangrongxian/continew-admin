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

package top.continew.admin.wework.model.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 审批申请查询条件
 *
 * @author Charles7c
 * @since 2023/1/1 00:00
 */
@Data
@Schema(description = "审批申请查询条件")
public class ApprovalApplyQuery implements Serializable {

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
     * 审批状态
     */
    @Schema(description = "审批状态", example = "1")
    private Integer status;
}