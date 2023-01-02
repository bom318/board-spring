package bom.basicboard.repository.jpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import bom.basicboard.domain.Member;

public interface JpaMemberRepository extends JpaRepository<Member,Long> {
    Optional<Member> findByMemberId(String memberId);
}
