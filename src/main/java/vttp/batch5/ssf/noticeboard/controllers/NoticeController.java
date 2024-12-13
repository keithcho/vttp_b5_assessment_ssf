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
    // POST mapping to handle form submission
    @PostMapping("/notice")
    public String postValidation(@Valid @ModelAttribute Notice notice, BindingResult binding, Model model) {

        // If fields are invalid, return notice.html with error messages displayed
        if (binding.hasErrors()) {
            return "notice";
        }

        // Build JSON payload from received form data
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        
        jsonObjectBuilder.add("title", notice.getTitle());
        jsonObjectBuilder.add("poster", notice.getPoster());
        jsonObjectBuilder.add("postDate", notice.getPostDate().getTime());
        
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        for (String category : notice.getCategories()) {
            jsonArrayBuilder.add(category);
        }
        jsonObjectBuilder.add("categories", jsonArrayBuilder.build());
        jsonObjectBuilder.add("text", notice.getText());

        JsonObject requestPayload = jsonObjectBuilder.build();

        try {
            // Submits JSON payload to server endpoint. If successful, returns view 2
            String response = noticeService.postToNoticeServer(requestPayload);
            model.addAttribute("response", response);
            return "view2";
        } catch (Exception e) {
            // If unsuccessful, redirects to view 3 with an error message displayed
            logger.error(e.toString());
            model.addAttribute("error", e.toString());
            return "view3";
        }
    }

    // Task 6
    // Health status endpoint for Docker container
    @GetMapping(path="/status")
    @ResponseBody
    public ResponseEntity<Object> getHealth() {

        if (noticeService.isRedisHealthy()) {
            return new ResponseEntity<>("", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("", HttpStatus.SERVICE_UNAVAILABLE);
        }
    }
}
