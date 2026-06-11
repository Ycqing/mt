-- ============================================
-- 抖音商家数据分析系统 - 数据库初始化脚本
-- ============================================

CREATE DATABASE IF NOT EXISTS merchant_analysis DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE merchant_analysis;

-- ============================================
-- 1. 行业分类表
-- ============================================
DROP TABLE IF EXISTS industry_category;
CREATE TABLE industry_category (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    name VARCHAR(100) NOT NULL COMMENT '分类名称',
    parent_id BIGINT DEFAULT NULL COMMENT '父级ID',
    level INT DEFAULT 1 COMMENT '层级深度',
    sort_order INT DEFAULT 0 COMMENT '排序',
    deleted INT DEFAULT 0 COMMENT '逻辑删除: 0-未删除 1-已删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_parent_id (parent_id),
    INDEX idx_deleted (deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='行业分类表';

-- ============================================
-- 2. 商家基础信息表
-- ============================================
DROP TABLE IF EXISTS merchant;
CREATE TABLE merchant (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    name VARCHAR(200) NOT NULL COMMENT '商家名称',
    website VARCHAR(500) DEFAULT NULL COMMENT '官网地址',
    is_listed TINYINT DEFAULT 0 COMMENT '是否上市: 0-否 1-是',
    listing_exchange VARCHAR(50) DEFAULT NULL COMMENT '上市交易所',
    stock_code VARCHAR(50) DEFAULT NULL COMMENT '股票代码',
    industry_id BIGINT DEFAULT NULL COMMENT '行业分类ID',
    company_scale VARCHAR(50) DEFAULT NULL COMMENT '企业规模',
    registered_capital DECIMAL(15,2) DEFAULT NULL COMMENT '注册资本(万元)',
    established_date DATE DEFAULT NULL COMMENT '成立日期',
    business_description TEXT COMMENT '商家介绍',
    advantages TEXT COMMENT '核心优势',
    products_info TEXT COMMENT '产品信息',
    revenue DECIMAL(15,4) DEFAULT NULL COMMENT '营收(万元)',
    employee_count INT DEFAULT NULL COMMENT '员工人数',
    headquarters VARCHAR(200) DEFAULT NULL COMMENT '总部地址',
    source_url VARCHAR(500) DEFAULT NULL COMMENT '数据来源URL',
    data_status TINYINT DEFAULT 0 COMMENT '数据状态: 0-待采集 1-采集中 2-已完成 3-失败',
    last_fetch_time DATETIME DEFAULT NULL COMMENT '最后采集时间',
    deleted INT DEFAULT 0 COMMENT '逻辑删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_name (name),
    INDEX idx_industry_id (industry_id),
    INDEX idx_is_listed (is_listed),
    INDEX idx_data_status (data_status),
    INDEX idx_deleted (deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商家基础信息表';

-- ============================================
-- 3. 社交账号表
-- ============================================
DROP TABLE IF EXISTS merchant_social_account;
CREATE TABLE merchant_social_account (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    merchant_id BIGINT NOT NULL COMMENT '商家ID',
    platform VARCHAR(50) NOT NULL COMMENT '平台: douyin/xiaohongshu/weibo/weixin/redbook',
    account_name VARCHAR(200) DEFAULT NULL COMMENT '账号名称',
    account_id VARCHAR(200) DEFAULT NULL COMMENT '平台账号ID',
    account_url VARCHAR(500) DEFAULT NULL COMMENT '账号主页URL',
    followers_count BIGINT DEFAULT 0 COMMENT '粉丝数',
    following_count BIGINT DEFAULT 0 COMMENT '关注数',
    posts_count INT DEFAULT 0 COMMENT '帖子数',
    verification_status TINYINT DEFAULT 0 COMMENT '认证状态: 0-未认证 1-已认证',
    last_fetch_time DATETIME DEFAULT NULL COMMENT '最后采集时间',
    deleted INT DEFAULT 0 COMMENT '逻辑删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_merchant_id (merchant_id),
    INDEX idx_platform (platform),
    INDEX idx_deleted (deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='社交账号表';

-- ============================================
-- 4. 商家产品表
-- ============================================
DROP TABLE IF EXISTS merchant_product;
CREATE TABLE merchant_product (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    merchant_id BIGINT NOT NULL COMMENT '商家ID',
    product_name VARCHAR(200) NOT NULL COMMENT '产品名称',
    category VARCHAR(100) DEFAULT NULL COMMENT '产品类别',
    price DECIMAL(10,2) DEFAULT NULL COMMENT '价格(元)',
    sales_count BIGINT DEFAULT 0 COMMENT '销量',
    rating DECIMAL(3,2) DEFAULT NULL COMMENT '评分',
    product_url VARCHAR(500) DEFAULT NULL COMMENT '产品链接',
    description TEXT COMMENT '产品描述',
    tags VARCHAR(500) DEFAULT NULL COMMENT '产品标签',
    source VARCHAR(50) DEFAULT NULL COMMENT '数据来源: 抖音/天猫/官网',
    deleted INT DEFAULT 0 COMMENT '逻辑删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_merchant_id (merchant_id),
    INDEX idx_category (category),
    INDEX idx_deleted (deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商家产品表';

-- ============================================
-- 5. 商家财报数据表
-- ============================================
DROP TABLE IF EXISTS merchant_financial;
CREATE TABLE merchant_financial (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    merchant_id BIGINT NOT NULL COMMENT '商家ID',
    report_year INT NOT NULL COMMENT '报告年度',
    report_period VARCHAR(20) DEFAULT '年报' COMMENT '报告期: 年报/半年报/季报',
    report_date DATE DEFAULT NULL COMMENT '报告日期',
    revenue DECIMAL(15,2) DEFAULT NULL COMMENT '营业收入(万元)',
    net_profit DECIMAL(15,2) DEFAULT NULL COMMENT '净利润(万元)',
    gross_margin DECIMAL(5,2) DEFAULT NULL COMMENT '毛利率(%)',
    operating_margin DECIMAL(5,2) DEFAULT NULL COMMENT '营业利润率(%)',
    total_assets DECIMAL(15,2) DEFAULT NULL COMMENT '总资产(万元)',
    total_liabilities DECIMAL(15,2) DEFAULT NULL COMMENT '总负债(万元)',
    roe DECIMAL(5,2) DEFAULT NULL COMMENT '净资产收益率(%)',
    eps DECIMAL(10,4) DEFAULT NULL COMMENT '每股收益(元)',
    source VARCHAR(100) DEFAULT NULL COMMENT '数据来源',
    source_url VARCHAR(500) DEFAULT NULL COMMENT '原始链接',
    deleted INT DEFAULT 0 COMMENT '逻辑删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_merchant_id (merchant_id),
    INDEX idx_report_year (report_year),
    INDEX idx_deleted (deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商家财报数据表';

-- ============================================
-- 6. 经营数据表
-- ============================================
DROP TABLE IF EXISTS merchant_operating_data;
CREATE TABLE merchant_operating_data (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    merchant_id BIGINT NOT NULL COMMENT '商家ID',
    platform VARCHAR(50) DEFAULT NULL COMMENT '数据平台: feigua/chanmama/manual',
    data_date DATE NOT NULL COMMENT '数据日期',
    gmv DECIMAL(15,2) DEFAULT NULL COMMENT 'GMV(万元)',
    sales_amount DECIMAL(15,2) DEFAULT NULL COMMENT '销售额(万元)',
    orders_count BIGINT DEFAULT 0 COMMENT '订单数',
    avg_order_price DECIMAL(10,2) DEFAULT NULL COMMENT '客单价(元)',
    conversion_rate DECIMAL(5,2) DEFAULT NULL COMMENT '转化率(%)',
    traffic_count BIGINT DEFAULT 0 COMMENT '访客数',
    fans_growth INT DEFAULT 0 COMMENT '粉丝增长数',
    engagement_rate DECIMAL(5,2) DEFAULT NULL COMMENT '互动率(%)',
    live_sales DECIMAL(15,2) DEFAULT NULL COMMENT '直播销售额(万元)',
    ad_investment DECIMAL(15,2) DEFAULT NULL COMMENT '广告投入(万元)',
    roi DECIMAL(5,2) DEFAULT NULL COMMENT 'ROI',
    hot_products_rank INT DEFAULT NULL COMMENT '爆款排名',
    category_rank INT DEFAULT NULL COMMENT '品类排名',
    market_share DECIMAL(8,4) DEFAULT NULL COMMENT '市场份额',
    raw_data JSON DEFAULT NULL COMMENT '原始JSON数据',
    deleted INT DEFAULT 0 COMMENT '逻辑删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_merchant_id (merchant_id),
    INDEX idx_platform (platform),
    INDEX idx_data_date (data_date),
    INDEX idx_deleted (deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='经营数据表';

-- ============================================
-- 7. 产品评价表
-- ============================================
DROP TABLE IF EXISTS merchant_review;
CREATE TABLE merchant_review (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    merchant_id BIGINT NOT NULL COMMENT '商家ID',
    product_id BIGINT DEFAULT NULL COMMENT '产品ID',
    platform VARCHAR(50) NOT NULL COMMENT '评价平台: douyin/xiaohongshu/taobao/jd',
    reviewer_nickname VARCHAR(100) DEFAULT NULL COMMENT '评价者昵称',
    rating TINYINT DEFAULT NULL COMMENT '评分 1-5',
    content TEXT COMMENT '评价内容',
    like_count INT DEFAULT 0 COMMENT '点赞数',
    reply_count INT DEFAULT 0 COMMENT '回复数',
    media_urls JSON DEFAULT NULL COMMENT '图片/视频URL数组',
    sentiment VARCHAR(20) DEFAULT NULL COMMENT '情感分析: positive/negative/neutral',
    keywords VARCHAR(500) DEFAULT NULL COMMENT '关键词标签',
    review_date DATE DEFAULT NULL COMMENT '评价日期',
    source_url VARCHAR(500) DEFAULT NULL COMMENT '原始链接',
    deleted INT DEFAULT 0 COMMENT '逻辑删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_merchant_id (merchant_id),
    INDEX idx_product_id (product_id),
    INDEX idx_platform (platform),
    INDEX idx_sentiment (sentiment),
    INDEX idx_review_date (review_date),
    INDEX idx_deleted (deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='产品评价表';

-- ============================================
-- 8. 商家对比分析表
-- ============================================
DROP TABLE IF EXISTS merchant_comparison;
CREATE TABLE merchant_comparison (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    name VARCHAR(200) NOT NULL COMMENT '对比方案名称',
    merchant_ids JSON NOT NULL COMMENT '参与对比的商家ID数组',
    compare_dimensions JSON DEFAULT NULL COMMENT '对比维度配置',
    summary JSON DEFAULT NULL COMMENT '对比结论摘要',
    created_by VARCHAR(100) DEFAULT NULL COMMENT '创建人',
    deleted INT DEFAULT 0 COMMENT '逻辑删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_created_by (created_by),
    INDEX idx_deleted (deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商家对比分析表';

-- ============================================
-- 9. 报告表
-- ============================================
DROP TABLE IF EXISTS merchant_report;
CREATE TABLE merchant_report (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    merchant_id BIGINT DEFAULT NULL COMMENT '商家ID',
    report_name VARCHAR(200) NOT NULL COMMENT '报告名称',
    report_type VARCHAR(50) DEFAULT 'full' COMMENT '报告类型: full/marketing/financial/comparison/product',
    report_period VARCHAR(50) DEFAULT NULL COMMENT '报告周期',
    content LONGTEXT COMMENT '报告内容(JSON结构)',
    file_path VARCHAR(500) DEFAULT NULL COMMENT '导出文件路径',
    status TINYINT DEFAULT 0 COMMENT '状态: 0-生成中 1-已完成 2-失败',
    created_by VARCHAR(100) DEFAULT NULL COMMENT '创建人',
    generate_time DATETIME DEFAULT NULL COMMENT '生成时间',
    deleted INT DEFAULT 0 COMMENT '逻辑删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_merchant_id (merchant_id),
    INDEX idx_report_type (report_type),
    INDEX idx_status (status),
    INDEX idx_deleted (deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='报告表';

-- ============================================
-- 10. 采集任务表
-- ============================================
DROP TABLE IF EXISTS fetch_task;
CREATE TABLE fetch_task (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    task_name VARCHAR(200) NOT NULL COMMENT '任务名称',
    merchant_id BIGINT DEFAULT NULL COMMENT '商家ID',
    task_type VARCHAR(50) NOT NULL COMMENT '任务类型: website/financial/review/product/social/operating',
    task_status VARCHAR(20) DEFAULT 'pending' COMMENT '任务状态: pending/running/success/failed/paused',
    priority INT DEFAULT 3 COMMENT '优先级 1-5',
    source_url VARCHAR(500) DEFAULT NULL COMMENT '采集源URL',
    params JSON DEFAULT NULL COMMENT '任务参数',
    result TEXT DEFAULT NULL COMMENT '执行结果/错误信息',
    retry_count INT DEFAULT 0 COMMENT '重试次数',
    execute_time DATETIME DEFAULT NULL COMMENT '执行时间',
    cost_ms BIGINT DEFAULT NULL COMMENT '耗时(毫秒)',
    deleted INT DEFAULT 0 COMMENT '逻辑删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_merchant_id (merchant_id),
    INDEX idx_task_type (task_type),
    INDEX idx_task_status (task_status),
    INDEX idx_deleted (deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='采集任务表';

-- ============================================
-- 11. 第三方API配置表
-- ============================================
DROP TABLE IF EXISTS third_party_config;
CREATE TABLE third_party_config (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    platform VARCHAR(50) NOT NULL COMMENT '平台名称: feigua/chanmama/faxianbaogao',
    api_name VARCHAR(100) DEFAULT NULL COMMENT 'API名称',
    api_url VARCHAR(500) DEFAULT NULL COMMENT 'API地址',
    app_key VARCHAR(200) DEFAULT NULL COMMENT 'AppKey',
    app_secret VARCHAR(500) DEFAULT NULL COMMENT 'AppSecret(加密存储)',
    rate_limit INT DEFAULT 60 COMMENT '调用频率限制(次/分钟)',
    monthly_quota INT DEFAULT 10000 COMMENT '月度配额',
    monthly_used INT DEFAULT 0 COMMENT '本月已用',
    status TINYINT DEFAULT 1 COMMENT '状态: 0-禁用 1-启用',
    deleted INT DEFAULT 0 COMMENT '逻辑删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE INDEX idx_platform (platform),
    INDEX idx_deleted (deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='第三方API配置表';

-- ============================================
-- 初始化行业分类数据
-- ============================================
INSERT INTO industry_category (name, parent_id, level, sort_order) VALUES
('美妆护肤', NULL, 1, 1),
('服装鞋包', NULL, 1, 2),
('食品饮料', NULL, 1, 3),
('数码家电', NULL, 1, 4),
('家居生活', NULL, 1, 5),
('母婴用品', NULL, 1, 6),
('运动户外', NULL, 1, 7),
('宠物用品', NULL, 1, 8),
('珠宝配饰', NULL, 1, 9),
('其他', NULL, 1, 10);

INSERT INTO industry_category (name, parent_id, level, sort_order) VALUES
('彩妆', 1, 2, 1),
('护肤', 1, 2, 2),
('香水', 1, 2, 3),
('美妆工具', 1, 2, 4);

-- ============================================
-- 初始化示例商家数据
-- ============================================
INSERT INTO merchant (name, website, is_listed, industry_id, company_scale, employee_count, headquarters, data_status, business_description) VALUES
('完美日记', 'https://www.perfectdiary.com', 0, 1, '1000-9999人', 3500, '广州', 2, '完美日记是逸仙电商旗下美妆品牌，致力于为年轻女性提供高品质美妆产品。'),
('花西子', 'https://www.huaxizi.com', 0, 1, '500-999人', 800, '杭州', 2, '花西子是东方彩妆品牌，以草本养肤为理念，打造适合东方女性的彩妆产品。'),
('珀莱雅', 'https://www.proya.com.cn', 1, 1, '1000-9999人', 2800, '杭州', 2, '珀莱雅是A股上市公司，专注于海洋护肤研究，为消费者提供科学护肤方案。');

-- ============================================
-- 初始化第三方平台配置（占位）
-- ============================================
INSERT INTO third_party_config (platform, api_name, rate_limit, monthly_quota, status) VALUES
('feigua', '飞瓜数据', 60, 10000, 0),
('chanmama', '蝉妈妈', 60, 10000, 0),
('faxian', '发现报告', 30, 1000, 0);
