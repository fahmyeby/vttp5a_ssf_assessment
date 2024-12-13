package vttp.batch5.ssf.noticeboard.models;

import java.util.Arrays;
import java.util.Date;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class Notice {
    @NotEmpty(message = "Title field cannot be empty")
    @Size(message = "Title length must be between 3 and 128 characters")
    private String title;

    @NotEmpty(message = "Poster field cannot be empty")
    @Email(message = "Input does not conform to email format")
    private String poster;

    @NotEmpty(message = "Text field cannot be empty")
    private String text;

    @Future(message = "Date must be in the future, and never in the past or present")
    private Long postDate;

    @NotEmpty(message = "Category selection cannot be empty, at least one category must be selected")
    private String[] categories;

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Notice() {
    }

    public Notice(String id, String title, String poster, String text, Long postDate, String[] categories) {
        this.id = id;
        this.title = title;
        this.poster = poster;
        this.text = text;
        this.postDate = postDate;
        this.categories = categories;
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
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
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
    @Override
    public String toString() {
        return  title + ", " + poster + ", " + text + ", " + postDate
                + ", " + Arrays.toString(categories);
    }

    
    
}
