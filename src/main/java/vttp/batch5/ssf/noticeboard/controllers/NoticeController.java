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
import vttp.batch5.ssf.noticeboard.models.HealthStatus;
import vttp.batch5.ssf.noticeboard.models.Notice;
import vttp.batch5.ssf.noticeboard.services.NoticeService;

// Use this class to write your request handlers
@Controller
public class NoticeController {

    @Autowired
    private NoticeService service;

    @GetMapping("/notice")
    public String showNoticeForm(Model model) {
        model.addAttribute("notice", new Notice());
        return "notice";
    }

    @PostMapping("/notice")
    public String submitNotice(@Valid @ModelAttribute Notice notice, BindingResult binding, Model model) {
        if (binding.hasErrors()) {
            return "notice";
        }

        ResponseEntity<String> response = service.postToNoticeServer(notice);

        if (!response.getStatusCode().isError()) {
            model.addAttribute("id", notice.getId());
            return "view2";
        } else {
            model.addAttribute("error", response.getBody());
            return "view3";
        }
    }

    @GetMapping("/status")
    @ResponseBody
    public ResponseEntity<String> getHealthStatus() {
        return service.checkHealth();
    }
}
