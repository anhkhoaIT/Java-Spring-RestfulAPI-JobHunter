package vn.khoait.jobhunter.domain.response;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;
import vn.khoait.jobhunter.domain.Company;
import vn.khoait.jobhunter.util.constant.GenderEnum;
@Getter @Setter
public class ResCreateUserDTO {
    private Long id;
    private String name;
    private String email;
    private int age;
    private GenderEnum gender;
    private String address;
    private Instant createdAt;
    private CompanyUser company;
    @Getter @Setter
    public static class CompanyUser {
        private long id;
        private String name;
    }
   
}
