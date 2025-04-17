package top.continew.admin.wework.service.impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.continew.admin.wework.mapper.ApprovalTemplateMapper;
import top.continew.admin.wework.model.entity.ApprovalTemplateDO;
import top.continew.admin.wework.model.resp.ApprovalTemplateResp;
import top.continew.admin.wework.service.ApprovalTemplateService;
import top.continew.admin.wework.util.WeWorkClient;
import top.continew.starter.core.exception.BusinessException;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApprovalTemplateServiceImpl extends ServiceImpl<ApprovalTemplateMapper, ApprovalTemplateDO> implements ApprovalTemplateService {

    private final WeWorkClient weWorkClient;

    @Override
    public List<ApprovalTemplateResp> listTemplates() {
        List<ApprovalTemplateDO> templates = list();
        return templates.stream()
            .map(this::convertToResp)
            .collect(Collectors.toList());
    }
    
    @Override
    public ApprovalTemplateResp getTemplate(String templateId) {
        log.info("根据企业微信模板ID获取模板详情: {}", templateId);
        
        // 先从数据库查询，只查询实际存在的字段
        ApprovalTemplateDO template = lambdaQuery()
            .select(ApprovalTemplateDO::getId, 
                   ApprovalTemplateDO::getTemplateId, 
                   ApprovalTemplateDO::getTemplateName,
                   ApprovalTemplateDO::getTemplateContent)
            .eq(ApprovalTemplateDO::getTemplateId, templateId)
            .one();
            
        // 如果数据库中不存在，则从企业微信获取并保存
        if (template == null) {
            log.info("数据库中不存在该模板，从企业微信获取: {}", templateId);
            try {
                // 调用企业微信API获取模板详情
                JSONObject templateJson = weWorkClient.getTemplate(templateId);
                if (templateJson != null) {
                    // 保存模板到数据库
                    template = new ApprovalTemplateDO();
                    template.setTemplateId(templateId);
                    // 获取中文模板名称
                    String templateName = templateJson.getJSONArray("template_names")
                        .stream()
                        .map(item -> {
                            JSONObject nameObj = (JSONObject) item;
                            if ("zh_CN".equals(nameObj.getStr("lang"))) {
                                return nameObj.getStr("text");
                            }
                            return null;
                        })
                        .filter(name -> name != null)
                        .findFirst()
                        .orElse("未命名模板");
                    template.setTemplateName(templateName);
                    
                    // 只保存模板内容部分
                    JSONObject templateContent = templateJson.getJSONObject("template_content");
                    // 将JSONObject转换为字符串保存
                    template.setTemplateContent(templateContent.toString());
                    
                    save(template);
                    log.info("成功从企业微信获取并保存模板: {}", templateId);
                }
            } catch (Exception e) {
                log.error("从企业微信获取模板失败: {}", e.getMessage(), e);
                throw new BusinessException("获取企业微信模板失败: " + e.getMessage());
            }
        }
        
        return convertToResp(template);
    }

    private ApprovalTemplateResp convertToResp(ApprovalTemplateDO template) {
        ApprovalTemplateResp resp = new ApprovalTemplateResp();
        resp.setId(template.getId());
        resp.setTemplateId(template.getTemplateId());
        resp.setTemplateName(template.getTemplateName());

        // 处理模板内容
        if (template.getTemplateContent() != null) {
            try {
                // 将 JSON 字符串转换为 Object
                Object jsonObject = JSONUtil.parse(template.getTemplateContent());
                resp.setTemplateContent(jsonObject);
            } catch (Exception e) {
                log.error("解析模板内容失败: {}", e.getMessage());
                // 如果解析失败，仍然返回原始字符串
                resp.setTemplateContent(template.getTemplateContent());
            }
        } else {
            log.info("模板内容为空");
        }
        
        return resp;
    }
}