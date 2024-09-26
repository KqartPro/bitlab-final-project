package kz.pryahin.bitlabFinalProject.security.entities;

import jakarta.persistence.*;
import kz.pryahin.bitlabFinalProject.entities.Ledger;
import kz.pryahin.bitlabFinalProject.entities.Task;
import lombok.Data;

import java.util.Collection;
import java.util.Date;

@Entity
@Table(name = "backup_users")

@Data
public class BackupUser {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String surname;

	@Column(unique = true, nullable = false)
	private String email;

	@Column(nullable = false)
	private String password;

	private String code;

	private boolean verificationStatus;

	@Column(nullable = false)
	private Date expiredDate;

	@ManyToMany(fetch = FetchType.EAGER)
	private Collection<Role> roles;

	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "backup_user_id")
	private Collection<Ledger> ledgers;

	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "backup_user_id")
	private Collection<Task> tasks;

}
