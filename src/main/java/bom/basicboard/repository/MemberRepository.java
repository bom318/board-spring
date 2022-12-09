package bom.basicboard.repository;

import java.util.List;
import java.util.Optional;

import bom.basicboard.domain.Member;

public interface MemberRepository {
    
    public Member save(Member member);
    public Member update(Long uuid,String password,String memberName);
    public Optional<Member> findById(Long uuid);
    public List<Member> findAll();
    public Optional<Member> findByMemberId(String memberId);
    public void delete(Long uuid);
}
