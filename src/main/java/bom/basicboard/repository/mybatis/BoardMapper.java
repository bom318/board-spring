package bom.basicboard.repository.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import bom.basicboard.domain.Board;
import bom.basicboard.domain.BoardSearchCond;
import bom.basicboard.domain.File;
import bom.basicboard.domain.Rewrite;

@Mapper
public interface BoardMapper {
    
    List<Board> findAll(BoardSearchCond searchCond);
    Board findOne(Long boardNum);
    void write(Board board);
    void update(@Param("boardNum") Long boardNum,@Param("board") Board board);
    void delete(Long boardNum);

    void writeRe(@Param("boardNum") Long boardNum, @Param("rewrite") Rewrite rewrite);
    void deleteRe(Long reId);
    int getReNum(Long boardNum);
    List<Rewrite> getRe(Long boardNum);

    List<File> getFiles(Long boardNum);
    void saveFiles(@Param("boardNum") Long boardNum, @Param("saveFile") File saveFile);
    File findFile(Long fileId);
    void deleteFile(Long boardNum);
}
