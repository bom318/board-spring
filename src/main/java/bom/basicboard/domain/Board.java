package bom.basicboard.domain;

import java.util.HashMap;
import java.util.List;

import javax.validation.constraints.NotBlank;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Board {
    
    @NotBlank
    private Long boardNum;
    @NotBlank
    private String boardTitle;
    @NotBlank
    private String writer;
    @NotBlank
    private String date;
    @NotBlank
    private String content;

    private HashMap<Long, Rewrite> reples;
    private HashMap<Long, File> files;

    public Board() {
    }

    public Board(String boardTitle, String writer, String content) {
        this.boardTitle = boardTitle;
        this.writer = writer;
        this.content = content;
    }
    
    
}
