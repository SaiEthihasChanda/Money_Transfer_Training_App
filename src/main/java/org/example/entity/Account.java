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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private long id;
    private String holderName;
    private String status;
    @Column(name = "last_updated_at")
    private LocalDateTime lastupdatedAt;

    private int balance;


    @Override
    public String toString() {
        return "Todo{" +
                "id=" + id +
                ", Name='" + holderName + '\'' +

                ", updatedAt=" + lastupdatedAt +
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
