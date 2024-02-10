package rs.ac.singidunum.isa.payload.response;

public class CommentResponse {

    private Long id;

    private Long article_id;

    private String text;

    private String username;

    public CommentResponse(Long id, Long article_id, String text, String username) {
        this.id = id;
        this.article_id = article_id;
        this.text = text;
        this.username = username;
    }

    public CommentResponse() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getArticle_id() {
        return article_id;
    }

    public void setArticle_id(Long article_id) {
        this.article_id = article_id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
