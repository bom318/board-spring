package bom.basicboard.repository.mybatis;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import bom.basicboard.domain.Member;
import bom.basicboard.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MyBatisMemberRepository implements MemberRepository {
    private final MemberMapper mapper;

    @Override
    public void delete(Long uuid) {
        mapper.delete(uuid);
        
    }

    @Override
    public List<Member> findAll() {
        return mapper.findAll();
    }

    @Override
    public Optional<Member> findById(Long uuid) {
        return mapper.findById(uuid);
    }

    @Override
    public Optional<Member> findByMemberId(String memberId) {
        return mapper.findByMemberId(memberId);
    }

    @Override
    public Member save(Member member) {
        mapper.save(member);
        return member;
    }

    @Override
    public Member update(Long uuid, String password, String memberName) {
        mapper.update(uuid, password, memberName);
        return mapper.findById(uuid).get();
    }

    
}
