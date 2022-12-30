package bom.basicboard.repository.mybatis;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import bom.basicboard.domain.Member;

@Mapper
public interface MemberMapper {
    void save(Member member);
    void update(@Param("UUID") Long uuid,@Param("PASSWORD") String password,@Param("memberName") String memberName);
    Optional<Member> findById(Long uuid);
    List<Member> findAll();
    Optional<Member> findByMemberId(String memberId);
    void delete(Long uuid);
}
