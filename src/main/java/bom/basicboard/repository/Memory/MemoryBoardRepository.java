package bom.basicboard.repository.Memory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import bom.basicboard.domain.Board;
import bom.basicboard.domain.Rewrite;
import bom.basicboard.repository.BoardRepository;

@Repository
public class MemoryBoardRepository implements BoardRepository {

    Map<Long, Board> boardStore = new HashMap<>();
    Map<Long, Rewrite> reStore = new HashMap<>();
    Long reId = 0L;
    Long boardId = 0L;

    Date date = new Date();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public Optional<List<Board>> findAll() {

        return Optional.ofNullable(new ArrayList<>(boardStore.values()));
    }

    @Override
    public Board findOne(Long boardNum) {
        // TODO Auto-generated method stub
        return boardStore.get(boardNum);
    }

    @Override
    public Board write(Board board) {

        // 파일

        board.setDate(dateFormat.format(date));
        board.setBoardNum(++boardId);
        boardStore.put(board.getBoardNum(), board);

        return board;
    }

    @Override
    public Board update(Long boardNum, Board board) {

        Board findBoard = findOne(boardNum);

        board.setBoardNum(boardNum);
        board.setDate(findBoard.getDate());
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

    public void clearStore() {
        boardStore.clear();
        reStore.clear();
    }

}
