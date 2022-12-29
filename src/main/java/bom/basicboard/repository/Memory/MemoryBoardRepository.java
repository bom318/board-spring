package bom.basicboard.repository.Memory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import bom.basicboard.domain.Board;
import bom.basicboard.domain.BoardSearchCond;
import bom.basicboard.domain.File;
import bom.basicboard.domain.Rewrite;
import bom.basicboard.repository.BoardRepository;
import bom.basicboard.repository.FileStore;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class MemoryBoardRepository implements BoardRepository {

    private final FileStore fileSave;

    
    @Autowired
    public MemoryBoardRepository(FileStore fileSave) {
        this.fileSave = fileSave;
    }

    Map<Long, Board> boardStore = new HashMap<>();
    Map<Long, Rewrite> reStore = new HashMap<>();
    HashMap<Long, File> fileStore = new HashMap<>();
    Long reId = 0L;
    Long boardId = 0L;
    Long fileId = 0L;


    Date date = new Date();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public Optional<List<Board>> findAll(BoardSearchCond searchCond) {
        String filter = searchCond.getFilter();
        String word = searchCond.getWord();

        List<Board> boardList = boardStore.values().stream().filter(board -> {
            if(!StringUtils.hasText(filter)) {
                return true;
            }else {
                if(filter.equals("title")) {
                    log.info("filter==title call");
                    return board.getBoardTitle().contains(word);
                }else {
                    log.info("filter != title call");
                    return board.getWriter().equals(word);
                }
            }
        }).collect(Collectors.toList());

        return Optional.ofNullable(boardList);
    }

    @Override
    public Board findOne(Long boardNum) {
        // TODO Auto-generated method stub
        return boardStore.get(boardNum);
    }

    @Override
    public Board write(Board board) {

        // 파일

        board.setBoardNum(++boardId);
        boardStore.put(board.getBoardNum(), board);

        return board;
    }

    @Override
    public Board update(Long boardNum, Board board) {

        Board findBoard = findOne(boardNum);

        board.setBoardNum(boardNum);
        board.setBoardDate(findBoard.getBoardDate());
        board.setReples(findBoard.getReples());

        boardStore.replace(boardNum, board);

        return boardStore.get(boardNum);
    }

    @Override
    public void delete(Long boardNum) {
        boardStore.remove(boardNum);

    }


    @Override
    public Board writeRe(Long boardNum, Rewrite rewrite) {

        rewrite.setReId(++reId);
        rewrite.setReDate(dateFormat.format(date));
        rewrite.setBoardNum(boardNum);
        reStore.put(rewrite.getReId(), rewrite);

        Board board = findOne(boardNum);

        HashMap<Long, Rewrite> reples = board.getReples();
        if (reples == null) {
            HashMap<Long, Rewrite> newReples = new HashMap<>();
            newReples.put(rewrite.getReId(), rewrite);
            board.setReples(newReples);
        } else {
            reples.put(rewrite.getReId(), rewrite);
            board.setReples(reples);
        }

        return board;

    }

    @Override
    public void deleteRe(Long boardNum, Long reId) {
        reStore.remove(reId);
        Board board = boardStore.get(boardNum);
        HashMap<Long, Rewrite> reples = board.getReples();
        reples.remove(reId);

    }


    @Override
    public HashMap<Long, File> saveFiles(Long boardNum, List<MultipartFile> multipartFiles) {
        
        List<File> saveFiles = fileSave.saveFiles(boardNum, multipartFiles);
        for (File file : saveFiles) {

            file.setFileId(++fileId);
            fileStore.put(file.getFileId(), file);
            
        }
        return fileStore;
    }


    @Override
    public File findFile(Long boardNum, Long fileId) {
        Board findBoard = findOne(boardNum);
        HashMap<Long, File> files = findBoard.getFiles();

        return files.get(fileId);

    }

    

    @Override
    public void deleteFile(Long boardNum) {
        Board findBoard = findOne(boardNum);
        HashMap<Long, File> files = findBoard.getFiles();
        files.clear();
    }

    public void clearStore() {
        boardStore.clear();
        reStore.clear();
    }

    // init 데이터
    @PostConstruct
    public void init() {
        write(new Board("hi", "bom", "하이루"));
        write(new Board("안녕하세여", "미역", "귀여웡"));
        writeRe(1L, new Rewrite("2222", "미역","miyoek10"));
    }

}
