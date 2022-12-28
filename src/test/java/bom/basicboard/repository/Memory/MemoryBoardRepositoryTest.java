package bom.basicboard.repository.Memory;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import bom.basicboard.domain.Board;
import bom.basicboard.domain.BoardSearchCond;
import bom.basicboard.domain.Rewrite;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MemoryBoardRepositoryTest {

    MemoryBoardRepository boardRepository = new MemoryBoardRepository();

    @AfterEach
    void clear() {
        boardRepository.clearStore();
    }


    @Test
    void testDelete() {
        Board board = new Board("hi","bom","안녕하세요");
        Board saveBoard = boardRepository.write(board);

        boardRepository.delete(saveBoard.getBoardNum());
        Board findOne = boardRepository.findOne(saveBoard.getBoardNum());

        Assertions.assertThat(findOne).isNull();
    }

    @Test
    void testDeleteRe() {
        Board board = new Board("hi","bom","안녕하세요");
        Board savedBoard = boardRepository.write(board);
        
        Rewrite rewrite = new Rewrite("하이루","미역","miyoek");
        boardRepository.writeRe(savedBoard.getBoardNum(), rewrite);

        boardRepository.deleteRe(savedBoard.getBoardNum(), rewrite.getReId());

        Board findBoard = boardRepository.findOne(savedBoard.getBoardNum());


        Assertions.assertThat(findBoard.getReples().size()).isEqualTo(0);


    }

    @Test
    void testUpdate() {
        Board board = new Board("hi","bom","안녕하세요");
        boardRepository.write(board);
        Board uptBoard = new Board("bye","bom","안녕히가세요");
        Board rs = boardRepository.update(board.getBoardNum(), uptBoard);
        Assertions.assertThat(rs.getBoardNum()).isEqualTo(1);
        Assertions.assertThat(rs.getBoardTitle()).isEqualTo("bye");
    }

    @Test
    void testWrite() {
        Board board = new Board("hi","bom","안녕하세요");
        boardRepository.write(board);
        BoardSearchCond searchCond = null;
        Optional<List<Board>> boards = boardRepository.findAll(searchCond);
        Assertions.assertThat(boards.get().size()).isEqualTo(1);
    }

    @Test
    void testWriteRe() {
        Board board = new Board("hi","bom","안녕하세요");
        Board savedBoard = boardRepository.write(board);

        Rewrite rewrite = new Rewrite("하이루","미역","miyoek");
        Rewrite rewrite2 = new Rewrite("하이루","대규","eorb");
        
        boardRepository.writeRe(savedBoard.getBoardNum(), rewrite);
        boardRepository.writeRe(savedBoard.getBoardNum(), rewrite2);

        Assertions.assertThat(savedBoard.getReples().size()).isEqualTo(2);
    }
}
