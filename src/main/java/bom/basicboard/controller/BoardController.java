package bom.basicboard.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import bom.basicboard.domain.Board;
import bom.basicboard.domain.Member;
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
        if(loginMember == null) {
            return "login";
        }
        String memberId = loginMember.getMemberId();
        String password = loginMember.getPassword();
        String memberName = loginMember.getMemberName();
        Member member = new Member(memberId,password,memberName);
        model.addAttribute("member", member);

        List<Board> boardList = boardService.getBoardList();
        log.info(String.valueOf(boardList.size()));
        model.addAttribute("boardList", boardList);
        
        return "board";
    }
}
