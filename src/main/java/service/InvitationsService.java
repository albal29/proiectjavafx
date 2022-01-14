package service;

import domain.InvitationEveniment;
import repository.Repository;

public class InvitationsService implements Repository<Integer, InvitationEveniment> {
    Repository<Integer,InvitationEveniment> integerInvitationEvenimentRepository;

    public InvitationsService(Repository<Integer, InvitationEveniment> integerInvitationEvenimentRepository) {
        this.integerInvitationEvenimentRepository = integerInvitationEvenimentRepository;
    }

    @Override
    public InvitationEveniment findOne(Integer integer) {
        return integerInvitationEvenimentRepository.findOne(integer);
    }

    @Override
    public Iterable<InvitationEveniment> findAll() {
        return integerInvitationEvenimentRepository.findAll();
    }

    @Override
    public InvitationEveniment save(InvitationEveniment entity) {
        return integerInvitationEvenimentRepository.save(entity);
    }

    @Override
    public InvitationEveniment delete(Integer integer) {
        return integerInvitationEvenimentRepository.delete(integer);
    }

    @Override
    public InvitationEveniment update(InvitationEveniment entity) {
        return null;
    }

}
