package kz.pryahin.bitlabFinalProject.entities;

import jakarta.persistence.*;
import kz.pryahin.bitlabFinalProject.security.entities.BackupUser;
import kz.pryahin.bitlabFinalProject.security.entities.User;
import lombok.Data;

import java.util.Currency;
import java.util.List;

@Entity
@Table(name = "ledgers")

@Data
public class Ledger {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private Currency currency;

	// Связи

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, orphanRemoval = true)
	@JoinColumn(name = "ledger_id")
	private List<Account> accounts;

	@ManyToOne(fetch = FetchType.EAGER)
	private User user;

	@ManyToOne(fetch = FetchType.EAGER)
	private BackupUser backupUser;
}



