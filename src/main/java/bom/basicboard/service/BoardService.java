package bom.basicboard.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bom.basicboard.domain.Board;
import bom.basicboard.domain.Rewrite;
import bom.basicboard.repository.BoardRepository;

@Service
public class BoardService {

    private final BoardRepository boardRepository;

    @Autowired
    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    
    public Optional<List<Board>> getBoardList() {
        return boardRepository.findAll();
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

    public Optional<List<Board>> deleteBoard(Long boardNum) {
        boardRepository.delete(boardNum);
        return boardRepository.findAll();
    }

    // 댓글
    public Board saveRe(Long boardNum, Rewrite rewrite) {
        return boardRepository.writeRe(boardNum, rewrite);
    }

    public Board deleteRe(Long reId, Long boardNum) {
        boardRepository.deleteRe(boardNum, reId);
        return boardRepository.findOne(boardNum);
    }

}
