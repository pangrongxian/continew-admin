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

package top.continew.admin.wework.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 审批状态枚举
 *
 * @author Charles7c
 * @since 2023/1/1 00:00
 */
@Getter
@RequiredArgsConstructor
public enum ApprovalStatusEnum {

    /**
     * 审批中
     */
    PROCESSING(1, "审批中"),

    /**
     * 已通过
     */
    APPROVED(2, "已通过"),

    /**
     * 已驳回
     */
    REJECTED(3, "已驳回"),

    /**
     * 已撤销
     */
    CANCELED(4, "已撤销");

    private final Integer value;
    private final String description;

    /**
     * 根据值获取枚举
     *
     * @param value 值
     * @return 枚举
     */
    public static ApprovalStatusEnum getByValue(Integer value) {
        if (null == value) {
            return null;
        }
        for (ApprovalStatusEnum statusEnum : values()) {
            if (statusEnum.getValue().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }
}