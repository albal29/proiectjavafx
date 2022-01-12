package domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class Friendship extends Entity<Tuple<Long, Long>> {

    LocalDateTime date;
    String statut;

    public Friendship(Long id1, Long id2) {
        this.setId(new Tuple<>(id1, id2));
        this.date = LocalDateTime.now();
        this.statut = "Pending";
    }

    public Friendship(Long id1, Long id2, LocalDateTime time, String statut) {
        this.setId(new Tuple<>(id1, id2));
        this.date = time;
        this.statut = statut;
    }

    public Friendship(Long id1, Long id2, String statut) {
        this.setId(new Tuple<>(id1, id2));
        this.date = LocalDateTime.now();
        this.statut = statut;
    }

    public Friendship() {

    }

    /**
     * @return the date when the friendship was created
     */
    public LocalDateTime getDate() {
        return date;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Friendship that = (Friendship) o;
        return Objects.equals(this.getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }
}
