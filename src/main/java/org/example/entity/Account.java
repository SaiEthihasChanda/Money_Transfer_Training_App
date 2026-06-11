package org.example.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@Data
@Entity
@Table(name="accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_seq")
    @SequenceGenerator(name = "account_seq", sequenceName = "account_sequence", initialValue = 2002, allocationSize = 1)
    @Column(name="id")
    private long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String holderName;
    private String status;
    @Column(name = "last_updated_at")
    private LocalDateTime lastupdatedAt;

    private int balance;

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", holderName='" + holderName + '\'' +
                ", status='" + status + '\'' +
                ", lastupdatedAt=" + lastupdatedAt +
                ", balance=" + balance +
                '}';
    }

    public void credit(int amount) {
        this.balance += amount;
        this.lastupdatedAt = LocalDateTime.now();
    }

    public void debit(int amount) {
        this.balance -= amount;
        this.lastupdatedAt = LocalDateTime.now();
    }

}
