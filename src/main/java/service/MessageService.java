package service;

import domain.DTOchat;
import domain.Message;
import repository.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MessageService implements Service<Integer, Message>{
    Repository<Integer,Message> rep;

    public MessageService(Repository<Integer, Message> rep) {
        this.rep = rep;
    }

    @Override
    public Message findOne(Integer integer) {
        return rep.findOne(integer);
    }

    @Override
    public Iterable<Message> findAll() {
        return rep.findAll();
    }

    @Override
    public Message save(Message entity) {
        return rep.save(entity);
    }

    @Override
    public Message delete(Integer integer) {
        return rep.delete(integer);
    }

    public List<DTOchat> getConv(Long id1, Long id2){
        List<Message> msgs = new ArrayList<>();
        findAll().forEach(msgs::add);
        List<DTOchat> aux = msgs.stream()
                .filter(m -> m.getFrom().getId() == id1 && m.getTo().stream().filter(x->x.getId()==id2).findAny().orElse(null)!=null)
                .map(m -> new DTOchat(m.getId(),m.getFrom().getUserName(),m.getMessage(),m.getData(),m.getReply()))
                .collect(Collectors.toList());
        aux.addAll(msgs.stream()
                .filter(m -> m.getFrom().getId() == id2 && m.getTo().stream().filter(x->x.getId()==id1).findAny().orElse(null)!=null)
                .map(m -> new DTOchat(m.getId(),m.getFrom().getUserName(),m.getMessage(),m.getData(),m.getReply()))
                .collect(Collectors.toList()));
        Collections.sort(aux, (o1, o2) -> o1.getDate().compareTo(o2.getDate()));
        return aux;
    }

    @Override
    public Message update(Message entity) {
        return null;
    }
}
