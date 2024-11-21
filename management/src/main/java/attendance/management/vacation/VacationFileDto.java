package attendance.management.vacation;

import lombok.Data;
import org.springframework.core.io.Resource;

@Data
public class VacationFileDto {
    private Resource resource;
    private String fileName;

    public VacationFileDto(Resource resource, String fileName) {
        this.resource = resource;
        this.fileName = fileName;
    }

}
