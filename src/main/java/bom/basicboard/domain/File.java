package bom.basicboard.domain;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class File {
    
    @NotBlank
    private Long boardNum;
    @NotBlank
    private String filePath;
    public File(@NotBlank Long boardNum, @NotBlank String filePath) {
        this.boardNum = boardNum;
        this.filePath = filePath;
    }
    
}
