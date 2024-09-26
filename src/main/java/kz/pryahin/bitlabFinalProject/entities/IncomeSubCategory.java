package kz.pryahin.bitlabFinalProject.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "income_sub_categories")

@Data
public class IncomeSubCategory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

}
