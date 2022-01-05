package repository.file;

import domain.User;
import domain.validation.Validator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UtilizatorFile extends AbstractFileRepository<Long, User>{


    public UtilizatorFile(String fileName, Validator<User> validator) {
        super(fileName, validator);
    }



    @Override
    public User extractEntity(List<String> attributes) {
        User u = new User();
        u.setId(Long.parseLong(attributes.get(0)));
        u.setFirstName(attributes.get(1));
        u.setLastName(attributes.get(2));
        u.setUserName(attributes.get(3));
        u.seteMail(attributes.get(4));
        u.setPassword(attributes.get(5));
       return u;

    }

    @Override
    public String createEntityAsString(User entity) {
        String aux = new String();

        aux = String.valueOf(entity.getId())+";"+entity.getFirstName()+";"+entity.getLastName()+";"+entity.getUserName()+";"+entity.geteMail()+";"+entity.getPassword();
        return aux;
    }
}
