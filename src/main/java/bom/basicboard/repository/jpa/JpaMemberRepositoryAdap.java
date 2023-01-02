package bom.basicboard.repository.jpa;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import bom.basicboard.domain.Member;
import bom.basicboard.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@Repository
@Transactional
@RequiredArgsConstructor
public class JpaMemberRepositoryAdap implements MemberRepository{

    private final JpaMemberRepository repository;

    @Override
    public void delete(Long uuid) {
        repository.deleteById(uuid);
        
    }

    @Override
    public List<Member> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Member> findById(Long uuid) {
        return repository.findById(uuid);
    }

    @Override
    public Optional<Member> findByMemberId(String memberId) {
        return repository.findByMemberId(memberId);
    }

    @Override
    public Member save(Member member) {
        return repository.save(member);
    }

    @Override
    public Member update(Long uuid, String password, String memberName) {
        Member findMember = findById(uuid).get();
        findMember.setMemberName(memberName);
        findMember.setPassword(password);
        return findMember;
    }
    
}
