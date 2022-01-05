package repository.database;

import domain.Friendship;
import domain.Message;
import domain.User;
import domain.validation.Validator;
import repository.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MessageDbRepository implements Repository<Integer, Message> {
    String url;
    String username;
    String password;
    public MessageDbRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public User getUser(Integer id){

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from users u WHERE u.id=\'"+ id + "\'");

             ResultSet resultSet = statement.executeQuery()) {
            if(resultSet.next()){

                Integer uid = resultSet.getInt("id");
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");
                String username = resultSet.getString("username");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                User user = new User(Long.valueOf(uid),firstName,lastName,username,email,password);
                return user;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public Message findOne(Integer integer) {
        String sql = "select * from messages m where m.id=\'"+ integer + "\'";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            if(resultSet.next()){
                Integer id = resultSet.getInt("id");
                String message = resultSet.getString("text");
                LocalDateTime date = LocalDateTime.parse(resultSet.getString("date"));
                Integer from = resultSet.getInt("from");
                Integer reply = resultSet.getInt("reply");
                Message mreply = new Message();
                if(reply==-1) mreply = null;
                    else mreply = findOne(reply);

                Message m = new Message(id,getUser(from),new ArrayList<User>(),date,message,mreply);
                    return m;
            }

        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<User> getMessageReceivers(Integer mid){
        ArrayList<User> users = new ArrayList<>();
        String sql = "select * from users u inner join chat c on c.uid=u.id where c.mid=\'"+ mid + "\'";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while(resultSet.next()){
                Integer id = resultSet.getInt("id");
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");
                String username = resultSet.getString("username");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                User user = new User(Long.valueOf(id),firstName,lastName,username,email,password);
                users.add(user);

            }
            return users;
        }

        catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public Iterable<Message> findAll() {
        Set<Message> messages = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from messages");
             ResultSet resultSet = statement.executeQuery()) {

            while(resultSet.next()){

                Integer id = resultSet.getInt("id");
                String message = resultSet.getString("text");
                LocalDateTime date = LocalDateTime.parse(resultSet.getString("date"));
                Integer from = resultSet.getInt("from");
                Integer reply = resultSet.getInt("reply");
                Message mreply = new Message();
                if(reply==-1) mreply = null;
                else mreply = findOne(reply);
                Message m = new Message(id,getUser(from),getMessageReceivers(id),date,message,mreply);
                messages.add(m);
            }
        }

        catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

    public Integer size(){
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) as nr from messages");

             ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                return Integer.valueOf(resultSet.getInt("nr"));
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;

    }

    public void saveChat(Message entity){
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("insert into chat(mid,uid) values(?,?)"))
        {
            for(User u: entity.getTo()){
                statement.setInt(1,size());
                statement.setInt(2,u.getId().intValue());
                statement.executeUpdate();
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

    }


    @Override
    public Message save(Message entity) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("insert into messages values(?,?,?,?,?) "))
             {
              statement.setInt(1,size()+1);
              statement.setString(2,entity.getMessage());
              statement.setString(3,entity.getData().toString());
              statement.setInt(4,entity.getFrom().getId().intValue());
              if(entity.getReply()==null)
                    statement.setInt(5,-1);
              else
                  statement.setInt(5,entity.getReply().getId());

              statement.executeUpdate();
              saveChat(entity);

        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Message delete(Integer id) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("delete from chat c where c.mid=?"))
        {
            statement.setInt(1,id);
            statement.executeUpdate();
            PreparedStatement st1 = connection.prepareStatement("delete from messages m where m.id=?");
            st1.setInt(1,id);
            st1.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteChat(Integer mid,Integer uid){
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("delete from chat c where c.mid=? and c.uid=?"))
        {
            statement.setInt(1,mid);
            statement.setInt(2,uid);
            statement.executeUpdate();

    }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Message update(Message entity) {
        return null;
    }
}
