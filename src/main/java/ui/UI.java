package ui;

import domain.Friendship;
import domain.Tuple;
import domain.User;
import domain.validation.ValidationException;
import repository.RepoException;
import service.MainService;

import java.util.List;
import java.util.Scanner;

public class UI {
    MainService srv;
    Scanner scan;

    public UI(MainService srv){
        this.srv = srv;
        this.scan = new Scanner(System.in).useDelimiter("\n");
    }

    public void printmenu(){
        String menu = new String();
        menu = "1.Add user\n2.Add friendship\n3.Remove user\n4.Remove friendship\n5.Show all users\n6.Show all friendships\n7.Number of communities\n8.Most sociable community\n9.Show friends of user\n10.Show friends of user by month\n11.Exit";
        System.out.println(menu);
    }

    public void addUser(){
        System.out.println("Give id :");
        Long id = scan.nextLong();
        System.out.println("Give first name :");
        String fName = scan.next();
        System.out.println("Give last name :");
        String lName = scan.next();
        System.out.println("Give username :");
        String username = scan.next();
        System.out.println("Give email :");
        String email = scan.next();
        System.out.println("Give password :");
        String password = scan.next();

        User u = new User(id,fName,lName,username,email,password);
        try{
            srv.addUser(u);
            System.out.println("User added successfully!\n");
        } catch (ValidationException e) {
            System.out.println(e.toString());
        }
        catch (RepoException e){
            System.out.println(e.toString());
        }
        catch (IllegalArgumentException e){
            System.out.println(e.toString());
        }

    }

    public void addFriendship(){
        System.out.println("Give 1st user id:");
        Long id1 = scan.nextLong();
        System.out.println("Give 2st user id:");
        Long id2 = scan.nextLong();
        if(id1>id2){
            Long aux = id1;
            id1 = id2;
            id2 = aux;
        }

        Friendship f = new Friendship(id1,id2);
        try{
            srv.addFriendship(f);
            System.out.println("Friendship added successfully!");
        }
        catch (ValidationException e){
            System.out.println(e.toString());
        }
        catch (RepoException e){
            System.out.println(e.toString());
        }
        catch (IllegalArgumentException e){
            System.out.println(e.toString());
        }
    }

    public void removeUser(){
        System.out.println("Give id :");
        Long id = scan.nextLong();

        try{
            User u = srv.deleteUser(id);
            System.out.println(u.toString()+"has been removed!");
        }
        catch (RepoException e){
            System.out.println(e.toString());
        }
        catch (IllegalArgumentException e){
            System.out.println(e.toString());
        }

    }

    public void removeFriendship(){
        System.out.println("Give 1st user id :");
        Long id1 = scan.nextLong();
        System.out.println("Give 2nd user id :");
        Long id2 = scan.nextLong();
        if(id1>id2){
            Long aux = id1;
            id1 = id2;
            id2 = aux;
        }
        Tuple<Long,Long> id = new Tuple<>(id1,id2);

        try{
            srv.removeFriendship(id);
            System.out.println("Friendship between"+srv.findUser(id1).toString()+" and"+srv.findUser(id2)+" has been removed!");
        }
        catch (ValidationException e){
            System.out.println(e.toString());
        }
        catch (RepoException e){
            System.out.println(e.toString());
        }
        catch (IllegalArgumentException e){
            System.out.println(e.toString());
        }

    }

    public void nrOfCommunities(){
        int nr = srv.noOfCommunities();
        System.out.println("Number of communities:"+nr);
    }

    public void mostSociableCommunity(){
        List<Long> l = srv.mostSociableCommunity();
        System.out.println("Members of most sociable community:");
        for(Long e:l){
            System.out.println(srv.findUser(e));
        }
    }

    public void showAllUsers(){
        srv.findAllUsers().forEach(System.out::println);
    }
    public void showAllFriendships(){
        for(Friendship f : srv.findAllFriendships())
        {
            System.out.println("Friendship between "+srv.findUser(f.getId().getLeft()).toString()+" and "+srv.findUser(f.getId().getRight()));
        }
    }

    public void userFriends(Long aux){
        try{
            System.out.println("Friends of "+srv.findUser(aux).toString()+":");
            srv.getUserFriends(aux).forEach(System.out::println);
        }
        catch (RepoException r) {
            System.out.println(r.getMessage());
        }
    }

    public void userFriendsByMonth(){
        Long aux = scan.nextLong();
        String month = scan.next();
        try{
            System.out.println("Friends of "+srv.findUser(aux).toString()+":");
            srv.getUserFriendsByMonth(aux,month).forEach(System.out::println);
        }
        catch (RepoException r) {
            System.out.println(r.getMessage());
        }
    }

    public void updateUser(){
        System.out.println("Give id: ");
        Long id = scan.nextLong();
        System.out.println("Give first name :");
        String fName = scan.next();
        System.out.println("Give last name :");
        String lName = scan.next();
        System.out.println("Give password :");
        String password = scan.next();
        try{
            srv.updateUser(id,fName,lName,password);
            System.out.println("User updated succesfully!");
        }
        catch (RepoException e){
            System.out.println(e.getMessage());
        }
        catch (ValidationException e){
            System.out.println(e.getMessage());
        }
    }


    public void start(){
        boolean ok = true;
        int c;
        while(ok){
            printmenu();
            System.out.println("Give option: \n");
            c = scan.nextInt();
             switch (c) {
                 case 1:{
                     addUser();
                     break;}
                 case 2: {
                      addFriendship();
                      break;
                 }
                 case 3: {
                     removeUser();
                     break;
                 }
                 case 4: {
                     removeFriendship();
                     break;
                 }
                 case 5: {
                     showAllUsers();
                     break;
                 }
                 case 6: {
                     showAllFriendships();
                     break;
                 }
                 case 7: {
                     nrOfCommunities();
                     break;
                 }
                 case 8:{
                     mostSociableCommunity();
                     break;
                 }
                 case 9:{
                     Long aux = scan.nextLong();
                     userFriends(aux);
                     break;
                 }
                 case 10: {
                    userFriendsByMonth();
                    break;
                 }
                 case 11:{
                     ok=false;
                     break;
                 }
             }

        }

    }
}
