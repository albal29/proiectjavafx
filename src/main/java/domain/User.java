package domain;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User extends Entity<Long>{
    private String firstName;
    private String lastName;
    private String userName;
    private String eMail;
    private String password;
    private List<User> friends;

    public User(Long id,String firstName, String lastName, String userName, String eMail, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.eMail = eMail;
        this.password = password;
        this.friends = new ArrayList<>();
        this.setId(id);
    }
    public User(){}

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<User> getFriends() {
        return friends;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName + " " + "with the username:" + userName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User that = (User) o;
        return this.getId().equals(that.getId())||this.geteMail().equals(that.geteMail())||this.getUserName().equals(that.geteMail());}
    @Override
    public int hashCode() {
        return Objects.hash(getId(),geteMail(),getUserName());
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFriends(List<User> friends) {
        this.friends = friends;
    }

    public void addFriend(User u){
        friends.add(u);
    }

    public void remFriend(User u){
        friends.remove(u);
    }
}