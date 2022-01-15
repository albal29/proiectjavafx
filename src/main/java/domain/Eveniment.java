package domain;

import java.time.LocalDateTime;
import java.util.List;

public class Eveniment extends Entity<Integer> {
    private int eventID;
    private String title;
    private String description;
    private User creator;
    private List<User> participants;
    private LocalDateTime eventDate;
    private String location;

    public Eveniment(int eventID, String title, String description, User creator, List<User> participant, LocalDateTime eventDate, String location) {
        this.eventID = eventID;
        this.title = title;
        this.description = description;
        this.creator = creator;
        this.participants = participant;
        this.eventDate = eventDate;
        this.location = location;
    }

    public Eveniment(){

    }

    public Eveniment(String title, String description, User creator, LocalDateTime eventDate, String location) {
        this.title = title;
        this.description = description;
        this.creator = creator;
        this.eventDate = eventDate;
        this.location = location;
    }

    public int getEventID() {
        return eventID;
    }

    public void setEventID(int eventID) {
        this.eventID = eventID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public List<User> getParticipants() {
        return participants;
    }

    public void setParticipants(List<User> participants) {
        this.participants = participants;
    }

    public LocalDateTime getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDateTime eventDate) {
        this.eventDate = eventDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "Eveniment{" +
                "eventID=" + eventID +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", creator=" + creator +
                ", participants=" + participants +
                ", eventDate=" + eventDate +
                ", location='" + location + '\'' +
                '}';
    }
}
