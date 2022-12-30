package bom.basicboard.repository.mybatis;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import bom.basicboard.domain.Board;
import bom.basicboard.domain.BoardSearchCond;
import bom.basicboard.domain.File;
import bom.basicboard.domain.Rewrite;
import bom.basicboard.repository.BoardRepository;
import bom.basicboard.repository.FileStore;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MyBatisBoardRepository implements BoardRepository{

    private final BoardMapper mapper;
    private final FileStore fileStore;

    @Override
    public void delete(Long boardNum) {
        mapper.delete(boardNum);
        
    }

    @Override
    public void deleteFile(Long boardNum) {
        mapper.deleteFile(boardNum);
        
    }

    @Override
    public void deleteRe(Long boardNum, Long reId) {
        mapper.deleteRe(reId);
        
    }

    @Override
    public Optional<List<Board>> findAll(BoardSearchCond searchCond) {
        List<Board> boards = mapper.findAll(searchCond);
        for (Board board : boards) {
            int reNum = mapper.getReNum(board.getBoardNum());
            board.setReNum(reNum);
        }
        
        return Optional.ofNullable(boards);
    }

    @Override
    public File findFile(Long boardNum, Long fileId) {
        return mapper.findFile(fileId);
    }

    @Override
    public Board findOne(Long boardNum) {
        Board board = mapper.findOne(boardNum);
        List<File> files = mapper.getFiles(boardNum);
        if(files.size() > 0) {
            HashMap<Long, File> fileMap = new HashMap<>();
            for (File file : files) {
                fileMap.put(file.getFileId(), file);
            }
            board.setFiles(fileMap);
        }
        List<Rewrite> reples = mapper.getRe(boardNum);
        if (reples.size() > 0) {
            HashMap<Long, Rewrite> reMap = new HashMap<>();
            for (Rewrite repl : reples) {
                reMap.put(repl.getReId(), repl);
            }
            board.setReples(reMap);
        }
        return board;
    }

    @Override
    public HashMap<Long, File> saveFiles(Long boardNum, List<MultipartFile> multipartFiles) {
        HashMap<Long, File> files = new HashMap<>();
        List<File> saveFiles = fileStore.saveFiles(boardNum, multipartFiles);
        if(saveFiles == null) {
            return null;
        }
        
        for (File file : saveFiles) {
            mapper.saveFiles(boardNum, file);
            files.put(file.getFileId(), file);
        }
        return files;
    }

    @Override
    public Board update(Long boardNum, Board board) {
        mapper.update(boardNum, board);
        return findOne(boardNum);
    }

    @Override
    public Board write(Board board) {
        mapper.write(board);
        return board;
    }

    @Override
    public Board writeRe(Long boardNum, Rewrite rewrite) {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        rewrite.setBoardNum(boardNum);
        rewrite.setReDate(dateFormat.format(date));

        mapper.writeRe(boardNum, rewrite);
        return findOne(boardNum);
    }
    
    
}
