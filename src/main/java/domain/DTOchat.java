package domain;

import java.time.LocalDateTime;

public class DTOchat {
    int id;
    String userFrom;
    String message;
    LocalDateTime date;
    Message reply;

    public DTOchat(int id, String userFrom, String message, LocalDateTime date, Message reply) {
        this.id = id;
        this.userFrom = userFrom;
        this.message = message;
        this.date = date;
        this.reply = reply;
    }

    public int getId() {
        return id;
    }

    public String getUserFrom() {
        return userFrom;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public Message getReply() {
        return reply;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUserFrom(String userFrom) {
        this.userFrom = userFrom;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public void setReply(Message reply) {
        this.reply = reply;
    }

    @Override
    public String toString() {
        String s = "("+id+")"+userFrom+":";
        if(reply!=null) s = s + "(replying to '"+ reply.getMessage() +"')" +"\n";
            else s += "\n";
        s = s + message;
        return s;
    }
}
