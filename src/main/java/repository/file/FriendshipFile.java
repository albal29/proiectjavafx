package repository.file;

import domain.Friendship;
import domain.Tuple;
import domain.validation.Validator;

import java.time.LocalDateTime;
import java.util.List;

public class FriendshipFile extends AbstractFileRepository<Tuple<Long,Long>, Friendship> {
    public FriendshipFile(String fileName, Validator<Friendship> validator) {
        super(fileName, validator);
    }

    @Override
    protected Friendship extractEntity(List<String> attributes) {
        Friendship f = new Friendship(Long.parseLong(attributes.get(0)),Long.parseLong(attributes.get(1)), LocalDateTime.parse(attributes.get(2)),attributes.get(3));
        return f;
    }

    @Override
    protected String createEntityAsString(Friendship entity) {
        String aux = new String();
        aux = String.valueOf(entity.getId().getLeft()) + ";" + String.valueOf(entity.getId().getRight()) + ";" +String.valueOf(entity.getDate())+";"+entity.getStatut();
        return aux;
    }
}
