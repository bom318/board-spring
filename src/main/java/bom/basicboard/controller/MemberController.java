package bom.basicboard.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import bom.basicboard.domain.Member;
import bom.basicboard.repository.MemberRepository;
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

    @GetMapping("/join")
    public String joinForm() {
        return "join";
    }

    @PostMapping("/join")
    public String join(@Validated@RequestParam("memberId") String memberId, 
                        @Validated@RequestParam String password,
                        @RequestParam String memberName, BindingResult bindingResult) {
        
        //서버 유효성 검사
        if(bindingResult.hasErrors()) {
            log.info("errors={}",bindingResult);
            return "join";
        }

        Member member = new Member(memberId, password, memberName);
        memberService.join(member);

        return "redirect:/";
    }


}
