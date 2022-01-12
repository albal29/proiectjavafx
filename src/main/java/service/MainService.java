package service;

import Networking.Networking;
import domain.*;
import domain.validation.ValidationException;
import repository.RepoException;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MainService {
    UserService us;
    FriendshipService fs;
    MessageService ms;
    String currentUser;

    public MainService(UserService us, FriendshipService fs, MessageService ms) {
        this.us = us;
        this.fs = fs;
        this.ms = ms;
    }

    public void setcurrentUser(String User){
        this.currentUser = User;
    }
    public String getCurrentUser(){
        return currentUser;
    }

    public User addUser(User entity) {
        return us.save(entity);
    }

    public User findUser(Long id) {
        return us.findOne(id);
    }

    public User deleteUser(Long id) {
        User u = us.delete(id);

        for (Friendship f : findAllFriendships()) {
            if (f.getId().getRight() == u.getId())
                removeFriendship(new Tuple<>(f.getId().getLeft(), u.getId()));
            if (f.getId().getLeft() == u.getId())
                removeFriendship(new Tuple<>(u.getId(), f.getId().getRight()));
        }


        return u;
    }

    public Iterable<User> findAllUsers() {
        return us.findAll();
    }

    public Iterable<Friendship> findAllFriendships() {
        return fs.findAll();
    }

    public Friendship addFriendship(Friendship f) {
        if (us.findOne(f.getId().getLeft()) == null || us.findOne(f.getId().getRight()) == null) {
            throw new RepoException("Friendship must be between existent users!");
        }
        fs.save(f);
        return null;
    }

    public Friendship removeFriendship(Tuple<Long, Long> id) {
        Friendship f = fs.findOne(id);
        fs.delete(id);
        us.findOne(f.getId().getLeft()).remFriend(us.findOne(f.getId().getRight()));
        us.findOne(f.getId().getRight()).remFriend(us.findOne(f.getId().getLeft()));
        return f;
    }

    public Friendship findFrienship(Tuple<Long, Long> id) {
        return fs.findOne(id);
    }

    public int noOfCommunities() {
        List<Long> aux = us.getListOfUID();
        Networking net = new Networking(fs.findAll(), aux, aux.get(aux.size() - 1) + 1);
        net.setFriendMatrix();
        return net.numberOfCommunities();

    }

    public List<Long> mostSociableCommunity() {
        List<Long> aux = us.getListOfUID();
        Networking net = new Networking(fs.findAll(), aux, aux.get(aux.size() - 1) + 1);
        net.setFriendMatrix();
        return net.mostSociableCommunity();

    }

    public boolean logInToAccount(String username,String password) {

        for (var user : findAllUsers()
        ) {
            if (user.getUserName().equals(username) && user.getPassword().equals(password)) {
                return true;
            }

        }
        return false;
    }


    public List<User> getUserFriends(Long id) {
        List<Friendship> friendships = new ArrayList<Friendship>();
        findAllFriendships().forEach(friendships::add);
        List<User> friends = friendships.stream()
                .filter(f -> f.getId().getLeft() == id && f.getStatut().equals("Approved"))
                .map(f -> findUser(f.getId().getRight()))
                .collect(Collectors.toList());
        friends.addAll(friendships.stream()
                .filter(f -> f.getId().getRight() == id && f.getStatut().equals("Approved"))
                .map(f -> findUser(f.getId().getLeft()))
                .collect(Collectors.toList()));
        return friends;
    }

    public List<DTO> getUserFriendsByMonth(Long id, String month) {
        List<Friendship> friendships = StreamSupport
                .stream(findAllFriendships().spliterator(), false)
                .collect(Collectors.toList())
                .stream()
                .filter(x -> x.getDate().getMonth().toString().equalsIgnoreCase((month)) && x.getStatut().equals("Approved") || (x.getId().getLeft().equals(id) || x.getId().getRight().equals(id)))
                .collect(Collectors.toList());
        List<DTO> users = new ArrayList<>();
        friendships.forEach(x -> {
            if (x.getId().getLeft().equals(id)) {
                users.add(new DTO(findUser(x.getId().getRight()).getFirstName(), findUser(x.getId().getRight()).getLastName(), x.getDate()));
            }
            if (x.getId().getRight().equals(id)) {
                users.add(new DTO(findUser(x.getId().getLeft()).getFirstName(), findUser(x.getId().getLeft()).getLastName(), x.getDate()));
            }
        });
        return users;
    }

    public User updateUser(Long id, String fName, String lName, String password) {
        User u = findUser(id);
        if (u == null) throw new RepoException("User doesn't exist!");
        u.setFirstName(fName);
        u.setLastName(lName);
        u.setPassword(password);
        return us.update(u);
    }

    public Iterable<Friendship> findPendingFriendships() {
        Set<Friendship> friendsToBe = new HashSet<>();
        fs.findAll().forEach(x -> {
            if (x.getStatut().equals("Pending"))
                friendsToBe.add(x);
        });
        return friendsToBe;
    }

    public Iterable<Friendship> findFriendRequests(User user) {
        List<Friendship> friendsToBe = new ArrayList<>();
        fs.findAll().forEach(x -> {
            if (x.getId().getRight().equals(user.getId()) || x.getId().getLeft().equals(user.getId()))
                friendsToBe.add(x);
        });
        return friendsToBe;
    }

    public Message saveMsg(Message entity) {
        return ms.save(entity);
    }

    public List<DTOchat> getChats(Long id1, Long id2) {
        return ms.getConv(id1, id2);
    }

    public Message findMsg(Integer id) {
        return ms.findOne(id);
    }

    public User getByUsername(String username) {
        return us.getbyusername(username);
    }

    public Friendship updateFriendship(Friendship f) {
        return fs.update(f);
    }
}
