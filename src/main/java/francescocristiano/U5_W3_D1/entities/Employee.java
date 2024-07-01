package francescocristiano.U5_W3_D1.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
public class Employee {
    @Id
    @GeneratedValue
    private UUID id;
    @Setter
    private String username;
    @Setter
    private String name;
    @Setter
    private String surname;
    @Setter
    private String email;
    @Setter
    private String avatarUrl;

    @OneToMany(mappedBy = "employee")
/*
    @JsonManagedReference
*/
    @JsonBackReference // Annotazione trovata che permette a delle entità bidirezionali di non andare in StackOverFlow,
    // ho invertito la referenza per il tipo di applicazione che volevo creare, ovvero potendo vedere all'interno della
    // lista di device l'id dell'utente a cui facessero riferimento e non viceversa
    private List<Device> devices;


    public Employee(String username, String name, String surname, String email) {
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.avatarUrl = "https://ui-avatars.com/api/?name=" + name + "+" + surname;
    }

}
