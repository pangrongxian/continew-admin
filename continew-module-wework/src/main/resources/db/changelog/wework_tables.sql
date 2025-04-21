-- liquibase formatted sql

-- changeset charles7c:2024.01.01-wework-01
-- 审批模板表
CREATE TABLE IF NOT EXISTS ww_approval_template (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    template_id VARCHAR(64) NOT NULL COMMENT '企业微信模板ID',
    template_name VARCHAR(100) NOT NULL COMMENT '模板名称',
    template_content TEXT COMMENT '模板内容JSON',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '更新时间',
    UNIQUE KEY uk_template_id (template_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='审批模板表';

-- changeset charles7c:2024.01.01-wework-02
-- 审批申请表
CREATE TABLE IF NOT EXISTS ww_approval_apply (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    sp_no VARCHAR(64) NOT NULL COMMENT '审批单号',
    template_id VARCHAR(64) NOT NULL COMMENT '模板ID',
    title VARCHAR(200) NOT NULL COMMENT '申请标题',
    creator_userid BIGINT NOT NULL COMMENT '申请人ID',
    apply_time DATETIME NOT NULL COMMENT '申请时间',
    status INT NOT NULL COMMENT '审批状态',
    apply_data TEXT COMMENT '审批控件数据JSON',
    process TEXT COMMENT '审批人员数据JSON',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '更新时间',
    UNIQUE KEY uk_sp_no (sp_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='审批申请表';

-- changeset charles7c:2024.01.01-wework-03
-- 审批记录表
CREATE TABLE IF NOT EXISTS ww_approval_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    sp_no VARCHAR(64) NOT NULL COMMENT '审批单号',
    approver_id BIGINT NOT NULL COMMENT '审批人ID',
    approver_name VARCHAR(50) NOT NULL COMMENT '审批人姓名',
    status INT NOT NULL COMMENT '审批状态',
    comment VARCHAR(500) COMMENT '审批意见',
    approve_time DATETIME NOT NULL COMMENT '审批时间',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '更新时间',
    KEY idx_sp_no (sp_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='审批记录表';