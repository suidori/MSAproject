//package com.green.qna.Repository;
//
//import com.green.qna.Entity.Lecture;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import java.util.List;
//import java.util.Optional;
//
//public interface LectureRepository extends JpaRepository<Lecture, Long> {
//
//    Optional<Lecture> findByTitle(String title);
//    Optional<Lecture> findByIdx(Long idx);
//    List<Lecture> findByEnable(boolean enable);
//}
