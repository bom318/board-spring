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
    private String uploadFileName;
    @NotBlank
    private String storeFileName;
    public File(@NotBlank Long boardNum, @NotBlank String uploadFileName, @NotBlank String storeFileName) {
        this.boardNum = boardNum;
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
    }

    
    
    
}
