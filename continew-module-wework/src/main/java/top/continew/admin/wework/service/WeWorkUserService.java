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
import top.continew.admin.wework.model.entity.WeWorkUserDO;
import top.continew.admin.wework.model.resp.WeWorkUserResp;

import java.util.List;

/**
 * 企业微信用户服务接口
 *
 * @author Charles7c
 * @since 2023/1/1 00:00
 */
public interface WeWorkUserService extends IService<WeWorkUserDO> {

    /**
     * 同步企业微信用户数据
     */
    void syncUsers();

    /**
     * 获取部门下的用户列表
     *
     * @param departmentId 部门ID
     * @return 用户列表
     */
    List<WeWorkUserResp> listDepartmentUsers(Long departmentId);
}