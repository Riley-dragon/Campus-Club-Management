package com.campus.club.config;

import com.campus.club.entity.User;
import com.campus.club.entity.Club;
import com.campus.club.repository.UserRepository;
import com.campus.club.repository.ClubRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;

@Configuration
public class InitDataConfig {

    @Bean
    public CommandLineRunner initData(UserRepository userRepository, ClubRepository clubRepository) {
        return args -> {
            if (userRepository.count() == 0) {
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

                // 创建管理员账号
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(encoder.encode("admin123"));
                admin.setRealName("系统管理员");
                admin.setRole(User.Role.ADMIN);
                admin.setEmail("admin@campus.edu.cn");
                admin.setPhone("13800000000");
                admin.setCreateTime(LocalDateTime.now());
                userRepository.save(admin);

                // 创建社团负责人账号
                User leader = new User();
                leader.setUsername("leader01");
                leader.setPassword(encoder.encode("leader123"));
                leader.setRealName("张三");
                leader.setRole(User.Role.LEADER);
                leader.setStudentId("2024001");
                leader.setCollege("计算机学院");
                leader.setMajor("软件工程");
                leader.setEmail("leader@campus.edu.cn");
                leader.setPhone("13800000001");
                leader.setCreateTime(LocalDateTime.now());
                userRepository.save(leader);

                // 创建学生账号
                User student = new User();
                student.setUsername("student01");
                student.setPassword(encoder.encode("student123"));
                student.setRealName("李四");
                student.setRole(User.Role.STUDENT);
                student.setStudentId("2024002");
                student.setCollege("计算机学院");
                student.setMajor("计算机科学");
                student.setEmail("student@campus.edu.cn");
                student.setPhone("13800000002");
                student.setCreateTime(LocalDateTime.now());
                userRepository.save(student);

                // 创建示例社团
                Club club = new Club();
                club.setName("ACM编程社");
                club.setCategory("学术");
                club.setDescription("ACM编程社致力于培养学生的算法设计与编程能力，定期举办算法训练、编程竞赛和学术交流活动。社团成员在各类编程竞赛中屡获佳绩。");
                club.setRequirement("热爱编程，有一定的编程基础，愿意投入时间进行算法训练。");
                club.setContactName("张三");
                club.setContactPhone("13800000001");
                club.setContactEmail("acm@campus.edu.cn");
                club.setLeader(leader);
                club.setStatus(Club.ClubStatus.APPROVED);
                club.setCreateTime(LocalDateTime.now());
                clubRepository.save(club);

                Club club2 = new Club();
                club2.setName("摄影协会");
                club2.setCategory("文艺");
                club2.setDescription("摄影协会是一个充满艺术气息的社团，聚集了众多摄影爱好者。社团定期组织户外采风、摄影讲座和作品展览。");
                club2.setRequirement("热爱摄影艺术，有相机或手机均可。");
                club2.setContactName("王五");
                club2.setContactPhone("13800000003");
                club2.setContactEmail("photo@campus.edu.cn");
                club2.setLeader(leader);
                club2.setStatus(Club.ClubStatus.APPROVED);
                club2.setCreateTime(LocalDateTime.now());
                clubRepository.save(club2);

                Club club3 = new Club();
                club3.setName("篮球社");
                club3.setCategory("体育");
                club3.setDescription("篮球社是校园最具活力的体育社团之一，每周组织训练和友谊赛，代表学校参加各类篮球赛事。");
                club3.setRequirement("热爱篮球运动，能坚持参加训练。");
                club3.setContactName("赵六");
                club3.setContactPhone("13800000004");
                club3.setContactEmail("basketball@campus.edu.cn");
                club3.setLeader(leader);
                club3.setStatus(Club.ClubStatus.APPROVED);
                club3.setCreateTime(LocalDateTime.now());
                clubRepository.save(club3);

                System.out.println("========== 初始数据加载完成 ==========");
                System.out.println("管理员: admin / admin123");
                System.out.println("社团负责人: leader01 / leader123");
                System.out.println("学生: student01 / student123");
                System.out.println("======================================");
            }
        };
    }
}
