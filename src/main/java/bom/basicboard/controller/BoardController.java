package bom.basicboard.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriUtils;

import bom.basicboard.domain.Board;
import bom.basicboard.domain.BoardForm;
import bom.basicboard.domain.File;
import bom.basicboard.domain.Member;
import bom.basicboard.domain.Rewrite;
import bom.basicboard.repository.BoardRepository;
import bom.basicboard.service.BoardService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@AllArgsConstructor
@Slf4j
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/board")
    public String board(@SessionAttribute(name = "loginMember", required = false) Member loginMember, Model model) {
        if (loginMember == null) {
            return "login";
        }
        String memberId = loginMember.getMemberId();
        String password = loginMember.getPassword();
        String memberName = loginMember.getMemberName();
        Member member = new Member(memberId, password, memberName);
        model.addAttribute("member", member);

        List<Board> boardList = boardService.getBoardList();
        model.addAttribute("boardList", boardList);

        return "board";
    }

    @GetMapping("/write")
    public String write(@SessionAttribute(name = "loginMember", required = false) Member loginMember, Model model) {
        model.addAttribute("member", loginMember);

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formatDate = dateFormat.format(date);
        model.addAttribute("date", formatDate);

        return "writeForm";
    }

    @PostMapping("/write")
    public String write(@SessionAttribute(name = "loginMember", required = false) Member loginMember, @ModelAttribute BoardForm form, RedirectAttributes redirectAttributes) {
        HashMap<Long, File> files = boardService.saveFile(form.getBoardNum(), form.getFiles());
        Board newBoard = new Board();
        newBoard.setMemberId(loginMember.getMemberId());
        newBoard.setBoardTitle(form.getBoardTitle());
        newBoard.setContent(form.getContent());
        newBoard.setDate(form.getDate());
        newBoard.setWriter(form.getWriter());
        newBoard.setFiles(files);
        Board saveBoard = boardService.saveBoard(newBoard);

        redirectAttributes.addAttribute("boardNum", saveBoard.getBoardNum());

        return "redirect:/boardDetail/{boardNum}";

    }

    @GetMapping("/boardDetail/{boardNum}")
    public String detail(@PathVariable Long boardNum, Model model,
            @SessionAttribute(name = "loginMember", required = false) Member loginMember) {
        Board board = boardService.getBoard(boardNum);
        model.addAttribute("board", board);
        model.addAttribute("member", loginMember);
        return "boardDetail";
    }

    @GetMapping("/boardDetail/{boardNum}/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long boardNum, @PathVariable Long fileId)
            throws MalformedURLException {
        File findFile = boardService.findFile(boardNum, fileId);
        String storeFileName = findFile.getStoreFileName();
        String uploadFileName = findFile.getUploadFileName();

        UrlResource resource = new UrlResource("file:c://boardSpringFiles/" + storeFileName);

        String encodeUploadFileName = UriUtils.encode(uploadFileName, StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" + encodeUploadFileName + "\"";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(resource);
    }

    @GetMapping("/update/{boardNum}")
    public String update(@PathVariable Long boardNum, Model model) {
        Board findBoard = boardService.getBoard(boardNum);
        model.addAttribute("board", findBoard);
        return "editForm";
    }

    @PostMapping("/update/{boardNum}")
    public String update(@PathVariable Long boardNum, @ModelAttribute BoardForm board,
            RedirectAttributes redirectAttributes) {

        boardService.initFile(boardNum);

        HashMap<Long, File> files = boardService.saveFile(boardNum, board.getFiles());
        Board newBoard = new Board();
        newBoard.setBoardTitle(board.getBoardTitle());
        newBoard.setContent(board.getContent());
        newBoard.setDate(board.getDate());
        newBoard.setWriter(board.getWriter());
        newBoard.setFiles(files);
        boardService.updateBoard(boardNum, newBoard);

        redirectAttributes.addAttribute("boardNum", boardNum);
        return "redirect:/boardDetail/{boardNum}";
    }

    @GetMapping("/delete/{boardNum}")
    public String delete(@PathVariable Long boardNum) {
        boardService.deleteBoard(boardNum);
        return "redirect:/board";
    }

    @GetMapping("/boardDetail/{boardNum}/getRepl")
    @ResponseBody
    public Collection<Rewrite> getRepl(@PathVariable Long boardNum) {
        Board board = boardService.getBoard(boardNum);
        HashMap<Long, Rewrite> reples = board.getReples();
        return reples.values();
    }

    @PostMapping("/boardDetail/{boardNum}/saveRepl")
    @ResponseBody
    public Rewrite saveRepl(@PathVariable Long boardNum, @RequestParam(name = "reContent") String reContent,
    @SessionAttribute(name = "loginMember", required = false) Member loginMember) {

        
        Rewrite repl = new Rewrite(reContent,loginMember.getMemberName(),loginMember.getMemberId());
        boardService.saveRe(boardNum, repl);

        //redirectAttributes.addAttribute("boardNum", boardNum);

        
        return repl;
    }

    @PostMapping("/boardDetail/{boardNum}/deleteRe/{reId}")
    public String deleteRe(@PathVariable Long boardNum, @PathVariable Long reId, RedirectAttributes redirectAttributes ) {
        boardService.deleteRe(reId, boardNum);

        redirectAttributes.addAttribute("boardNum",boardNum);

        return "redirect:/boardDetail/{boardNum}";
    }
}
