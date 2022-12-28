package bom.basicboard.domain;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Rewrite {
    @NotBlank
    private Long reId;
    @NotBlank
    private String memberId;
    @NotBlank
    private String reContent;
    @NotBlank
    private String reWriter;
    @NotBlank
    private String reDate;
    @NotBlank
    private Long boardNum;
    public Rewrite() {
    }
    public Rewrite(String reContent, String reWriter, String memberId) {
        this.reContent = reContent;
        this.reWriter = reWriter;
        this.memberId = memberId;
    }

    
}
