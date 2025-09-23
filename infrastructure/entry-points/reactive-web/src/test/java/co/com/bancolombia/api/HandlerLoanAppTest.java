package co.com.bancolombia.api;

import co.com.bancolombia.api.util.RequestValidator;
import co.com.bancolombia.dto.LoanApplicationRequest;
import co.com.bancolombia.dto.UpdateLoanAppReq;
import co.com.bancolombia.model.LoanApplication;
import co.com.bancolombia.r2dbc.mapper.LoanApplicationRequestMapper;
import co.com.bancolombia.usecase.loanApplication.LoanAppUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class HandlerLoanAppTest {

    @Mock
    private RequestValidator requestValidator;
    @Mock
    private LoanAppUseCase loanAppUseCase;
    @Mock
    private LoanApplicationRequestMapper requestMapper;

    @InjectMocks
    private HandlerLoanApp handlerLoanApp;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getLoanApps_success() {
        ServerRequest request = mock(ServerRequest.class);
        when(request.queryParam("page")).thenReturn(Optional.of("1"));
        when(request.queryParam("size")).thenReturn(Optional.of("5"));
        LoanApplication loan = new LoanApplication();
        co.com.bancolombia.model.PageResponse<LoanApplication> pageResponse = new co.com.bancolombia.model.PageResponse<>();
        pageResponse.setContent(java.util.Collections.singletonList(loan));
        pageResponse.setPage(1);
        pageResponse.setSize(5);
        pageResponse.setTotalElements(1);
        when(loanAppUseCase.getLoanApps(5, 5, 1)).thenReturn(Mono.just(pageResponse));

        Mono<ServerResponse> response = handlerLoanApp.getLoanApps(request);
        assertEquals(200, response.block().statusCode().value());
    }

    @Test
    void saveLoanApp_success() {
        ServerRequest request = mock(ServerRequest.class);
        ServerRequest.Headers headers = mock(ServerRequest.Headers.class);
        when(request.headers()).thenReturn(headers);
        when(headers.firstHeader("X-User-Email")).thenReturn("test@email.com");
        LoanApplicationRequest dto = LoanApplicationRequest.builder().amount(BigDecimal.TEN).term(12).email("test@email.com").loanTypeId(1L).build();
        when(request.bodyToMono(LoanApplicationRequest.class)).thenReturn(Mono.just(dto));
        LoanApplication model = new LoanApplication();
        when(requestMapper.toModel(dto)).thenReturn(model);
        when(loanAppUseCase.saveLoanApp(model, "test@email.com")).thenReturn(Mono.just(model));

        Mono<ServerResponse> response = handlerLoanApp.saveLoanApp(request);
        assertEquals(200, response.block().statusCode().value());
        verify(requestValidator).validate(dto, LoanApplicationRequest.class);
    }

    @Test
    void saveLoanApp_missingHeader() {
        ServerRequest request = mock(ServerRequest.class);
        ServerRequest.Headers headers = mock(ServerRequest.Headers.class);
        when(request.headers()).thenReturn(headers);
        when(headers.firstHeader("X-User-Email")).thenReturn(null);

        Mono<ServerResponse> response = handlerLoanApp.saveLoanApp(request);
        assertThrows(Exception.class, () -> response.block());
    }

    @Test
    void updateLoanApp_success() {
        ServerRequest request = mock(ServerRequest.class);
        UpdateLoanAppReq dto = UpdateLoanAppReq.builder().id(1L).name(null).build();
        when(request.bodyToMono(UpdateLoanAppReq.class)).thenReturn(Mono.just(dto));
        when(loanAppUseCase.updateLoanApp(1L, String.valueOf(dto.getName()))).thenReturn(Mono.just(new LoanApplication()));

        Mono<ServerResponse> response = handlerLoanApp.updateLoanApp(request);
        assertEquals(200, response.block().statusCode().value());
        verify(requestValidator).validate(dto, UpdateLoanAppReq.class);
    }

    @Test
    void updateLoanApp_empty() {
        ServerRequest request = mock(ServerRequest.class);
        when(request.bodyToMono(UpdateLoanAppReq.class)).thenReturn(Mono.empty());

        Mono<ServerResponse> response = handlerLoanApp.updateLoanApp(request);
        assertEquals(400, response.block().statusCode().value());
    }

    @Test
    void calcularCapacidad_success() {
        ServerRequest request = mock(ServerRequest.class);
        ServerRequest.Headers headers = mock(ServerRequest.Headers.class);
        when(request.headers()).thenReturn(headers);
        when(headers.firstHeader("X-User-Id")).thenReturn("1");
        when(headers.firstHeader("X-User-Salary")).thenReturn("1000.0");
        co.com.bancolombia.dto.CalculateCapacityReq dto = new co.com.bancolombia.dto.CalculateCapacityReq();
        when(request.bodyToMono(co.com.bancolombia.dto.CalculateCapacityReq.class)).thenReturn(Mono.just(dto));
        doNothing().when(requestValidator).validate(dto, co.com.bancolombia.dto.CalculateCapacityReq.class);
        when(loanAppUseCase.calcularCapacidadEndeudamiento(any(), any())).thenReturn(Mono.just(new co.com.bancolombia.model.response.CalcularCapacidadResponse()));

        Mono<ServerResponse> response = handlerLoanApp.calcularCapacidad(request);
        assertEquals(200, response.block().statusCode().value());
    }


}


