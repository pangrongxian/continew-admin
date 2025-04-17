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

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.continew.admin.common.model.entity.BaseDO;

import java.time.LocalDateTime;

/**
 * 审批申请
 *
 * @author Charles7c
 * @since 2023/1/1 00:00
 */
@Data
@TableName("ww_approval_apply")
@EqualsAndHashCode(callSuper = true)
public class ApprovalApplyDO extends BaseDO {

    /**
     * 审批单号
     */
    private String spNo;

    /**
     * 模板ID
     */
    private String templateId;

    /**
     * 申请标题
     */
    private String title;

    /**
     * 申请人ID
     */
    private Long creatorId;

    /**
     * 申请人姓名
     */
    private String creatorName;

    /**
     * 申请时间
     */
    private LocalDateTime applyTime;

    /**
     * 审批状态
     */
    private Integer status;

    /**
     * 表单数据JSON
     */
    @TableField("form_data")
    private String formData;
}