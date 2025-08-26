package co.com.bancolombia.r2dbc.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table("states")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StateEntity {

    @Id
    private Long id;

    @NotBlank(message = "State name cannot be empty")
    @Column("name")
    private String name;

    @Column("description")
    private String description;
}
