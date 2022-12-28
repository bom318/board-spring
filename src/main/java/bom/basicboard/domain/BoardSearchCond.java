package bom.basicboard.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardSearchCond {
    
    private String filter;
    private String word;
    public BoardSearchCond() {
    }
    
}
