package bom.basicboard.repository.Memory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Repository;

import bom.basicboard.domain.Member;
import bom.basicboard.repository.MemberRepository;

//@Repository
public class MemoryMemberRepository implements MemberRepository{

    Map<Long, Member> store = new HashMap<>();
    Long sequence = 0L;

    @Override
    public Member save(Member member) {
        member.setUuid(++sequence);
        store.put(member.getUuid(), member);
        return member;
        
    }

    @Override
    public Member update(Long uuid,String password, String memberName) {
        Member member = store.get(uuid);
        member.setPassword(password);
        member.setMemberName(memberName);
        return member;
    }

    @Override
    public Optional<Member> findById(Long uuid) {
        return Optional.ofNullable(store.get(uuid));
    }
    
    

    @Override
    public List<Member> findAll() {
        return new ArrayList<>(store.values());
    }

    

    @Override
    public Optional<Member> findByMemberId(String memberId) {
        return findAll().stream().filter(m -> m.getMemberId().equals(memberId)).findFirst();
    }

    @Override
    public void delete(Long uuid) {
        store.remove(uuid);
        
    }

    public void clear() {
        store.clear();
    }

    @PostConstruct
    public void init() {
        save(new Member("qweqweqwe", "qwe123!@#", "보미"));
    }
    
}
