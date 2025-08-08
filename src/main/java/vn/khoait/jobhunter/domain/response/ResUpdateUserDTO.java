package vn.khoait.jobhunter.domain.response;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;
import vn.khoait.jobhunter.util.constant.GenderEnum;
@Getter @Setter
public class ResUpdateUserDTO {
    private Long id;
    private String name;
    private int age;
    private GenderEnum gender;
    private String address;
    private Instant updatedAt;
   
}
