package domain;

import java.time.LocalDateTime;
import java.util.List;

public class Message extends Entity<Integer>{
    User from;
    List<User> to;
    LocalDateTime data;
    String message;
    Message reply;

    public Message(User from, List<User> to, String message, Message reply) {
        this.from = from;
        this.to = to;
        this.data = LocalDateTime.now();
        this.message = message;
        this.reply = reply;
    }
    public Message(int id,User from, List<User> to, LocalDateTime data, String message, Message reply) {
        this.from = from;
        this.to = to;
        this.data = data;
        this.message = message;
        this.reply = reply;
        this.setId(id);
    }
    public Message(){

    }

    public User getFrom() {
        return from;
    }

    public void setFrom(User from) {
        this.from = from;
    }

    public List<User> getTo() {
        return to;
    }

    public void setTo(List<User> to) {
        this.to = to;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Message getReply() {
        return reply;
    }

    public void setReply(Message reply) {
        this.reply = reply;
    }
}
