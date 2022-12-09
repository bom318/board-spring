package bom.basicboard.repository.Memory;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import bom.basicboard.domain.Member;

public class MemoryMemberRepositoryTest {

    MemoryMemberRepository memberRepository = new MemoryMemberRepository();

    @AfterEach
    void clearStore() {
        memberRepository.clear();
    }


    @Test
    void testSave() {
        Member member = new Member("userA", "1234","bom");
        Member savedMember = memberRepository.save(member);
        Member findMember = memberRepository.findById(savedMember.getUuid()).get();
        Assertions.assertThat(findMember).isEqualTo(savedMember);
    }

    @Test
    void testUpdate() {
        Member member = new Member("userA", "1234","bom");
        Member savedMember = memberRepository.save(member);

        Member updateMember = memberRepository.update(savedMember.getUuid(), "0000", "null");

        Assertions.assertThat(updateMember.getMemberName()).isEqualTo("null");

    }

    @Test
    void testDelete() {
        Member member = new Member("userA", "1234","bom");
        Member savedMember = memberRepository.save(member);

        memberRepository.delete(savedMember.getUuid());
        Optional<Member> result = memberRepository.findById(savedMember.getUuid());

        Assertions.assertThat(result).isEmpty();
    }
}
