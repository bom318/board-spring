package bom.basicboard.repository.JDBC;

import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import bom.basicboard.domain.Member;
import bom.basicboard.repository.MemberRepository;

@Repository
public class JdbcMemberRepository implements MemberRepository{
    private final JdbcTemplate jdbcTemplate;

    public JdbcMemberRepository(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void delete(Long uuid) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public List<Member> findAll() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<Member> findById(Long uuid) {
        // TODO Auto-generated method stub
        return Optional.empty();
    }

    @Override
    public Optional<Member> findByMemberId(String memberId) {
        // TODO Auto-generated method stub
        return Optional.ofNullable(jdbcTemplate.queryForObject("select * from member where memberId = ?",memberRowMapper(),memberId));
        
    }
    private RowMapper<Member> memberRowMapper() {
        return (rs, rowNum) -> {
            Member member = new Member();
            member.setMemberId(rs.getString("memberId"));
            member.setMemberName(rs.getString("name"));
            member.setPassword(rs.getString("password"));
            member.setUuid(rs.getLong("UUID"));
            return member;
        };
    }

    @Override
    public Member save(Member member) {
        // TODO Auto-generated method stub
        jdbcTemplate.update("INSERT INTO member (memberId,PASSWORD,NAME) VALUES (?,?,?)", member.getMemberId(),member.getPassword(),member.getMemberName());
        return member;
    }

    @Override
    public Member update(Long uuid, String password, String memberName) {
        // TODO Auto-generated method stub
        return null;
    }
    
    
}
