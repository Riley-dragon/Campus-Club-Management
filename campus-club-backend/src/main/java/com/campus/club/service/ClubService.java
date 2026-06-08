package com.campus.club.service;

import com.campus.club.entity.Club;
import com.campus.club.entity.User;
import com.campus.club.repository.ClubRepository;
import com.campus.club.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClubService {

    private final ClubRepository clubRepository;
    private final UserRepository userRepository;

    public List<Club> getApprovedClubs(String keyword) {
        if (keyword != null && !keyword.isEmpty()) {
            return clubRepository.findByNameContaining(keyword);
        }
        return clubRepository.findByStatus(Club.ClubStatus.APPROVED);
    }

    public Club getClubById(Long id) {
        return clubRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("社团不存在"));
    }

    public List<Club> getMyClubs(Long userId) {
        User leader = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        return clubRepository.findByLeader(leader);
    }

    public Club createClub(Long userId, Club club) {
        User leader = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        club.setLeader(leader);
        club.setStatus(Club.ClubStatus.PENDING);
        club.setCreateTime(LocalDateTime.now());
        return clubRepository.save(club);
    }

    public Club updateClub(Long clubId, Long userId, Club updated) {
        Club club = getClubById(clubId);
        if (!club.getLeader().getId().equals(userId)) {
            throw new RuntimeException("无权修改此社团");
        }
        club.setName(updated.getName());
        club.setCategory(updated.getCategory());
        club.setDescription(updated.getDescription());
        club.setRequirement(updated.getRequirement());
        club.setContactName(updated.getContactName());
        club.setContactPhone(updated.getContactPhone());
        club.setContactEmail(updated.getContactEmail());
        club.setUpdateTime(LocalDateTime.now());
        return clubRepository.save(club);
    }
}
