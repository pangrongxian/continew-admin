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

import cn.hutool.json.JSONObject;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import top.continew.admin.common.model.resp.BaseDetailResp;

import java.io.Serial;

/**
 * 审批模板响应信息
 *
 * @author Charles7c
 * @since 2023/1/1 00:00
 */
@Data
@Schema(description = "审批模板响应信息")
public class ApprovalTemplateResp extends BaseDetailResp {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 企业微信模板ID
     */
    @Schema(description = "企业微信模板ID", example = "3TkZDXxUY9yQMmFhpAAiDMuLvfnia")
    private String templateId;

    /**
     * 模板名称
     */
    @Schema(description = "模板名称", example = "请假申请")
    private String templateName;

    /**
     * 模板内容JSON
     */
    @Schema(description = "模板内容JSON")
    private Object templateContent;
}