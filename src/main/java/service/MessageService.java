package service;

import domain.DTOchat;
import domain.Message;
import repository.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MessageService implements Service<Integer, Message> {
    private final Repository<Integer, Message> rep;

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

    public List<DTOchat> getConv(Long id1, Long id2) {
        List<Message> msgs = new ArrayList<>();
        msgs.addAll((Collection<? extends Message>) findAll());
        List<DTOchat> aux = msgs.stream()
                .filter(m -> m.getFrom().getId().equals(id1) && m.getTo().stream().filter(x -> x.getId().equals(id2)).findAny().orElse(null) != null)
                .map(m -> new DTOchat(m.getId(), m.getFrom().getUserName(), m.getMessage(), m.getData(), m.getReply()))
                .collect(Collectors.toList());
        aux.addAll(msgs.stream()
                .filter(m -> m.getFrom().getId().equals(id2) && m.getTo().stream().filter(x -> x.getId().equals(id1)).findAny().orElse(null) != null)
                .map(m -> new DTOchat(m.getId(), m.getFrom().getUserName(), m.getMessage(), m.getData(), m.getReply()))
                .collect(Collectors.toList()));
        aux.sort((o1, o2) -> o2.getDate().compareTo(o1.getDate()));
        Collections.reverse(aux);
        return aux;
    }

    @Override
    public Message update(Message entity) {
        return null;
    }
}
