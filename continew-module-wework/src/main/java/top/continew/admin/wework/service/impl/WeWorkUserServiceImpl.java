package top.continew.admin.wework.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.continew.admin.wework.mapper.WeWorkUserMapper;
import top.continew.admin.wework.model.entity.WeWorkUserDO;
import top.continew.admin.wework.model.resp.WeWorkUserResp;
import top.continew.admin.wework.service.WeWorkUserService;
import top.continew.admin.wework.util.WeWorkClient;
import jakarta.annotation.PostConstruct;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeWorkUserServiceImpl extends ServiceImpl<WeWorkUserMapper, WeWorkUserDO> implements WeWorkUserService {

    private final WeWorkClient weWorkClient;

    @PostConstruct
    public void initSync() {
        try {
            log.info("系统启动，准备同步企业微信用户数据...");
            // 使用异步方式执行同步，避免阻塞系统启动
            CompletableFuture.runAsync(() -> {
                try {
                    Thread.sleep(5000); // 延迟5秒执行，确保其他组件已初始化完成
                    syncUsers();
                    log.info("系统启动完成首次同步企业微信用户数据");
                } catch (Exception e) {
                    log.error("系统启动同步企业微信用户数据失败", e);
                    // 不抛出异常，避免影响系统启动
                }
            });
        } catch (Exception e) {
            log.error("初始化同步企业微信用户数据失败", e);
            // 不抛出异常，避免影响系统启动
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void syncUsers() {
        try {
            // 从企业微信获取所有用户
            List<WeWorkUserResp> users = weWorkClient.getDepartmentUsers(1L);
            
            if (users.isEmpty()) {
                log.warn("从企业微信获取的用户列表为空，跳过同步操作");
                return;
            }
            
            // 转换为DO对象
            List<WeWorkUserDO> userList = users.stream().map(user -> {
                WeWorkUserDO userDO = new WeWorkUserDO();
                userDO.setUserId(user.getUserId());
                userDO.setName(user.getName());
                userDO.setDepartments(String.join(",", 
                    user.getDepartment() == null ? new String[0] : 
                    java.util.Arrays.stream(user.getDepartment())
                        .map(String::valueOf)
                        .toArray(String[]::new)));
                userDO.setPosition(user.getPosition());
                userDO.setStatus(1);
                return userDO;
            }).collect(Collectors.toList());
    
            // 获取所有新用户ID
            List<String> newUserIds = userList.stream()
                .map(WeWorkUserDO::getUserId)
                .collect(Collectors.toList());
                
            // 查询数据库中已存在的用户
            LambdaQueryWrapper<WeWorkUserDO> existsWrapper = new LambdaQueryWrapper<>();
            existsWrapper.in(WeWorkUserDO::getUserId, newUserIds);
            List<WeWorkUserDO> existingUsers = this.list(existsWrapper);
            
            // 创建已存在用户ID的映射，用于快速查找
            Map<String, WeWorkUserDO> existingUserMap = existingUsers.stream()
                .collect(Collectors.toMap(WeWorkUserDO::getUserId, user -> user));
            
            // 分离需要新增和需要更新的用户
            List<WeWorkUserDO> usersToInsert = userList.stream()
                .filter(user -> !existingUserMap.containsKey(user.getUserId()))
                .collect(Collectors.toList());
                
            List<WeWorkUserDO> usersToUpdate = userList.stream()
                .filter(user -> existingUserMap.containsKey(user.getUserId()))
                .map(user -> {
                    // 保留原有ID
                    WeWorkUserDO existingUser = existingUserMap.get(user.getUserId());
                    user.setId(existingUser.getId());
                    return user;
                })
                .collect(Collectors.toList());
            
            // 删除不在新用户列表中的旧用户
            LambdaQueryWrapper<WeWorkUserDO> deleteWrapper = new LambdaQueryWrapper<>();
            deleteWrapper.notIn(WeWorkUserDO::getUserId, newUserIds);
            this.remove(deleteWrapper);
            
            // 分批处理插入和更新操作
            if (!usersToInsert.isEmpty()) {
                this.saveBatch(usersToInsert);
            }
            
            if (!usersToUpdate.isEmpty()) {
                this.updateBatchById(usersToUpdate);
            }
        } catch (Exception e) {
            log.error("同步企业微信用户数据失败", e);
            throw new RuntimeException("同步企业微信用户数据失败", e);
        }
    }

    @Override
    public List<WeWorkUserResp> listDepartmentUsers(Long departmentId) {
        // 从数据库查询用户
        LambdaQueryWrapper<WeWorkUserDO> wrapper = new LambdaQueryWrapper<>();
        if (departmentId != 1) {
            wrapper.like(WeWorkUserDO::getDepartments, departmentId);
        }
        
        return this.list(wrapper).stream().map(user -> {
            WeWorkUserResp resp = new WeWorkUserResp();
            resp.setUserId(user.getUserId());
            resp.setName(user.getName());
            resp.setPosition(user.getPosition());
            resp.setDepartment(user.getDepartments() == null ? new Long[0] :
                java.util.Arrays.stream(user.getDepartments().split(","))
                    .map(Long::valueOf)
                    .toArray(Long[]::new));
            return resp;
        }).collect(Collectors.toList());
    }
}