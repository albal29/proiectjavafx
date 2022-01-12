package domain;

import java.time.LocalDateTime;

public class FriendRequest {
    private String firstName;
    private String lastName;
    private LocalDateTime date;
    private String statut;
    private Tuple<Long, Long> id;

    public FriendRequest(String firstName, String lastName, LocalDateTime date, String statut, Tuple<Long, Long> id) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.date = date;
        this.statut = statut;
        this.id=id;
    }

    public Tuple<Long, Long>getId(){
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getStatut() {
        return statut;
    }
}
