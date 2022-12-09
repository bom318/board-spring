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
