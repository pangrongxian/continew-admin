package top.continew.admin.wework.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;  // 添加这行导入
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import top.continew.admin.wework.service.WeWorkUserService;

/**
 * 企业微信配置
 *
 * @author Charles7c
 * @since 2023/1/1 00:00
 */
@Slf4j  // 添加这个注解
@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class WeWorkConfig {

    private final WeWorkUserService weWorkUserService;

    /**
     * 每天凌晨2点执行同步企业微信用户数据
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void syncWeWorkUsers() {
        log.info("开始执行企业微信用户数据同步任务");
        weWorkUserService.syncUsers();
        log.info("企业微信用户数据同步任务执行完成");
    }
}