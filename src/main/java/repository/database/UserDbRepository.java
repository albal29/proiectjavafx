package repository.database;
import domain.validation.Validator;
import repository.RepoException;
import repository.Repository;
import domain.*;
import java.sql.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class UserDbRepository implements Repository<Long, User> {
    private String url;
    private String username;
    private String password;
    private Validator<User> validator;

    public UserDbRepository(String url, String username, String password, Validator<User> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }
    @Override
    public User findOne(Long aLong) {
        final int aux = Integer.parseInt(aLong.toString());
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from users u WHERE u.id=\'"+ aLong.intValue() + "\'");

             ResultSet resultSet = statement.executeQuery()) {
                    if(resultSet.next()){

                        Integer id = resultSet.getInt("id");
                        String firstName = resultSet.getString("firstName");
                        String lastName = resultSet.getString("lastName");
                        String username = resultSet.getString("username");
                        String email = resultSet.getString("email");
                        String password = resultSet.getString("password");
                        User user = new User(Long.valueOf(id),firstName,lastName,username,email,password);
                        return user;
                    }
            }
         catch (SQLException e) {
            e.printStackTrace();
        }
    return null;
    }

    @Override
    public Iterable<User> findAll() {
        Set<User> users = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from users");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public User save(User entity) {
        User u = findOne(entity.getId());
        if(u!=null)
            throw new RepoException("User already exists!");
        String sql = "insert into users values (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            validator.validate(entity);
            ps.setInt(1,entity.getId().intValue());
            ps.setString(2, entity.getFirstName());
            ps.setString(3, entity.getLastName());
            ps.setString(4,entity.getUserName());
            ps.setString(5,entity.geteMail());
            ps.setString(6,entity.getPassword());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User delete(Long aLong) {
        User u = findOne(aLong);
        int aux = aLong.intValue();
        if(u==null)
            throw new RepoException("User with given id doesn't exist!");
        String sql = "delete from users u WHERE u.id=?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1,aux);
            statement.executeUpdate();
            return u;
        }

        catch (SQLException e) {
            e.printStackTrace();
        }
        return u;
    }

    @Override
    public User update(User entity) {
        User u = findOne(entity.getId());
        if(u==null) throw new RepoException("User with given id doesn't exist!");
        validator.validate(entity);
        String sql = "UPDATE users set firstName=?,lastName=?, password=? from users WHERE id =\'"+ entity.getId().intValue() + "\'";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            validator.validate(entity);
            ps.setString(1, entity.getFirstName());
            ps.setString(2, entity.getLastName());
            ps.setString(3,entity.getPassword());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return u;
    }
}