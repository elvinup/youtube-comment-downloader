public class Comment {

    private String cid;
    private String text;
    private String time;
    private String author;
    private int likes;

    Comment(String cid, String text, String time, String author, int likes) {
        this.cid = cid;
        this.text = text;
        this.time = time;
        this.author = author;
        this.likes = likes;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }
}
