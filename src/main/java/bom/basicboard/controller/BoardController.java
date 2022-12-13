package bom.basicboard.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import bom.basicboard.domain.Board;
import bom.basicboard.domain.BoardForm;
import bom.basicboard.domain.File;
import bom.basicboard.domain.Member;
import bom.basicboard.repository.FileStore;
import bom.basicboard.service.BoardService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@AllArgsConstructor
@Slf4j
public class BoardController {

    private final BoardService boardService;
    private final FileStore fileStore;

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
    public String write(@ModelAttribute BoardForm form, RedirectAttributes redirectAttributes) {
        List<File> files = fileStore.storeFiles(form.getBoardNum(), form.getFiles());
        Board newBoard = new Board();
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

}
