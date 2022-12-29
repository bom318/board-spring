package bom.basicboard.repository.jdbcTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import bom.basicboard.domain.Member;
import bom.basicboard.repository.MemberRepository;
import lombok.AllArgsConstructor;

@Repository
public class JdbcMemberRepository implements MemberRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public JdbcMemberRepository(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("member").usingGeneratedKeyColumns("UUID");
    }

    @Override
    public void delete(Long uuid) {
        String sql = "delete * from member where UUID = :uuid";
        Map<String, Object> param = Map.of("uuid",uuid);
        jdbcTemplate.update(sql, param);

    }

    @Override
    public List<Member> findAll() {
        String sql = "select * from member";
        List<Member> members = jdbcTemplate.query(sql, memberRowMapper());
        return members;
    }

    @Override
    public Optional<Member> findById(Long uuid) {
        String sql = "select * from member where UUID = :uuid";
        try {
            Map<String, Object> param = Map.of("UUID", uuid);
            Member member = jdbcTemplate.queryForObject(sql, param, memberRowMapper());
            return Optional.of(member);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Member> findByMemberId(String memberId) {
        String sql = "select * from member where member_id = :memberId";
        try {
            Map<String, Object> param = Map.of("memberId", memberId);
            Member member = jdbcTemplate.queryForObject(sql, param, memberRowMapper());
            return Optional.of(member);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }

    }

    @Override
    public Member save(Member member) {
        SqlParameterSource param = new BeanPropertySqlParameterSource(member);
        Long key = jdbcInsert.executeAndReturnKey(param).longValue();
        member.setUuid(key);
        return member;
    }

    @Override
    public Member update(Long uuid, String password, String memberName) {
        String sql = "update member set PASSWORD=:password, member_name=:memberName where uuid=:uuid";
        SqlParameterSource param = new MapSqlParameterSource().addValue("password", password).addValue("memberName", memberName).addValue("uuid", uuid);
        jdbcTemplate.update(sql, param);
        Optional<Member> member = findById(uuid);
        return member.get();
    }

    private RowMapper<Member> memberRowMapper() {
        return BeanPropertyRowMapper.newInstance(Member.class);
    }

}
