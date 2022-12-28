package bom.basicboard.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import bom.basicboard.domain.Board;
import bom.basicboard.domain.BoardSearchCond;
import bom.basicboard.domain.File;
import bom.basicboard.domain.Rewrite;
import bom.basicboard.repository.BoardRepository;

@Service
public class BoardService {

    private final BoardRepository boardRepository;

    @Autowired
    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    
    public List<Board> getBoardList(BoardSearchCond searchCond) {
        return boardRepository.findAll(searchCond).get();
    }

    public Board getBoard(Long boardNum) {
        return boardRepository.findOne(boardNum);
    }

    public Board saveBoard(Board board) {
        return boardRepository.write(board);
    }

    public Board updateBoard(Long boardNum, Board board) {
        return boardRepository.update(boardNum, board);
    }

    public List<Board> deleteBoard(Long boardNum, BoardSearchCond searchCond) {
        boardRepository.delete(boardNum);
        return boardRepository.findAll(searchCond).get();
    }

    // 댓글
    public Board saveRe(Long boardNum, Rewrite rewrite) {
        return boardRepository.writeRe(boardNum, rewrite);
    }

    public Board deleteRe(Long reId, Long boardNum) {
        boardRepository.deleteRe(boardNum, reId);
        return boardRepository.findOne(boardNum);
    }

    //파일
    public HashMap<Long, File> saveFile(Long boardNum, List<MultipartFile> files) {
        HashMap<Long, File> saveFiles = boardRepository.saveFiles(boardNum, files);

        return saveFiles;
    }
    public File findFile(Long boardNum, Long fileId) {
        return boardRepository.findFile(boardNum, fileId);
    }
    public void initFile(Long boardNum) {
        boardRepository.deleteFile(boardNum);
    }

}
