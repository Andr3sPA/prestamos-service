package co.com.bancolombia.r2dbc.aws;

import co.com.bancolombia.model.request.CalcularCapacidadRequest;
import co.com.bancolombia.model.response.CalcularCapacidadResponse;
import com.amazonaws.services.lambda.invoke.LambdaFunction;
import org.springframework.stereotype.Service;

@Service
public interface CapacidadEndeudService {
    @LambdaFunction(functionName="CalcularCapacidadEndeudamiento")
    CalcularCapacidadResponse calcularCapacidadEndeudamiento(CalcularCapacidadRequest payload);
}

