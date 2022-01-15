package service;

import Networking.Networking;
import domain.*;
import obs.Observable;
import repository.RepoException;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MainService extends Observable {
    private final UserService us;
    private final FriendshipService fs;
    private final MessageService ms;
    private final EvenimentService es;
    private final InvitationsService is;
    String currentUser;

    public MainService(UserService us, FriendshipService fs, MessageService ms, EvenimentService es, InvitationsService is) {
        this.us = us;
        this.fs = fs;
        this.ms = ms;
        this.es = es;
        this.is = is;
    }

    public void setcurrentUser(String User) {
        this.currentUser = User;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public User addUser(User entity) {

        if (us.getbyusername(entity.getUserName()) != null)
            throw new RepoException("Username is taken by another user!");
        return us.save(entity);
    }

    public User findUser(Long id) {
        return us.findOne(id);
    }

    public User deleteUser(Long id) {
        User u = us.delete(id);

        for (Friendship f : findAllFriendships()) {
            if (f.getId().getRight().equals(u.getId()))
                removeFriendship(new Tuple<>(f.getId().getLeft(), u.getId()));
            if (f.getId().getLeft().equals(u.getId()))
                removeFriendship(new Tuple<>(u.getId(), f.getId().getRight()));
        }
        for (Message message : ms.findAll()) {
            if (message.getFrom() != null && message.getFrom().getId().equals(id)) {
                ms.delete(message.getId());
            }
            for (User x : message.getTo()) {
                if (x.getId().equals(id))
                    ms.delete(message.getId());
            }
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
        notifyobservers();
        return null;
    }

    public Friendship removeFriendship(Tuple<Long, Long> id) {
        Friendship f = fs.findOne(id);
        fs.delete(id);
        us.findOne(f.getId().getLeft()).remFriend(us.findOne(f.getId().getRight()));
        us.findOne(f.getId().getRight()).remFriend(us.findOne(f.getId().getLeft()));
        notifyobservers();
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

    public boolean logInToAccount(String username, String password) {

        for (var user : findAllUsers()
        ) {
            if (user.getUserName().equals(username) && user.getPassword().equals(password)) {
                return true;
            }

        }
        return false;
    }


    public List<User> getUserFriends(Long id) {
        List<Friendship> friendships = new ArrayList<>();
        findAllFriendships().forEach(friendships::add);
        List<User> friends = friendships.stream()
                .filter(f -> f.getId().getLeft().equals(id) && f.getStatut().equals("Approved"))
                .map(f -> findUser(f.getId().getRight()))
                .collect(Collectors.toList());
        friends.addAll(friendships.stream()
                .filter(f -> f.getId().getRight().equals(id) && f.getStatut().equals("Approved"))
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
        return getDtos(id, friendships);
    }

    public User updateUser(Long id, String fName, String lName, String password) {
        User u = findUser(id);
        if (u == null) throw new RepoException("User doesn't exist!");
        u.setFirstName(fName);
        u.setLastName(lName);
        u.setPassword(password);
        return us.update(u);
    }

    public Iterable<Friendship> findFriendRequests(User user) {
        List<Friendship> friendsToBe = new ArrayList<>();
        fs.findAll().forEach(x -> {
            if ((x.getId().getRight().equals(user.getId()) || x.getId().getLeft().equals(user.getId())) && x.getStatut().equals("Pending"))
                friendsToBe.add(x);
        });
        return friendsToBe;
    }

    public List<DTO> getUserFriendsByMonth(Long id, LocalDate firstDate, LocalDate secondDate) {
        List<Friendship> friendships = StreamSupport
                .stream(findAllFriendships().spliterator(), false)
                .collect(Collectors.toList())
                .stream()
                .filter(x -> x.getDate().toLocalDate().isAfter(firstDate) && x.getDate().toLocalDate().isBefore(secondDate) && x.getStatut().equals("Approved") || (x.getId().getLeft().equals(id) || x.getId().getRight().equals(id)))
                .collect(Collectors.toList());
        return getDtos(id, friendships);
    }

    public List<Message> getMessageToUser(User user, LocalDate firstDate, LocalDate secondDate) {
        List<Message> messageList = new ArrayList<>();
        for (Message message : ms.findAll()
        ) {
            if (message.getData().toLocalDate().isAfter(firstDate) && message.getData().toLocalDate().isBefore(secondDate) && message.getTo().contains(user)) {
                messageList.add(message);
            }
        }
        return messageList;
    }

    public List<Message> getMessageToUserBySpecificFriend(User user, User userFriend, LocalDate firstDate, LocalDate secondDate) {
        List<Message> messageListBySpecificFriend = new ArrayList<>();
        for (Message message : getMessageToUser(user, firstDate, secondDate)
        ) {
            if (message.getFrom().equals(userFriend)) {
                messageListBySpecificFriend.add(message);
            }
        }
        return messageListBySpecificFriend;
    }


    public Message saveMsg(Message entity) {
        Message m = ms.save(entity);
        notifyobservers();
        return m;
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

    private List<DTO> getDtos(Long id, List<Friendship> friendships) {
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

    public Eveniment createEveniment(Eveniment eveniment) {

        es.save(eveniment);
       List<Eveniment> ev = new ArrayList<>();
       findEvenimente().forEach(x->ev.add(x));

        for (User u : findAllUsers()
        ) {
            if (u.equals(getByUsername(getCurrentUser())))
                saveInvitation(new InvitationEveniment(getByUsername(getCurrentUser()), ev.size(), u, "approved"));
            else
                saveInvitation(new InvitationEveniment(getByUsername(getCurrentUser()), ev.size(), u));
        }
        return eveniment;
    }


    public Eveniment deleteEveniment(int id) {
        for (InvitationEveniment ie : is.findAll()
        ) {
            is.delete(id);
        }
        return deleteEveniment(id);
    }

    public Eveniment findEveniment(int id) {
        return es.findOne(id);
    }

    public Iterable<Eveniment> findEvenimente() {
        return es.findAll();
    }

    public Eveniment updateEveniment(Eveniment eveniment) {
        return es.update(eveniment);
    }

    public InvitationEveniment saveInvitation(InvitationEveniment invitationEveniment) {
        return is.save(invitationEveniment);
    }

    public InvitationEveniment updateInvitation(InvitationEveniment invitationEveniment) {
        return is.update(invitationEveniment);
    }

    public List<Eveniment> getEventsByUser(User user) {
        List<Eveniment> evenimentsByUser = new ArrayList<>();
        for (Eveniment eveniment : es.findAll()
        ) {
            if (eveniment.getParticipants().contains(user))
                evenimentsByUser.add(eveniment);

        }
        return evenimentsByUser;
    }

    public Iterable<InvitationEveniment> findInvitaions() {
        return is.findAll();
    }

    public InvitationEveniment checkIfAccepted(User invitee, Eveniment eveniment) {
        for (InvitationEveniment x : findInvitaions()
        ) {
            if (x.getEventID() == eveniment.getEventID() && x.getInvitee().equals(invitee))
                return x;
        }
        return null;
    }
}
