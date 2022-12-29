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
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import bom.basicboard.domain.Board;
import bom.basicboard.domain.BoardSearchCond;
import bom.basicboard.domain.File;
import bom.basicboard.domain.Rewrite;
import bom.basicboard.repository.BoardRepository;
import bom.basicboard.repository.FileStore;
import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class JdbcBoardRepository implements BoardRepository {

    private final NamedParameterJdbcTemplate template;
    private final SimpleJdbcInsert jdbcInsert;
    private final FileStore fileStore;

    public JdbcBoardRepository(DataSource dataSource, FileStore filestore) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("board").usingGeneratedKeyColumns("boardNum");
        this.fileStore = filestore;
    }

    @Override
    public void delete(Long boardNum) {
        String sql = "delete from board where boardNum=:boardNum";
        Map<String, Object> param = Map.of("boardNum", boardNum);
        template.update(sql, param);

    }

    @Override
    public void deleteFile(Long boardNum) {
        String sql = "delete from file where boardNum=:boardNum";
        Map<String, Object> param = Map.of("boardNum", boardNum);
        template.update(sql, param);

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

    public List<File> getFiles(Long boardNum) {
        String sql = "select * from file where boardNum=:boardNum";
        Map<String, Object> param = Map.of("boardNum", boardNum);
        List<File> files = template.query(sql, param, fileRowMapper());
        return files;
    }

    @Override
    public File findFile(Long boardNum, Long fileId) {
        String sql = "select * from file where fileId=:fileId";
        Map<String, Object> param = Map.of("fileId", fileId);
        File file = template.queryForObject(sql, param, fileRowMapper());
        return file;
    }

    @Override
    public Board findOne(Long boardNum) {
        String sql = "select * from board where boardNum=:boardNum";
        Map<String, Object> param = Map.of("boardNum", boardNum);

        Board board = template.queryForObject(sql, param, boardRowMapper());

        // 파일
        List<File> files = getFiles(boardNum);
        log.info("files size={}",String.valueOf(files.size()));
        if (files.size()>0) {

            HashMap<Long, File> fileMap = new HashMap<>();
            for (File file : files) {
                log.info("uploadName={}", file.getUploadFileName());
                fileMap.put(file.getFileId(), file);
            }
            board.setFiles(fileMap);
        }

        
        return board;
    }

    @Override
    public HashMap<Long, File> saveFiles(Long boardNum, List<MultipartFile> multipartFiles) {
        HashMap<Long, File> files = new HashMap<>();
        List<File> saveFiles = fileStore.saveFiles(boardNum, multipartFiles);
        if(saveFiles == null) {
            return null;
        }
        String sql = "insert into file (boardNum, uploadFileName, storeFileName) values (:boardNum, :uploadFileName, :storeFileName)";
        for (File file : saveFiles) {
            String storeFileName = file.getStoreFileName();
            String uploadFileName = file.getUploadFileName();
            log.info("storedName={}", storeFileName);
            log.info("uploadName={}", uploadFileName);
            log.info("boardNum={}", boardNum);
            log.info("getboardNum={}", file.getBoardNum());
            SqlParameterSource param = new MapSqlParameterSource()
                    .addValue("uploadFileName", uploadFileName)
                    .addValue("storeFileName", storeFileName)
                    .addValue("boardNum", boardNum);
            KeyHolder keyHolder = new GeneratedKeyHolder();

            template.update(sql, param, keyHolder);
            long key = keyHolder.getKey().longValue();
            files.put(key, file);
        }
        return files;
    }

    @Override
    public Board update(Long boardNum, Board board) {
        String sql = "update board set boardTitle=:boardTitle, content=:content where boardNum=:boardNum";
        String boardTitle = board.getBoardTitle();
        String content = board.getContent();
        SqlParameterSource param = new MapSqlParameterSource().addValue("boardTitle", boardTitle)
                .addValue("content", content).addValue("boardNum", boardNum);
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

    private RowMapper<File> fileRowMapper() {
        return BeanPropertyRowMapper.newInstance(File.class);
    }

}
