package bom.basicboard.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Member {
    private Long uuid;
    private String memberId;
    private String password;
    private String memberName;

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