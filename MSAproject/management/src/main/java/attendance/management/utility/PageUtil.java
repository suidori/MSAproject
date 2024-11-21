package attendance.management.utility;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageUtil {
    public static Pageable getPageable(int pageNum, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "idx");
        Pageable pageable = PageRequest.of(pageNum, size, sort);
        return pageable;
    }

    public static Pageable getPageableASC(int pageNum, int size) {
        Sort sort = Sort.by(Sort.Direction.ASC, "idx");
        Pageable pageable = PageRequest.of(pageNum, size, sort);
        return pageable;
    }

}
