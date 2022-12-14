package bom.basicboard.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import bom.basicboard.domain.Member;
import bom.basicboard.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@AllArgsConstructor
public class MemberController {
    
    private final MemberService memberService;

    @GetMapping
    public String login() {
        return "login";
    }
    
    @PostMapping("/login")
    public String login(@RequestParam String memberId, @RequestParam String password, Model model, HttpServletRequest request) {

        Member loginMember = memberService.login(memberId, password);
        if(loginMember == null) {
            return "login";
        }

        //세션
        HttpSession session = request.getSession();
        session.setAttribute("loginMember", loginMember);

        model.addAttribute("member", loginMember);
        return "redirect:/board";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.removeAttribute("loginMember");
        return "/login";
    }

    @GetMapping("/join")
    public String joinForm() {
        return "join";
    }

    @PostMapping("/join")
    public String join(@Validated@ModelAttribute Member member,
                        BindingResult bindingResult) {
        
        //서버 유효성 검사
        if(bindingResult.hasErrors()) {
            log.info("errors={}",bindingResult);
            return "join";
        }
        //비밀번호 암호화
        

        Member result = new Member(member.getMemberId(), member.getPassword(), member.getMemberName());
        memberService.join(result);

        return "redirect:/";
    }


}
