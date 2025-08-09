package vn.khoait.jobhunter.domain.response.resume;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.khoait.jobhunter.util.constant.StatusEnum;

@Getter @Setter
public class ResFetchResumeDTO {
    private long id;
    private String email;
    private String url;	
    private StatusEnum status;
    private Instant createdAt;	
    private Instant updatedAt;	
    private String createdBy;	
    private String updatedBy;
    private String companyName;
    private UserResume user;
    private JobResume job;
    

    @Getter @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserResume {
        private long id;
        private String name;
    }

    @Getter @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class JobResume {
        private long id;
        private String name;
    }
}
