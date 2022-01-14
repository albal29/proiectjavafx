package domain.validation;


import domain.User;

public class UserValidator implements Validator<User> {

    public boolean validateID(Long id) {
        return id >= 0;
    }

    public boolean validateName(String fName) {
        if (fName.equals("")) return false;
        if (fName.contains("[0-9]+")) return false;
        return Character.isUpperCase(fName.charAt(0));
    }

    public boolean validateUserName(String username) {
        if (username != null && username.length() < 6) return false;
        return username == null || !username.isEmpty();
    }

    public boolean validateEmail(String email) {
        String regex = "^[\\w-_.+]*[\\w-_.]@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);

    }

    public boolean validatepassword(String password) {
        return password.length() >= 8;
    }

    @Override
    public void validate(User entity) throws ValidationException {
        String aux = "";
        if (!validateID(entity.getId())) aux += "Id can't be negative!\n";
        if (!validateName(entity.getFirstName()))
            aux += "Enter a valid first name(only letters,not null,first letter uppercase)!\n";
        if (!validateName(entity.getLastName()))
            aux += "Enter a valid last name(only letters,not null,first letter uppercase)!\n";
        if (!validateUserName(entity.getUserName())) aux += "Enter a valid username(not null,minimum 6 characters)!\n";
        if (!validateEmail(entity.geteMail())) aux += "Enter a valid email!\n";
        if (!validatepassword(entity.getPassword())) aux += "Password must have minimum 8 characters!";
        if (!aux.isEmpty()) throw new ValidationException(aux);
    }
}
