package domain.validation;


import domain.Friendship;
import domain.User;

public class FriendshipValidator implements Validator<Friendship> {

    @Override
    public void validate(Friendship entity) throws ValidationException {
        if(entity.getId().getLeft()<0||entity.getId().getRight()<0) throw new ValidationException("Id's can t be negative!");
        if(entity.getId().getLeft()==entity.getId().getRight()) throw new ValidationException("Id's can t be identical!");
    }
}
