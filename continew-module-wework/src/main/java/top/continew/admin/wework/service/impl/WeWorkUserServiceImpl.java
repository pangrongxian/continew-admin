package top.continew.admin.wework.service.impl;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.continew.admin.system.model.entity.user.UserDO;
import top.continew.admin.system.service.UserService;
import top.continew.admin.wework.service.WeWorkUserService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeWorkUserServiceImpl implements WeWorkUserService {

    private final UserService userService;

    @Override
    public String getWeWorkUserId(Long sysUserId) {
        UserDO user = userService.getById(sysUserId);
        if (user != null) {
            // 如果用户有设置企业微信ID - 则返回企业微信ID
            // String weWorkId = user.getWeWorkUserId();
            String weWorkId = "123456";
            if (StrUtil.isNotEmpty(weWorkId)) {
                return weWorkId;
            }
            // 否则使用默认规则：ww_ + 系统用户ID
            return "ww_" + sysUserId;
        }
        return null;
    }

    @Override
    public List<String> getWeWorkUserIds(List<Long> sysUserIds) {
        return sysUserIds.stream()
            .map(this::getWeWorkUserId)
            .collect(Collectors.toList());
    }
}