-- ================================================
-- 校园社团数字化管理与招新服务平台 - MySQL建表脚本
-- ================================================

CREATE DATABASE IF NOT EXISTS campus_club DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE campus_club;

-- 用户表
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    real_name VARCHAR(50) NOT NULL,
    role VARCHAR(20) NOT NULL COMMENT 'STUDENT/LEADER/ADMIN',
    student_id VARCHAR(30),
    college VARCHAR(50),
    major VARCHAR(50),
    email VARCHAR(100),
    phone VARCHAR(20),
    bio VARCHAR(500),
    create_time DATETIME
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 社团表
CREATE TABLE IF NOT EXISTS clubs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    category VARCHAR(30),
    description TEXT,
    requirement TEXT,
    logo VARCHAR(500),
    contact_name VARCHAR(50),
    contact_phone VARCHAR(20),
    contact_email VARCHAR(100),
    leader_id BIGINT,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT 'PENDING/APPROVED/REJECTED',
    create_time DATETIME,
    update_time DATETIME,
    FOREIGN KEY (leader_id) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 报名申请表
CREATE TABLE IF NOT EXISTS applications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id BIGINT NOT NULL,
    club_id BIGINT NOT NULL,
    student_name VARCHAR(50),
    student_id_no VARCHAR(30),
    college VARCHAR(50),
    major VARCHAR(50),
    phone VARCHAR(20),
    email VARCHAR(100),
    reason TEXT,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    leader_remark VARCHAR(500),
    admin_remark VARCHAR(500),
    create_time DATETIME,
    update_time DATETIME,
    FOREIGN KEY (student_id) REFERENCES users(id),
    FOREIGN KEY (club_id) REFERENCES clubs(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 活动表
CREATE TABLE IF NOT EXISTS activities (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    location VARCHAR(200),
    start_time DATETIME,
    end_time DATETIME,
    max_participants INT,
    current_participants INT DEFAULT 0,
    club_id BIGINT,
    status VARCHAR(20) DEFAULT 'UPCOMING',
    create_time DATETIME,
    FOREIGN KEY (club_id) REFERENCES clubs(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 活动报名/签到表
CREATE TABLE IF NOT EXISTS activity_registrations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    activity_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    checked_in BOOLEAN DEFAULT FALSE,
    register_time DATETIME,
    check_in_time DATETIME,
    FOREIGN KEY (activity_id) REFERENCES activities(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
