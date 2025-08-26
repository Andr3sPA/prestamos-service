package co.com.bancolombia.api;

import co.com.bancolombia.api.config.LoanAppPath;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
public class RouterRest {

    private final LoanAppPath loanAppPath;

    @Bean
    @RouterOperation(
            path = "/api/v1/solicitud",
            produces = { MediaType.APPLICATION_JSON_VALUE },
            method = RequestMethod.POST,
            beanClass = HandlerLoanApp.class,
            beanMethod = "saveLoanApp",
            operation = @Operation(
                    operationId = "saveLoanApp",
                    summary = "Crear solicitud de préstamo",
                    description = "Crea una nueva solicitud de préstamo en el sistema",
                    responses = {
                            @ApiResponse(responseCode = "200", description = "Solicitud creada"),
                            @ApiResponse(responseCode = "400", description = "Datos inválidos")
                    }
            )
    )
    public RouterFunction<ServerResponse> routerFunction(HandlerLoanApp handlerLoanApp) {
        return route(POST(loanAppPath.getLoanApplication()), handlerLoanApp::saveLoanApp);
    }
}
