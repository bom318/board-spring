package bom.basicboard.repository;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import bom.basicboard.domain.Board;
import bom.basicboard.domain.Member;
import bom.basicboard.repository.Memory.MemoryBoardRepository;
import bom.basicboard.repository.Memory.MemoryMemberRepository;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@Slf4j
public class BoardRepositoryTest {

    @Autowired
    BoardRepository boardRepository;
    @Autowired
    MemberRepository memberRepository;

    @AfterEach
    void afterEach() {
        if(boardRepository instanceof MemoryBoardRepository) {
            ((MemoryBoardRepository) boardRepository).clearStore();
        }
        if (memberRepository instanceof MemoryMemberRepository) {
            ((MemoryMemberRepository) memberRepository).clear();
        }
    }


    @Test
    void write() {
        Member member = new Member("bomboms11", "qwe123!@#", "bom");
        memberRepository.save(member);
        Board board = new Board("안녕하세요","boms","반갑습니다");
        board.setMemberId("bomboms11");
        board.setBoardDate("2022-12-30");

        Board saveBoard = boardRepository.write(board);
        Board findBoard = boardRepository.findOne(saveBoard.getBoardNum());

        Assertions.assertThat(findBoard.getBoardTitle()).isEqualTo("안녕하세요");

    }

    @Test
    void update() {
        Member member = new Member("bomboms11", "qwe123!@#", "bom");
        memberRepository.save(member);
        Board board = new Board("안녕하세요","boms","반갑습니다");
        board.setMemberId("bomboms11");
        board.setBoardDate("2022-12-30");

        Board saveBoard = boardRepository.write(board);

        Board updateParam = new Board("하이", "boms", "반갑습니다");

        boardRepository.update(saveBoard.getBoardNum(), updateParam);

        Board newBoard = boardRepository.findOne(saveBoard.getBoardNum());

        Assertions.assertThat(newBoard.getBoardTitle()).isEqualTo("하이");
    }

    @Test
    void delete() {
        Member member = new Member("bomboms11", "qwe123!@#", "bom");
        memberRepository.save(member);
        Board board = new Board("안녕하세요","boms","반갑습니다");
        board.setMemberId("bomboms11");
        board.setBoardDate("2022-12-30");

        Board saveBoard = boardRepository.write(board);

        boardRepository.delete(saveBoard.getBoardNum());
        List<Board> boards = boardRepository.findAll(null).get();
        Assertions.assertThat(boards.size()).isEqualTo(0);

    }

    
}
