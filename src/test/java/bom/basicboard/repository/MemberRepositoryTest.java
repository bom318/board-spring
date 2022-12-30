package bom.basicboard.repository;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import bom.basicboard.domain.Member;
import bom.basicboard.repository.MemberRepository;
import bom.basicboard.repository.Memory.MemoryMemberRepository;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@Slf4j
public class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;

    @AfterEach
    void afterEach() {
        if (memberRepository instanceof MemoryMemberRepository) {
            ((MemoryMemberRepository) memberRepository).clear();
        }
    }

    @Test
    void save() {
        //given
        Member member = new Member("bomboms11","qwe123!@#","보미");
        //when
        Member saveMember = memberRepository.save(member);
        //then
        Member findMember = memberRepository.findByMemberId(saveMember.getMemberId()).get();
        log.info(saveMember.toString());
        log.info(findMember.toString());
        Assertions.assertThat(findMember.getPassword()).isEqualTo(member.getPassword());
    }
}
