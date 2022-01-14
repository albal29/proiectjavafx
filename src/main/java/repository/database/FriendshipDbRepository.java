package repository.database;

import domain.Friendship;
import domain.Tuple;
import domain.validation.Validator;
import repository.RepoException;
import repository.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class FriendshipDbRepository implements Repository<Tuple<Long, Long>, Friendship> {
    private final String url;
    private final String username;
    private final String password;
    private final Validator<Friendship> validator;

    public FriendshipDbRepository(String url, String username, String password, Validator<Friendship> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }


    @Override
    public Friendship findOne(Tuple<Long, Long> longLongTuple) {
        final int aux1 = Integer.parseInt(longLongTuple.getLeft().toString());
        final int aux2 = Integer.parseInt(longLongTuple.getRight().toString());
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from friendships f WHERE f.id1='" + aux1 + "' and f.id2='" + aux2 + "'");

             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {

                int id1 = resultSet.getInt("id1");
                int id2 = resultSet.getInt("id2");
                String date = resultSet.getString("date");
                String statut = resultSet.getString("statut");
                return new Friendship((long) id1, (long) id2, LocalDateTime.parse(date), statut);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public Iterable<Friendship> findAll() {
        Set<Friendship> friends = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from friendships");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                int id1 = resultSet.getInt("id1");
                int id2 = resultSet.getInt("id2");
                String date = resultSet.getString("date");
                String statut = resultSet.getString("statut");
                Friendship f = new Friendship((long) id1, (long) id2, LocalDateTime.parse(date), statut);
                friends.add(f);
            }
            return friends;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friends;
    }


    @Override
    public Friendship save(Friendship entity) {
        Friendship f = findOne(entity.getId());
        if (f != null)
            throw new RepoException("Friendship already exists!");
        String sql = "insert into friendships(id1,id2,date,statut) values (?, ?, ?,?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            validator.validate(entity);
            ps.setInt(1, entity.getId().getLeft().intValue());
            ps.setInt(2, entity.getId().getRight().intValue());
            ps.setString(3, entity.getDate().toString());
            ps.setString(4, entity.getStatut());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Friendship delete(Tuple<Long, Long> longLongTuple) {
        Friendship f = findOne(longLongTuple);
        int aux1 = longLongTuple.getLeft().intValue();
        int aux2 = longLongTuple.getRight().intValue();
        if (f == null)
            throw new RepoException("Friendship with given id doesn't exist!");
        String sql = "delete from friendships f WHERE f.id1=?and f.id2=?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, aux1);
            statement.setInt(2, aux2);
            statement.executeUpdate();
            return f;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return f;
    }


    @Override
    public Friendship update(Friendship entity) {
        Friendship friendshipToUpdate = findOne(entity.getId());
        if (friendshipToUpdate == null) throw new IllegalArgumentException("User with given id doesn't exist!");
        validator.validate(entity);
        String sql = "UPDATE friendships set idFriend=?,idOtherFriend=?, dateOfFriendship=? from friendships WHERE id ='" + entity.getId() + "'";
        return save(entity);
    }
}
