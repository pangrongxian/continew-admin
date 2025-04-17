package top.continew.admin.wework.util;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.CacheManager;
import com.alicp.jetcache.RefreshPolicy;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.template.QuickConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.time.Duration;
import top.continew.starter.core.exception.BusinessException;
import top.continew.admin.wework.config.WeWorkProperties;
import top.continew.admin.wework.constant.WeWorkConstants;
import top.continew.admin.wework.model.req.ApprovalApplyReq;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 企业微信客户端
 *
 * @author Charles7c
 * @since 2023/1/1 00:00
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WeWorkClient {

    private final WeWorkProperties properties;
    private final CacheManager cacheManager;
    private Cache<String, String> tokenCache;

    /**
     * 初始化缓存
     */
    public void init() {
        QuickConfig qc = QuickConfig.newBuilder(WeWorkConstants.CACHE_PREFIX_ACCESS_TOKEN)
            .expire(Duration.ofSeconds(7200))
            .cacheType(CacheType.BOTH)
            .localLimit(10)
            .syncLocal(true)
            .refreshPolicy(RefreshPolicy.newPolicy(1800, TimeUnit.SECONDS))  // 修改这行，使用时间和时间单位
            .build();
        tokenCache = cacheManager.getOrCreateCache(qc);
    }

    /**
     * 获取访问令牌
     *
     * @return 访问令牌
     */
    /**
     * 获取访问令牌
     *
     * @return 访问令牌
     */
    public String getAccessToken() {
        if (tokenCache == null) {
            init();
        }
        
        try {
            return tokenCache.computeIfAbsent("token", key -> {


                log.info("请求企业微信获取访问令牌，URL: {}", WeWorkConstants.GET_TOKEN_URL);
                log.info("请求企业微信获取访问令牌，URL: {}", properties.getCorpId());
                log.info("请求企业微信获取访问令牌，URL: {}", properties.getSecret());

                String url = String.format(WeWorkConstants.GET_TOKEN_URL, properties.getCorpId(), properties.getSecret());
                log.info("请求企业微信获取访问令牌，URL: {}", url);
                
                HttpResponse response = HttpRequest.get(url).execute();
                String responseBody = response.body();
                log.info("企业微信获取访问令牌响应: {}", responseBody);
                
                JSONObject result = JSONUtil.parseObj(responseBody);
                int errcode = result.getInt("errcode", -1);
                if (errcode != 0) {
                    String errmsg = result.getStr("errmsg", "未知错误");
                    log.error("获取企业微信访问令牌失败: errcode={}, errmsg={}", errcode, errmsg);
                    throw new BusinessException("获取企业微信访问令牌失败: " + errmsg);
                }
                
                return result.getStr("access_token");
            });
        } catch (Exception e) {
            log.error("获取企业微信访问令牌异常", e);
            throw new BusinessException("获取企业微信访问令牌失败: " + e.getMessage());
        }
    }

    /**
     * 获取审批模板
     *
     * @param templateId 模板ID
     * @return 模板详情
     */
    /**
     * 获取审批模板
     *
     * @param templateId 模板ID
     * @return 模板详情
     */
    public JSONObject getTemplate(String templateId) {
        log.info("请求企业微信获取审批模板，URL: {}", WeWorkConstants.GET_TEMPLATE_URL);
        log.info("请求企业微信获取审批模板，AccessToken: {}", getAccessToken());
    
        String url = String.format(WeWorkConstants.GET_TEMPLATE_URL, getAccessToken());
        Map<String, Object> params = new HashMap<>(1);
        params.put("template_id", templateId);
        String requestBody = JSONUtil.toJsonStr(params);
        
        log.info("请求企业微信获取审批模板，URL: {}, 请求参数: {}", url, requestBody);
        
        try {
            HttpResponse response = HttpRequest.post(url)
                .body(requestBody)
                .execute();
                
            String responseBody = response.body();
            log.info("企业微信获取审批模板响应: {}", responseBody);
            
            JSONObject result = JSONUtil.parseObj(responseBody);
            int errcode = result.getInt("errcode", -1);
            if (errcode != 0) {
                String errmsg = result.getStr("errmsg", "未知错误");
                log.error("获取企业微信审批模板失败: errcode={}, errmsg={}", errcode, errmsg);
                throw new BusinessException("获取企业微信审批模板失败: " + errmsg);
            }
            
            // 创建一个新的JSONObject来存储模板信息
            JSONObject template = new JSONObject();
            // 添加模板名称
            template.set("template_names", result.getJSONArray("template_names"));
            // 添加模板内容
            template.set("template_content", result.getJSONObject("template_content"));
            // 添加模板ID
            template.set("template_id", templateId);
            
            return template;
        } catch (Exception e) {
            log.error("获取企业微信审批模板异常", e);
            throw new BusinessException("获取企业微信审批模板失败: " + e.getMessage());
        }
    }

    /**
     * 提交审批申请
     *
     * @param req 申请参数
     * @return 审批单号
     */
    public String applyApproval(ApprovalApplyReq req, List<String> approverUserIds) {
        String url = String.format(WeWorkConstants.APPLY_APPROVAL_URL, getAccessToken());
        Map<String, Object> params = new HashMap<>(8);
        params.put("creator_userid", "admin");  // 这里应该使用企业微信用户ID，需要与系统用户关联
        params.put("template_id", req.getTemplateId());
        params.put("use_template_approver", 0);  // 不使用模板中的审批人
        params.put("approver", buildApprovers(approverUserIds));
        params.put("notify_type", 1);  // 提交审批单时通知审批人
        params.put("apply_data", buildApplyData(req.getFormData()));
        params.put("summary_list", buildSummaryList(req.getTitle()));

        HttpResponse response = HttpRequest.post(url)
            .body(JSONUtil.toJsonStr(params))
            .execute();
        JSONObject result = JSONUtil.parseObj(response.body());
        if (result.getInt("errcode", -1) != 0) {
            log.error("提交企业微信审批申请失败: {}", result.getStr("errmsg"));
            throw new BusinessException("提交企业微信审批申请失败");
        }
        return result.getStr("sp_no");
    }

    /**
     * 获取审批详情
     *
     * @param spNo 审批单号
     * @return 审批详情
     */
    public JSONObject getApprovalDetail(String spNo) {
        String url = String.format(WeWorkConstants.GET_APPROVAL_DETAIL_URL, getAccessToken());
        Map<String, Object> params = new HashMap<>(1);
        params.put("sp_no", spNo);
        HttpResponse response = HttpRequest.post(url)
            .body(JSONUtil.toJsonStr(params))
            .execute();
        JSONObject result = JSONUtil.parseObj(response.body());
        if (result.getInt("errcode", -1) != 0) {
            log.error("获取企业微信审批详情失败: {}", result.getStr("errmsg"));
            throw new BusinessException("获取企业微信审批详情失败");
        }
        return result.getJSONObject("info");
    }

    /**
     * 构建审批人
     *
     * @param approverUserIds 审批人用户ID列表
     * @return 审批人列表
     */
    private List<Map<String, Object>> buildApprovers(List<String> approverUserIds) {
        return approverUserIds.stream().map(userId -> {
            Map<String, Object> approver = new HashMap<>(2);
            approver.put("userid", userId);
            approver.put("attr", 1);  // 1-必须审批
            return approver;
        }).toList();
    }

    /**
     * 构建申请数据
     *
     * @param formData 表单数据
     * @return 申请数据
     */
    private Map<String, Object> buildApplyData(Map<String, Object> formData) {
        Map<String, Object> applyData = new HashMap<>(1);
        applyData.put("contents", formData.entrySet().stream().map(entry -> {
            Map<String, Object> content = new HashMap<>(2);
            content.put("control", entry.getKey());
            content.put("value", entry.getValue());
            return content;
        }).toList());
        return applyData;
    }

    /**
     * 构建摘要信息
     *
     * @param title 标题
     * @return 摘要信息
     */
    private List<Map<String, Object>> buildSummaryList(String title) {
        Map<String, Object> summary = new HashMap<>(2);
        summary.put("summary_info", title);
        return List.of(summary);
    }
}