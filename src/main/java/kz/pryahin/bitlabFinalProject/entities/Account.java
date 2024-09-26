package kz.pryahin.bitlabFinalProject.entities;

import jakarta.persistence.*;
import kz.pryahin.bitlabFinalProject.enums.AccountType;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "accounts")

@Data
public class Account {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	private BigDecimal balance;

	private AccountType accountType;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, orphanRemoval = true)
	@JoinColumn(name = "account_id")
	private List<Expense> expenses;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, orphanRemoval = true)
	@JoinColumn(name = "account_id")
	private List<Income> incomes;

}
