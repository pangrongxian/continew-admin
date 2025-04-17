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

package top.continew.admin.wework.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.hutool.core.util.XmlUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import top.continew.admin.wework.config.WeWorkProperties;
import top.continew.admin.wework.service.ApprovalService;
import top.continew.admin.wework.util.WXBizMsgCrypt;

import java.util.Map;

/**
 * 企业微信回调 Controller
 *
 * @author Charles7c
 * @since 2023/1/1 00:00
 */
@Slf4j
@Tag(name = "企业微信-回调接口", description = "企业微信事件回调接口")
@SaIgnore
@RestController
@RequestMapping("/wework/callback")
@RequiredArgsConstructor
public class WeWorkCallbackController {

    private final WeWorkProperties properties;
    private final ApprovalService approvalService;

    @Operation(summary = "验证回调URL", description = "用于企业微信配置回调URL时的验证")
    @GetMapping("/approval")
    public String verifyUrl(@Parameter(description = "消息签名", required = true) @RequestParam("msg_signature") String msgSignature,
                           @Parameter(description = "时间戳", required = true) @RequestParam("timestamp") String timestamp,
                           @Parameter(description = "随机数", required = true) @RequestParam("nonce") String nonce,
                           @Parameter(description = "回显字符串", required = true) @RequestParam("echostr") String echostr) {
        try {
            WXBizMsgCrypt wxcpt = new WXBizMsgCrypt(properties.getToken(), 
                                                   properties.getEncodingAesKey(), 
                                                   properties.getCorpId());
            return wxcpt.verifyUrl(msgSignature, timestamp, nonce, echostr);
        } catch (Exception e) {
            log.error("验证回调URL失败", e);
            return "fail";
        }
    }

    @Operation(summary = "接收审批状态变更回调", description = "接收企业微信审批状态变更事件")
    @PostMapping("/approval")
    public String handleApprovalCallback(@Parameter(description = "请求体", required = true) @RequestBody String requestBody,
                                        @Parameter(description = "消息签名", required = true) @RequestParam("msg_signature") String msgSignature,
                                        @Parameter(description = "时间戳", required = true) @RequestParam("timestamp") String timestamp,
                                        @Parameter(description = "随机数", required = true) @RequestParam("nonce") String nonce) {
        try {
            // 解密回调消息
            WXBizMsgCrypt wxcpt = new WXBizMsgCrypt(properties.getToken(), 
                                                   properties.getEncodingAesKey(), 
                                                   properties.getCorpId());
            String decryptMsg = wxcpt.decryptMsg(msgSignature, timestamp, nonce, requestBody);
            
            // 解析XML消息
            Map<String, Object> xmlMap = XmlUtil.xmlToMap(decryptMsg);
            String eventType = (String) xmlMap.get("Event");
            
            // 处理审批状态变更事件
            if ("sys_approval_change".equals(eventType)) {
                String spNo = (String) xmlMap.get("SpNo");
                String status = (String) xmlMap.get("ApprovalStatus");
                
                // 处理审批状态变更
                approvalService.handleStatusChange(spNo, Integer.parseInt(status));
                
                log.info("审批状态变更: spNo={}, status={}", spNo, status);
            }
            
            // 返回成功
            return "success";
        } catch (Exception e) {
            log.error("处理审批状态变更回调失败", e);
            return "fail";
        }
    }
}