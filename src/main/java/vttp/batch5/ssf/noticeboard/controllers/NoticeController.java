package vttp.batch5.ssf.noticeboard.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.validation.Valid;
import vttp.batch5.ssf.noticeboard.models.Notice;
import vttp.batch5.ssf.noticeboard.services.NoticeService;

// Use this class to write your request handlers

@Controller
public class NoticeController {

    @Autowired
    NoticeService noticeService;

    // Task 1
    @GetMapping("")
    public String getLandingPage(Model model) {
        model.addAttribute("notice", new Notice());
        return "notice";
    }

    // Task 2
    @PostMapping("/notice")
    public String postValidation(@Valid @ModelAttribute Notice notice, BindingResult binding) {

        // If fields are invalid, return notice.html with error messages displayed
        if (binding.hasErrors()) {
            return "notice";
        }

        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        
        jsonObjectBuilder.add("title", notice.getTitle());
        jsonObjectBuilder.add("poster", notice.getPoster());
        jsonObjectBuilder.add("postDate", notice.getPostDate());
        // jsonObjectBuilder.add("categories", notice.getCategories().toString());
        jsonObjectBuilder.add("text", notice.getText());

        JsonObject requestPayload = jsonObjectBuilder.build();

        noticeService.postToNoticeServer(requestPayload);


        return "success";
    }
    

}
