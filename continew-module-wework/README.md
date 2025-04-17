# 企业微信模块

本模块提供企业微信集成功能，主要包括审批流程的集成。

## 功能特性

1. 审批模板管理：同步企业微信审批模板到系统中
2. 审批申请：创建审批申请并提交到企业微信
3. 审批状态跟踪：通过回调接收企业微信审批状态变更通知
4. 审批记录查询：查询审批申请和审批记录

## 使用说明

1. 在企业微信管理后台创建自建应用，获取企业ID、应用ID和应用密钥
2. 在应用管理中设置审批应用可见范围
3. 在回调配置中设置接收审批状态变更的URL，格式为：`https://您的域名/wework/callback/approval`
4. 在系统配置中填写企业微信相关配置
5. 使用API接口进行审批模板同步、审批申请等操作

## API接口

### 审批模板管理

- `GET /wework/approval/templates` - 获取审批模板列表
- `GET /wework/approval/template/{id}` - 获取审批模板详情
- `POST /wework/approval/template/sync` - 同步审批模板

### 审批申请

- `POST /wework/approval/apply` - 创建审批申请
- `GET /wework/approval/detail/{spNo}` - 获取审批详情
- `GET /wework/approval/status/{spNo}` - 获取审批状态
- `GET /wework/approval/page` - 分页查询审批申请

## 配置说明

```yaml
# 企业微信配置
wework:
  # 企业ID
  corp-id: ww12345678
  # 应用ID
  agent-id: 1000001
  # 应用密钥
  secret: abcdefghijklmn
  # 回调token
  token: token123
  # 回调消息加密密钥
  encoding-aes-key: abcdefghijklmnopqrstuvwxyz0123456789ABCDEFG
  # 回调域名
  callback-domain: https://example.com