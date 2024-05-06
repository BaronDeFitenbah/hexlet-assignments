package exercise.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

// BEGIN
@Setter
@Getter
public class GuestCreateDTO {
    @NotBlank
    private String name;

    @Email
    private String email;

    @Pattern(regexp = "^\\+\\d{11,13}$", message = "Phone number must start with '+' and contain 11 to 13 digits")
    private String phoneNumber;

    @Size(min = 4, max = 4, message = "Club card number must consist of exactly four digits")
    private String clubCard;

    @FutureOrPresent(message = "Card validity must not be in the past")
    private LocalDate cardValidUntil;
}

// END
