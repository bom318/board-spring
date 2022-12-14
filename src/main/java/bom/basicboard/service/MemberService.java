package bom.basicboard.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bom.basicboard.domain.Member;
import bom.basicboard.repository.MemberRepository;
import bom.basicboard.repository.Memory.MemoryMemberRepository;

@Service
public class MemberService {

    MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void join(Member member) {
        // 만약 이름 입력 안하면 이름에 아이디 저장
        if (member.getMemberName() == null || member.getMemberName() == "") {
            member.setMemberName(member.getMemberId());
        }
        // 중복회원 검사
        Optional<Member> findMember = memberRepository.findByMemberId(member.getMemberId());
        findMember.ifPresent(m -> {
            throw new IllegalStateException("이미 존재하는 회원입니다");
        });
        memberRepository.save(member);
    }

    public Member updateMember(Member member, String password, String memberName) {
        Member updateMember = memberRepository.update(member.getUuid(), password, memberName);
        return updateMember;
    }

    public void deleteMember(Member member) {
        memberRepository.delete(member.getUuid());
    }

    public Member login(String memberId, String password) {
        return memberRepository.findByMemberId(memberId).filter(m -> m.getPassword().equals(password)).orElse(null);
    }
}
