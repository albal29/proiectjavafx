package ui;

import domain.Friendship;
import domain.Message;
import domain.User;
import domain.UserAccount;
import repository.RepoException;
import service.MainService;

import java.util.ArrayList;
import java.util.List;


public class LoginUi extends UI{
    UserAccount userAccount;

    public LoginUi(MainService srv) {
        super(srv);
    }

    public void firstMenu() {
        System.out.println("1.Log in\n2.Sign up\n3.exit\n");
    }

    public void secondMenu(){
        System.out.println("1.Add friend\n2.See requests\n3.See friends\n4.Send message\n5.Enter chat\n6.Exit\n");
    }

    public boolean logInToAccount() {
        System.out.println("Username");
        String userName = scan.next();
        System.out.println("Password");
        String password = scan.next();

        userAccount = new UserAccount(userName, password);
        for (var user : srv.findAllUsers()
        ) {
            if (user.getUserName().equals(userAccount.getUserName()) && user.getPassword().equals(userAccount.getPassword())) {
                userAccount.setId(user.getId());
                return true;
            }
            if (user.getUserName().equals(userAccount.getUserName()) && !user.getPassword().equals(userAccount.getPassword())) {
                System.out.println("Incorrect password");
                return false;
            }
        }
        return false;
    }

    public void friendshipsMng() {
        if(srv.findPendingFriendships().spliterator().getExactSizeIfKnown()==0){
            System.out.println("There are no pending friendships.");
            return;
        }
        for (var x : srv.findPendingFriendships()) {
            if (x.getId().getRight().equals(userAccount.getId())) {
                System.out.println(srv.findUser(x.getId().getLeft()) + " wants to be friends with you.");
                System.out.println("Do you accept? yes/no");
                String answer = scan.next();
                switch (answer) {
                    case "yes" -> {
                        srv.updateFriendship(new Friendship(x.getId().getLeft(), x.getId().getRight(), x.getDate(), "Approved"));
                        break;
                    }
                    case "no" -> {
                        srv.updateFriendship(new Friendship(x.getId().getLeft(), x.getId().getRight(), x.getDate(), "Rejected"));
                        break;
                    }
                    default -> System.out.println("It will wait");
                }
            } else System.out.println("No requests");
        }
    }

    public void addFriend() {
        System.out.println("Give username:");
        String username = scan.next();
        for (var user : srv.findAllUsers()) {
            if (user.getUserName().equals(username)) {
                try{
                    srv.addFriendship(new Friendship(userAccount.getId(), user.getId()));
                }catch (RepoException re){
                    System.out.println(re.getMessage());
                }
            }
        }
    }

    public void checkRequests(){
        if(srv.findPendingFriendships().spliterator().getExactSizeIfKnown()>0){
            System.out.println("You have new friendship Requests");
        }
    }

    public void sendMsg(){
        System.out.println("Write the message:");
        String msg = scan.next();
        System.out.println("Give the usernames(type none to stop):");
        String username = new String();
        List<User> users = new ArrayList<>();
        while(!username.equals("none")){
            username = scan.nextLine();
            if(username.equals("none")) break;
            try{
                users.add(srv.getByUsername(username));
            }
            catch (RepoException re) {
                System.out.println(re.toString());
            }
        }
        Message m = new Message(srv.findUser(userAccount.getId()),users,msg,null);
        srv.saveMsg(m);
    }




    public void start() {
        boolean ok = true;
        int command;
        while (ok) {
            firstMenu();
            System.out.println("Option:");
            command = scan.nextInt();
            switch (command) {
                case 1 -> {
                    startLogIn();
                    break;
                }
                case 2 -> {
                    addUser();
                    break;
                }
                case 3 ->{
                    ok = false;
                    break;
                }


            }
        }
    }

    void sendChatMsg(User u){
        List<User> usr = new ArrayList<>();
        usr.add(u);
        System.out.println("Type message:");
        String msg = scan.next();
        Message m = new Message(srv.findUser(userAccount.getId()),usr,msg,null);
        srv.saveMsg(m);
    }

    void replyMsg(User u){
        List<User> usr = new ArrayList<>();
        usr.add(u);
        System.out.println("Enter message id:");
        Integer id = scan.nextInt();
        System.out.println("Type message:");
        String msg = scan.next();
        Message m = new Message(srv.findUser(userAccount.getId()),usr,msg,srv.findMsg(id));
        srv.saveMsg(m);
    }


    void enterChat(){
        System.out.println("Enter username to chat with");
        String username = scan.next();
        try{
            User u = srv.getByUsername(username);
            boolean ok = true;
            while(ok==true){
                srv.getChats(userAccount.getId(),u.getId()).forEach(System.out::println);
                System.out.println("1.Send message\n2.Reply to a message\n3.Exit");
                System.out.println("Option:");
                int command = scan.nextInt();
                switch (command) {
                    case 1 -> {
                        sendChatMsg(u);
                        break;
                    }
                    case 2 -> {
                       replyMsg(u);
                        break;
                    }
                    case 3 ->{
                        ok = false;
                        break;
                    }


                }

            }
        }
        catch (RepoException re) {
            System.out.println(re.toString());
        }
        }




    public void startLogIn(){
        System.out.println("Please login first");
        boolean ok = logInToAccount();
        int command;
        while (ok) {
//            checkRequests();
            secondMenu();
            System.out.println("Option:");
            command = scan.nextInt();
            switch (command) {
                case 1 -> {
                    addFriend();
                    break;
                }
                case 2 -> {
                    friendshipsMng();
                    break;
                }
                case 3 -> {
                    userFriends(userAccount.getId());
                }
                case 4 -> {
                    sendMsg();
                    break;
                }
                case 5 ->{
                    enterChat();
                    break;
                }
                case 6 -> ok = false;
            }
        }
    }
}
