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

package top.continew.admin.wework.model.entity;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.continew.admin.common.model.entity.BaseDO;

/**
 * 审批模板
 *
 * @author Charles7c
 * @since 2023/1/1 00:00
 */
@Data
@TableName("ww_approval_template")
@EqualsAndHashCode(callSuper = true)
public class ApprovalTemplateDO extends BaseDO {

    /**
     * 企业微信模板ID
     */
    @TableField(fill = FieldFill.INSERT)
    private String templateId;

    /**
     * 模板名称
     */
    @TableField(fill = FieldFill.INSERT)
    private String templateName;

    /**
     * 模板内容JSON
     */
    @TableField(value = "template_content", fill = FieldFill.INSERT)
    private String templateContent;
}