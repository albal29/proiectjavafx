package domain;

import java.time.LocalDateTime;

public class FriendRequest {
    private final String firstName;
    private final String lastName;
    private final LocalDateTime data;
    private final String statut;
    private final Tuple<Long, Long> id;

    public FriendRequest(String firstName, String lastName, LocalDateTime data, String statut, Tuple<Long, Long> id) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.data = data;
        this.statut = statut;
        this.id = id;
    }

    public Tuple<Long, Long> getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDateTime getData() {
        return data;
    }

    public String getStatut() {
        return statut;
    }
}
