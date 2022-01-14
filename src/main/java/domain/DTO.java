package domain;

import java.time.LocalDateTime;

public class DTO {
    private final String firstName;

    public String getFirstNameDto() {
        return firstName;
    }

    public String getLastNameDto() {
        return lastName;
    }

    public LocalDateTime getDateDto() {
        return date;
    }

    private final String lastName;
    private final LocalDateTime date;


    public DTO(String firstName, String lastName, LocalDateTime date) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.date = date;
    }



    @Override
    public String toString() {
        return "First name: "+ firstName + " | " + "Last name: " + lastName + " | " + "Date: " + date.toString();
    }
}

