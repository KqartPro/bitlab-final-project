package kz.pryahin.bitlabFinalProject.entities;

import jakarta.persistence.*;
import kz.pryahin.bitlabFinalProject.enums.Priority;
import kz.pryahin.bitlabFinalProject.security.entities.BackupUser;
import kz.pryahin.bitlabFinalProject.security.entities.User;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")

@Data
public class Task {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@CreationTimestamp
	@Column(updatable = false)
	private LocalDateTime createdAt;

	@UpdateTimestamp
	private LocalDateTime updatedAt;

	@Column(nullable = false)
	private Boolean isCompleted;

	private Priority priority;

	// Связки

	@ManyToOne(fetch = FetchType.EAGER)
	private User user;

	@ManyToOne(fetch = FetchType.EAGER)
	private BackupUser backupUser;
}