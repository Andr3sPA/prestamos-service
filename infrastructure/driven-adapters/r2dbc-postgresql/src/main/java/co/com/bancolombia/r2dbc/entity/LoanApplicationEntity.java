package co.com.bancolombia.r2dbc.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Table("loan_application")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanApplicationEntity {

    @Id
    private Long id;

    @Column("amount")
    private BigDecimal amount;

    @Column("term")
    private Integer term;

    @Column("email")
    private String email;

    @Column("state_id")
    private Long stateId;

    @Column("loan_type_id")
    private Long loanTypeId;
}
