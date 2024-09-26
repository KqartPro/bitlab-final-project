package kz.pryahin.bitlabFinalProject.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "expense_sub_categories")

@Data
public class ExpenseSubCategory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;
}
