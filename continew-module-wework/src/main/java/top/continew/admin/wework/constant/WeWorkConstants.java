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

package top.continew.admin.wework.constant;

/**
 * 企业微信常量
 *
 * @author Charles7c
 * @since 2023/1/1 00:00
 */
public interface WeWorkConstants {

    /**
     * 企业微信API基础URL
     */
    String API_BASE_URL = "https://qyapi.weixin.qq.com";

    /**
     * 获取访问令牌URL
     */
    String GET_TOKEN_URL = API_BASE_URL + "/cgi-bin/gettoken?corpid=%s&corpsecret=%s";

    /**
     * 获取审批模板URL
     */
    String GET_TEMPLATE_URL = API_BASE_URL + "/cgi-bin/oa/gettemplatedetail?access_token=%s";

    /**
     * 提交审批申请URL
     */
    String APPLY_APPROVAL_URL = API_BASE_URL + "/cgi-bin/oa/applyevent?access_token=%s";

    /**
     * 获取审批详情URL
     */
    String GET_APPROVAL_DETAIL_URL = API_BASE_URL + "/cgi-bin/oa/getapprovaldetail?access_token=%s";

    /**
     * 审批状态 - 审批中
     */
    int APPROVAL_STATUS_PROCESSING = 1;

    /**
     * 审批状态 - 已通过
     */
    int APPROVAL_STATUS_APPROVED = 2;

    /**
     * 审批状态 - 已驳回
     */
    int APPROVAL_STATUS_REJECTED = 3;

    /**
     * 审批状态 - 已撤销
     */
    int APPROVAL_STATUS_CANCELED = 4;

    /**
     * 缓存前缀 - 访问令牌
     */
    String CACHE_PREFIX_ACCESS_TOKEN = "wework:access_token";

    /**
     * 获取审批模板列表URL
     */
    String GET_TEMPLATE_LIST_URL = API_BASE_URL + "/cgi-bin/oa/gettemplatelistbygroup?access_token=%s";
}