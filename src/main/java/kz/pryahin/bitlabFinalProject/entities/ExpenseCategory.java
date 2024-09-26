package kz.pryahin.bitlabFinalProject.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "expense_categories")

@Data
public class ExpenseCategory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, orphanRemoval = true)
	@JoinColumn(name = "expense_category_id")
	private List<ExpenseSubCategory> expenseSubCategories = new ArrayList<>();
}
