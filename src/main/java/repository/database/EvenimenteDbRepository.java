package repository.database;

import domain.Eveniment;
import domain.User;
import repository.RepoException;
import repository.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EvenimenteDbRepository implements Repository<Integer, Eveniment> {
    private final String url;
    private final String username;
    private final String password;
    private final InvitationsDbRepository invitationsDbRepository;

    public EvenimenteDbRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.invitationsDbRepository = new InvitationsDbRepository(url, username, password);
    }

    @Override
    public Eveniment findOne(Integer integer) {
        String sql = "select * from evenimente even where even.idevent='" + integer + "'";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                int id = resultSet.getInt("idEvent");
                String title = resultSet.getString("title");
                String description = resultSet.getString("description");
                int idCreator = resultSet.getInt("creator");
                LocalDateTime eventDate = LocalDateTime.parse(resultSet.getString("eventDate"));
                String location = resultSet.getString("location");

                return new Eveniment(id, title, description, invitationsDbRepository.getUser(idCreator), getParticipants(id), eventDate, location);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<User> getParticipants(int id) {
        List<User> participants = new ArrayList<>();
        String sql = "select * from users u inner join invitations inv on inv.invitee=u.id where inv.reply = 'approved' and inv.eventID='" + id + "'";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int idUser = resultSet.getInt("id");
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");
                String username = resultSet.getString("username");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                participants.add(new User((long) idUser, firstName, lastName, username, email, password));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return participants;
    }

    @Override
    public Iterable<Eveniment> findAll() {
        Set<Eveniment> eveniments = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from evenimente");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                int id = resultSet.getInt("idEvent");
                String title = resultSet.getString("title");
                String description = resultSet.getString("description");
                int idCreator = resultSet.getInt("creator");
                LocalDateTime eventDate = LocalDateTime.parse(resultSet.getString("eventDate"));
                String location = resultSet.getString("location");

                eveniments.add(new Eveniment(id, title, description, invitationsDbRepository.getUser(idCreator), getParticipants(id), eventDate, location));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return eveniments;
    }

    @Override
    public Eveniment save(Eveniment entity) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("insert into evenimente values(?,?,?,?,?,?) ")) {
            statement.setInt(1, size() + 1);
            statement.setString(2, entity.getTitle());
            statement.setString(3, entity.getDescription());
            statement.setInt(4, Math.toIntExact(entity.getCreator().getId()));
            statement.setString(5, entity.getEventDate().toString());
            statement.setString(6, entity.getLocation());

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Eveniment delete(Integer integer) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("delete from invitations i where i.eventid=?")) {
            statement.setInt(1, integer);
            statement.executeUpdate();
            PreparedStatement st1 = connection.prepareStatement("delete from evenimente e where e.idevent=?");
            st1.setInt(1, integer);
            st1.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Eveniment update(Eveniment entity) {
        Eveniment eveniment = findOne(entity.getId());
        if(eveniment.equals(null)) throw new RepoException("Eveniment with given id doesn't exist!");
        String sql = "UPDATE evenimente set title=?,description=?, eventdate=? location=? WHERE id ='" + entity.getId() + "'";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, entity.getId());
            statement.setString(2, entity.getTitle());
            statement.setString(3, entity.getDescription());
            statement.setInt(4, Math.toIntExact(entity.getCreator().getId()));
            statement.setString(5, entity.getEventDate().toString());
            statement.setString(6, entity.getLocation());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return eveniment;
    }

    private int size() {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) as nr from evenimente");
             ResultSet resultSet = statement.executeQuery()) {
            resultSet.next();
            return resultSet.getInt("nr");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;

    }
}
