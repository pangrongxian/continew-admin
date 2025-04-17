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

package top.continew.admin.wework.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.continew.admin.wework.model.entity.ApprovalTemplateDO;
import top.continew.admin.wework.model.resp.ApprovalTemplateResp;

import java.util.List;

/**
 * 审批模板服务接口
 *
 * @author Charles7c
 * @since 2023/1/1 00:00
 */
public interface ApprovalTemplateService extends IService<ApprovalTemplateDO> {

    /**
     * 获取模板列表
     *
     * @return 模板列表
     */
    List<ApprovalTemplateResp> listTemplates();

    /**
     * 获取模板详情
     *
     * @param id 模板ID
     * @return 模板详情
     */
    ApprovalTemplateResp getTemplate(String id);

}