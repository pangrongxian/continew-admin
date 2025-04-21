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

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 审批详情响应信息
 *
 * @author Charles7c
 * @since 2023/1/1 00:00
 */
@Data
@Schema(description = "审批详情响应信息")
public class ApprovalDetailResp implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 审批单号
     */
    @Schema(description = "审批单号", example = "202401010001")
    private String spNo;

    /**
     * 申请标题
     */
    @Schema(description = "申请标题", example = "请假申请")
    private String title;

    /**
     * 审批状态：1-审批中，2-已通过，3-已驳回，4-已撤销
     */
    @Schema(description = "审批状态", example = "1")
    private Integer status;

    /**
     * 申请人信息
     */
    @Schema(description = "申请人信息")
    private Map<String, Object> applier;

    /**
     * 审批流程信息
     */
    @Schema(description = "审批流程信息")
    private List<Map<String, Object>> spRecords;

    /**
     * 表单数据
     */
    @Schema(description = "表单数据")
    private List<Map<String, Object>> formData;
}