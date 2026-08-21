package com.makers.prestamos.infrastructure.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.makers.prestamos.application.dto.DecisionPrestamoRequest;
import com.makers.prestamos.application.dto.LoginRequest;
import com.makers.prestamos.application.dto.SolicitudPrestamoRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PrestamoApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void clienteSolicitaYAdminAprueba() throws Exception {
        String tokenMaria = login("usuario@test.com", "123");
        String tokenAdmin = login("admin@test.com", "123");

        SolicitudPrestamoRequest solicitud = new SolicitudPrestamoRequest();
        solicitud.setMonto(new BigDecimal("3500000"));
        solicitud.setPlazoMeses(18);

        MvcResult creado = mockMvc.perform(post("/api/prestamos")
                        .header("Authorization", "Bearer " + tokenMaria)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(solicitud)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.estado", is("PENDIENTE")))
                .andReturn();

        long id = mapper.readTree(creado.getResponse().getContentAsString()).get("id").asLong();

        mockMvc.perform(get("/api/prestamos/{id}/estado", id)
                        .header("Authorization", "Bearer " + tokenMaria))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado", is("PENDIENTE")));

        DecisionPrestamoRequest decision = new DecisionPrestamoRequest();
        decision.setAprobar(true);
        decision.setComentario("Ingresos suficientes");

        mockMvc.perform(put("/api/prestamos/{id}/decision", id)
                        .header("Authorization", "Bearer " + tokenAdmin)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(decision)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado", is("APROBADO")));
    }

    @Test
    void clienteNoPuedeAprobar() throws Exception {
        String tokenMaria = login("usuario@test.com", "123");

        mockMvc.perform(put("/api/prestamos/1/decision")
                        .header("Authorization", "Bearer " + tokenMaria)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"aprobar\":true}"))
                .andExpect(status().isForbidden());
    }

    @Test
    void montoInvalidoDevuelve400() throws Exception {
        String tokenMaria = login("usuario@test.com", "123");
        mockMvc.perform(post("/api/prestamos")
                        .header("Authorization", "Bearer " + tokenMaria)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"monto\":0,\"plazoMeses\":12}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.codigo", is("VALIDACION")));
    }

    private String login(String user, String pass) throws Exception {
        LoginRequest request = new LoginRequest();
        request.setUsername(user);
        request.setPassword(pass);
        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();
        return mapper.readTree(result.getResponse().getContentAsString()).get("token").asText();
    }
}
