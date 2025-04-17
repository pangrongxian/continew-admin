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

package top.continew.admin.wework.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 企业微信配置属性
 *
 * @author Charles7c
 * @since 2023/1/1 00:00
 */
@Data
@Component
@ConfigurationProperties(prefix = "wework")
public class WeWorkProperties {

    /**
     * 企业ID
     */
    private String corpId;

    /**
     * 企业微信应用ID
     */
    private Integer agentId;

    /**
     * 应用密钥
     */
    private String secret;

    /**
     * 回调token
     */
    private String token;

    /**
     * 回调消息加密密钥
     */
    private String encodingAesKey;

    /**
     * 接收回调的域名
     */
    private String callbackDomain;
}