package bom.basicboard.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import bom.basicboard.domain.Board;
import bom.basicboard.domain.File;
import bom.basicboard.domain.Rewrite;

public interface BoardRepository {
    
    public Optional<List<Board>> findAll();
    public Board findOne(Long boardNum);
    public Board write(Board board);
    public Board update(Long boardNum, Board board);
    public void delete(Long boardNum);

    public Board writeRe(Long boardNum, Rewrite rewrite);
    public void deleteRe(Long boardNum, Long reId);

    public HashMap<Long, File> saveFiles(Long boardNum, List<MultipartFile> multipartFiles);
    public File findFile(Long boardNum, Long fileId);
    public void deleteFile(Long boardNum);

}
