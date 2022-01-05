package domain.validation;


import domain.User;

public class UserValidator implements Validator<User> {

    public boolean validateID(Long id){
        if(id<0)
            return false;
        return true;
    }

    public boolean validateName(String fName){
        if(fName.equals("")) return false;
        if(fName.contains("[0-9]+")) return false;
        if(Character.isUpperCase(fName.charAt(0))==false) return false;
        return true;
    }

    public boolean validateUserName(String username){
        if(username!=null&&username.length()<6) return false;
        if(username!=null&&username.isEmpty()==true) return false;
        return true;
    }

    public boolean validateEmail(String email){
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);

    }
    public boolean validatepassword(String password){
        if(password.length()<8)
            return false;
        return true;
    }

    @Override
    public void validate(User entity) throws ValidationException {
        String aux = new String();
        if(validateID(entity.getId()) == false) aux += "Id can't be negative!\n";
        if(validateName(entity.getFirstName())==false) aux += "Enter a valid first name(only letters,not null,first letter uppercase)!\n";
        if(validateName(entity.getLastName())==false) aux += "Enter a valid last name(only letters,not null,first letter uppercase)!\n";
        if(validateUserName(entity.getUserName())==false) aux+= "Enter a valid username(not null,minimum 6 characters)!\n";
        if(validateEmail(entity.geteMail())==false) aux += "Enter a valid email!\n";
        if(validatepassword(entity.getPassword())==false) aux += "Password must have minimum 8 characters!";
        if(!aux.isEmpty()) throw new ValidationException(aux);
    }
}
