package vttp.batch5.ssf.noticeboard.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
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

    Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    // Task 1
    @GetMapping(path={"/", "/notice"})
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

        Long postDate = notice.getPostDate().getTime();

        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        
        jsonObjectBuilder.add("title", notice.getTitle());
        jsonObjectBuilder.add("poster", notice.getPoster());
        jsonObjectBuilder.add("postDate", postDate);
        
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        for (String category : notice.getCategories()) {
            jsonArrayBuilder.add(category);
        }
        jsonObjectBuilder.add("categories", jsonArrayBuilder.build());
        jsonObjectBuilder.add("text", notice.getText());

        JsonObject requestPayload = jsonObjectBuilder.build();

        
        try {
            JsonObject response = noticeService.postToNoticeServer(requestPayload);
        } catch (Exception e) {
            logger.error(e.toString());
            return "view3";
        }

        return "success";
    }

    @GetMapping(path="/status")
    @ResponseBody
    public ResponseEntity<Object> getHealth() {
        return new ResponseEntity<Object>("", HttpStatus.OK);
    }
    

}
