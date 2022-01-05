package service;

import domain.Friendship;
import domain.Tuple;
import repository.Repository;

public class FriendshipService implements Service<Tuple<Long,Long>, Friendship>{

    Repository<Tuple<Long,Long>,Friendship> rep;

    public FriendshipService(Repository<Tuple<Long, Long>, Friendship> rep) {
        this.rep = rep;
    }

    @Override
    public Friendship findOne(Tuple<Long, Long> longLongTuple) {
        return rep.findOne(longLongTuple);
    }

    @Override
    public Iterable<Friendship> findAll() {
        return rep.findAll();
    }

    @Override
    public Friendship save(Friendship entity) {
        return rep.save(entity);
    }

    @Override
    public Friendship delete(Tuple<Long, Long> longLongTuple) {
        return rep.delete(longLongTuple);
    }

    @Override
    public Friendship update(Friendship entity) {
        return rep.update(entity);
    }
}
