package vttp.batch5.ssf.noticeboard.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.validation.Valid;
import vttp.batch5.ssf.noticeboard.models.Notice;
import vttp.batch5.ssf.noticeboard.services.NoticeService;

// Use this class to write your request handlers
@Controller
@RequestMapping("/notice")
public class NoticeController {
    @Autowired private NoticeService service;

    @GetMapping("")
    public String showLandingPage( Model model){
        Notice notice = new Notice();
        model.addAttribute("notice", notice);
        return "notice";
    }

    @PostMapping("")
    public String postForm(@Valid @ModelAttribute Notice notice, BindingResult result, Model model){
        if (result.hasErrors()){
            model.addAttribute("notice", notice);
            return "notice";
        }
        
        ResponseEntity<String> resp = service.postToNoticeServer(notice);
        if(resp.getStatusCode().is2xxSuccessful()){
            model.addAttribute("id", notice.getId());
            return "view2";
        } else {
            model.addAttribute("error", resp.getBody());
            return "view3";
        }
    }

}
