# 校园社团数字化管理与招新服务平台

## 项目概述

B/S架构的校园社团管理平台，实现社团展示、在线报名、多级审核、成员管理、活动发布五大核心功能。

**技术栈：** SpringBoot 3 + JPA + H2/MySQL + Vue3 + Element Plus

## 后端启动

```bash
cd campus-club-backend
mvn spring-boot:run
```

启动后访问 http://localhost:8080 ，H2控制台 http://localhost:8080/h2-console

## 前端启动

```bash
cd campus-club-frontend
npm install
npm run dev
```

启动后访问 http://localhost:3000

## 测试账号

| 角色 | 用户名 | 密码 |
|------|--------|------|
| 管理员 | admin | admin123 |
| 社团负责人 | leader01 | leader123 |
| 学生 | student01 | student123 |

## 功能模块

1. **社团展示** - 社团列表、详情查看、搜索筛选
2. **在线报名** - 学生填写申请、提交报名
3. **审核管理** - 社团负责人初审 → 管理员终审
4. **成员管理** - 成员列表、移除、Excel导出
5. **活动管理** - 发布活动、活动报名、签到管理
6. **后台管理** - 数据看板、社团审核、用户管理

## 切换MySQL

修改 `application.yml` 中的数据库配置，注释H2部分，启用MySQL部分，执行 `schema.sql` 建表。
