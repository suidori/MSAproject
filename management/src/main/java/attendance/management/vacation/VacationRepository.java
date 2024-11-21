package attendance.management.vacation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VacationRepository extends JpaRepository<Vacation, Long> {
    Page<Vacation> findByUser_Idx(Long userIdx, Pageable pageable);
    Page<Vacation> findByLecture_Idx(Long lectureIdx, Pageable pageable);
    Page<Vacation> findByAccept(Boolean accept, Pageable pageable);
    Page<Vacation> findByAcceptAndUser_Idx(Boolean accept, Long userIdx, Pageable pageable);

    @Query("SELECT v FROM Vacation v JOIN v.user u WHERE u.name LIKE :name%")
    Page<Vacation> findByUserName(@Param("name") String name, Pageable pageable);
}
