package service;

import domain.Eveniment;
import repository.Repository;

public class EvenimentService implements Service<Integer, Eveniment>{
    Repository<Integer,Eveniment> evenimentRepository;

    public EvenimentService(Repository<Integer, Eveniment> evenimentRepository) {
        this.evenimentRepository = evenimentRepository;
    }

    @Override
    public Eveniment findOne(Integer integer) {
        return evenimentRepository.findOne(integer);
    }

    @Override
    public Iterable<Eveniment> findAll() {
        return evenimentRepository.findAll();
    }

    @Override
    public Eveniment save(Eveniment entity) {
        return evenimentRepository.save(entity);
    }

    @Override
    public Eveniment delete(Integer integer) {
        return evenimentRepository.delete(integer);
    }

    @Override
    public Eveniment update(Eveniment entity) {
        return update(entity);
    }
}
