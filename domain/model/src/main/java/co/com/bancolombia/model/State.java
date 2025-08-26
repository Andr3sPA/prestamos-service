package co.com.bancolombia.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class State {
    private Long id;
    private String name;
    private String description;
}
