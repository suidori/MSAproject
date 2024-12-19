//package com.green.qna.Repository;
//
//import com.green.qna.Entity.User;
//import com.green.qna.Entity.UserAndLecture;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//
//import java.util.List;
//import java.util.Optional;
//
//public interface UserAndLectrueRepository extends JpaRepository<UserAndLecture, Long> {
//
//    List<UserAndLecture> findByUser_Idx(Long userIdx);
//
//    Optional<UserAndLecture> findByUser_IdxAndState(Long userIdx, int state);
//
//    Page<UserAndLecture> findByUser_Idx(Long userIdx, Pageable pageable);
//
//    List<UserAndLecture> findByState(int state);
//
//    @Query("SELECT ul.user FROM UserAndLecture ul JOIN ul.user u WHERE u.name = :name AND ul.lecture.idx = :lectureIdx")
//    Optional<User> findUsersByNameAndLectureIdx(@Param("name") String name, @Param("lectureIdx") Long lectureIdx);
//
//}
