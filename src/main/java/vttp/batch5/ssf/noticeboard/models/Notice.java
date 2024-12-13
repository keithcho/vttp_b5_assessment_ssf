package vttp.batch5.ssf.noticeboard.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class Notice {

    @NotBlank
    @Size(min=3, max=128, message="Title length must be between 3 and 128 characters.")
    private String title;        // Notice title

    @NotBlank
    @Email
    private String poster;       // Notice poster's email

    @NotNull
    private Long postDate;       // Post date in epoch time

    @NotEmpty
    private String[] categories; // List of categories

    @NotBlank
    private String text;         // Notice text
    
    public Notice() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public Long getPostDate() {
        return postDate;
    }

    public void setPostDate(Long postDate) {
        this.postDate = postDate;
    }

    public String[] getCategories() {
        return categories;
    }

    public void setCategories(String[] categories) {
        this.categories = categories;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    
}
