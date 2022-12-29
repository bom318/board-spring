package bom.basicboard.repository.jdbcTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import bom.basicboard.domain.Board;
import bom.basicboard.domain.BoardSearchCond;
import bom.basicboard.domain.File;
import bom.basicboard.domain.Rewrite;
import bom.basicboard.repository.BoardRepository;

@Repository
public class JdbcBoardRepository implements BoardRepository {

    private final NamedParameterJdbcTemplate template;
    private final SimpleJdbcInsert jdbcInsert;

    public JdbcBoardRepository(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("board").usingGeneratedKeyColumns("boardNum");
    }

    @Override
    public void delete(Long boardNum) {
        String sql = "delete * from board where boardNum=:boardNum";
        Map<String, Object> param = Map.of("boardNum", boardNum);
        template.update(sql, param);

    }

    @Override
    public void deleteFile(Long boardNum) {
        // TODO Auto-generated method stub

    }

    @Override
    public void deleteRe(Long boardNum, Long reId) {
        // TODO Auto-generated method stub

    }

    @Override
    public Optional<List<Board>> findAll(BoardSearchCond searchCond) {
        String sql = "select * from board";

        String filter = searchCond.getFilter();
        String word = searchCond.getWord();

        SqlParameterSource param = new BeanPropertySqlParameterSource(searchCond);

        // 동적쿼리
        if (StringUtils.hasText(word)) {
            sql += " where";

            if (filter.equals("title")) {
                sql += " boardTitle like concat('%', :word, '%')";
            }
            if (filter.equals("name")) {
                sql += " writer = :word";
            }
        }

        List<Board> boards = template.query(sql, param, boardRowMapper());


        // 댓글수 저장
        for (Board board : boards) {
            Long boardNum = board.getBoardNum();
            int reNum = getreNum(boardNum);
            board.setReNum(reNum);
        }

        return Optional.ofNullable(boards);

    }

    public int getreNum(Long boardNum) {
        String sql = "select count(*) from rewrite where boardNum=:boardNum";
        Map<String, Object> param = Map.of("boardNum", boardNum);
        return template.queryForObject(sql, param, Integer.class);
    }

    @Override
    public File findFile(Long boardNum, Long fileId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Board findOne(Long boardNum) {
        String sql = "select * from board where boardNum=:boardNum";
        Map<String, Object> param = Map.of("boardNum", boardNum);
        Board board = template.queryForObject(sql, param, boardRowMapper());
        return board;
    }

    @Override
    public HashMap<Long, File> saveFiles(Long boardNum, List<MultipartFile> multipartFiles) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Board update(Long boardNum, Board board) {
        String sql = "update board set boardTitle=:boardTitle, content=:content where boardNum=:boardNum";
        String boardTitle = board.getBoardTitle();
        String content = board.getContent();
        SqlParameterSource param = new MapSqlParameterSource().addValue("boardTitle", boardTitle).addValue("content", content).addValue("boardNum", boardNum);
        template.update(sql, param);
        return board;
    }

    @Override
    public Board write(Board board) {
        SqlParameterSource param = new BeanPropertySqlParameterSource(board);
        Long key = jdbcInsert.executeAndReturnKey(param).longValue();
        board.setBoardNum(key);

        return board;
    }

    @Override
    public Board writeRe(Long boardNum, Rewrite rewrite) {
        // TODO Auto-generated method stub
        return null;
    }

    private RowMapper<Board> boardRowMapper() {
        return BeanPropertyRowMapper.newInstance(Board.class);
    }

}
