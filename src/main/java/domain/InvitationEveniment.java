package domain;

public class InvitationEveniment extends Entity<Integer> {
    private User creator;
    private int eventID;
    private User invitee;
    private String replyInvite;
    private int idInv;

    public InvitationEveniment(){

    }

    public InvitationEveniment(User creator, int eventID, User invitee) {
        this.creator = creator;
        this.eventID = eventID;
        this.invitee = invitee;
        this.replyInvite = "Pending";
    }

    public InvitationEveniment(User creator, int eventID, User invitee, String reply) {
        this.creator = creator;
        this.eventID = eventID;
        this.invitee = invitee;
        this.replyInvite = reply;
    }

    public InvitationEveniment(int id, User creator, int eventID, User invitee, String replyInvite) {
        this.creator = creator;
        this.eventID = eventID;
        this.invitee = invitee;
        this.replyInvite = replyInvite;
        this.idInv = id;
    }

    public int getIdInv() {
        return idInv;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public int getEventID() {
        return eventID;
    }

    public void setEventID(int eventID) {
        this.eventID = eventID;
    }

    public User getInvitee() {
        return invitee;
    }

    public void setInvitee(User invitee) {
        this.invitee = invitee;
    }

    public String getReplyInvite() {
        return replyInvite;
    }

    public void setReplyInvite(String replyInvite) {
        this.replyInvite = replyInvite;
    }

    @Override
    public String toString() {
        return "InvitationEveniment{" +
                "creator=" + creator +
                ", eventID=" + eventID +
                ", invitee=" + invitee +
                ", replyInvite='" + replyInvite + '\'' +
                ", idInv=" + idInv +
                '}';
    }
}
