package bom.basicboard.domain;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Member {
    private Long uuid;

    @NotBlank(message="필수 입력 항목입니다")
    @Pattern(regexp = "^[a-zA-Z][0-9a-zA-Z]{7,}$")
        private String memberId;
    @NotBlank(message="필수 입력 항목입니다")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{7,}$")
    private String password;

    private String memberName;

    

    public Member() {
    }

    public Member(String memberId, String password, String memberName) {
        this.memberId = memberId;
        this.password = password;
        this.memberName = memberName;
    }

    public Member(String memberId, String password) {
        this.memberId = memberId;
        this.password = password;
    }

    
    
    
}