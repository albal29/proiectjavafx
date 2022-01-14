package repository.database;

import domain.InvitationEveniment;
import domain.User;
import repository.Repository;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class InvitationsDbRepository implements Repository<Integer, InvitationEveniment> {
    private final String url;
    private final String username;
    private final String password;

    public InvitationsDbRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public InvitationEveniment findOne(Integer integer) {
        String sql = "select * from invitations i where i.idinvite='" + integer + "'";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                int creator = resultSet.getInt("creator");
                int eventID = resultSet.getInt("eventid");
                String replyI = resultSet.getString("reply");
                int invitee = resultSet.getInt("invitee");
                int id = resultSet.getInt("idinvite");

                return new InvitationEveniment(id,getUser(creator),eventID,getUser(invitee),replyI);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User getUser(Integer id) {

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from users u WHERE u.id='" + id + "'");

             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {

                int uid = resultSet.getInt("id");
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");
                String username = resultSet.getString("username");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                return new User((long) uid, firstName, lastName, username, email, password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<InvitationEveniment> findAll() {
        Set<InvitationEveniment> invitations = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from invitations");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                int creator = resultSet.getInt("creator");
                int eventID = resultSet.getInt("eventid");
                String replyI = resultSet.getString("reply");
                int invitee = resultSet.getInt("invitee");
                int id = resultSet.getInt("idinvite");

                invitations.add(new InvitationEveniment(id,getUser(creator),eventID,getUser(invitee),replyI));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return invitations;
    }

    @Override
    public InvitationEveniment save(InvitationEveniment entity) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("insert into invitations values(?,?,?,?,?) ")) {
            statement.setInt(1, Math.toIntExact(entity.getCreator().getId()));
            statement.setInt(2, entity.getEventID());
            statement.setString(3, entity.getReplyInvite());
            statement.setInt(4, Math.toIntExact(entity.getInvitee().getId()));
            statement.setInt(5, size()+1);

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public InvitationEveniment delete(Integer integer) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("delete from invitations i where i.eventid=?")) {
            statement.setInt(1, integer);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public InvitationEveniment update(InvitationEveniment entity) {
        String sql = "UPDATE invitations set reply=? from users WHERE idinvite ='" + entity.getIdInv() + "'";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1,entity.getReplyInvite());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entity;
    }

    private int size() {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) as nr from invitations");
             ResultSet resultSet = statement.executeQuery()) {
            resultSet.next();
            return resultSet.getInt("nr");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;

    }
}
