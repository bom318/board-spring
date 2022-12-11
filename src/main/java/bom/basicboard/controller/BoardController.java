package bom.basicboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import bom.basicboard.domain.Member;

@Controller
public class BoardController {
    
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
        return "board";
    }
}
