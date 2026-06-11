# 抖音商家数据分析系统

自用抖音商家数据分析系统，用于收集、整理、分析商家多平台数据，输出营销报告。

## 技术栈

| 层级 | 技术 |
|------|------|
| 前端 | Vue 3 + Vite + Element Plus + ECharts |
| 后端 | Spring Boot 3 + MyBatis-Plus |
| 数据库 | MySQL 8.0 |
| 爬虫 | Jsoup + HttpClient |

## 项目结构

```
mt/
├── docs/                     # 开发设计文档
│   └── 开发设计文档.md
├── backend/                  # 后端 Spring Boot 项目
│   ├── pom.xml
│   ├── sql/
│   │   └── init.sql         # 数据库初始化脚本
│   └── src/main/java/com/merchantanalysis/
│       ├── MerchantAnalysisApplication.java
│       ├── config/          # 配置类
│       ├── controller/      # 控制器层
│       ├── service/         # 服务层
│       │   ├── merchant/    # 商家管理服务
│       │   ├── fetch/       # 数据采集服务
│       │   ├── analysis/    # 对比分析服务
│       │   ├── report/      # 报告服务
│       │   └── thirdparty/  # 第三方API服务
│       ├── mapper/          # 数据访问层
│       ├── entity/          # 实体类
│       ├── dto/             # 数据传输对象
│       └── common/          # 通用类
└── frontend/                # 前端 Vue 3 项目
    ├── package.json
    ├── vite.config.js
    └── src/
        ├── api/             # API 接口封装
        ├── stores/          # Pinia 状态管理
        ├── views/           # 页面组件
        │   ├── dashboard/   # 首页
        │   ├── merchant/    # 商家管理
        │   ├── fetch/       # 数据采集
        │   ├── analysis/    # 对比分析
        │   └── report/      # 报告中心
        └── assets/          # 静态资源
```

## 快速启动

### 1. 数据库初始化

```bash
# 登录 MySQL
mysql -u root -p

# 执行初始化脚本
source backend/sql/init.sql
```

### 2. 后端启动

```bash
cd backend

# 配置数据库连接 (修改 src/main/resources/application.yml)
# spring.datasource.url: jdbc:mysql://localhost:3306/merchant_analysis
# spring.datasource.username: root
# spring.datasource.password: your_password

# 编译运行
mvn clean install
mvn spring-boot:run

# 后端启动后访问: http://localhost:8080/api
```

### 3. 前端启动

```bash
cd frontend

# 安装依赖
npm install

# 开发模式运行
npm run dev

# 前端启动后访问: http://localhost:5173
```

## 功能模块

- **商家管理** - 商家的增删改查、详情查看、社交账号管理
- **数据采集** - 官网采集、财报采集、产品评价采集、第三方数据接入
- **对比分析** - 多商家多维度对比、商家画像生成
- **报告中心** - 营销报告、财务报告、对比报告生成与导出

## API 接口

| 路径 | 方法 | 说明 |
|------|------|------|
| `/merchant` | GET | 商家列表 |
| `/merchant/{id}` | GET | 商家详情 |
| `/merchant` | POST | 添加商家 |
| `/merchant/{id}` | PUT | 更新商家 |
| `/merchant/{id}` | DELETE | 删除商家 |
| `/fetch-task` | GET | 采集任务列表 |
| `/fetch-task` | POST | 创建采集任务 |
| `/analysis/compare` | POST | 执行对比分析 |
| `/report/generate` | POST | 生成报告 |

## 数据库表

- `merchant` - 商家基础信息
- `merchant_social_account` - 社交账号
- `merchant_product` - 商家产品
- `merchant_financial` - 财报数据
- `merchant_operating_data` - 经营数据
- `merchant_review` - 产品评价
- `merchant_comparison` - 对比记录
- `merchant_report` - 报告
- `fetch_task` - 采集任务
- `third_party_config` - 第三方API配置
- `industry_category` - 行业分类

## 注意事项

1. 首次使用请先在 MySQL 中执行 `backend/sql/init.sql` 创建数据库和表
2. 修改 `backend/src/main/resources/application.yml` 中的数据库连接信息
3. 第三方平台（飞瓜、蝉妈妈、发现报告）需自行申请 API 密钥并配置
4. 爬虫功能请遵守目标网站的 robots.txt 和使用条款
