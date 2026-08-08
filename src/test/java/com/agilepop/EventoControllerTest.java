package com.agilepop;

import com.agilepop.model.Evento;
import com.agilepop.repository.EventoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class EventoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EventoRepository eventoRepository;

    private Evento criarEventoValido(String nome) {
        Evento evento = new Evento();
        evento.setNome(nome);
        evento.setDescricao("Descrição de teste");
        evento.setLocal("São Paulo");
        evento.setCategoria("Tecnologia");
        evento.setDataHora(LocalDateTime.now().plusDays(1));
        evento.setMaxParticipantes(50);
        return evento;
    }

    @Test
    public void deveCriarEventoComSucesso() throws Exception {
        Evento evento = new Evento();
        evento.setNome("AgileConf");
        evento.setDescricao("Conferência de Agilidade");
        evento.setLocal("São Paulo");
        evento.setCategoria("Tecnologia");
        evento.setDataHora(LocalDateTime.now().plusDays(1));
        evento.setMaxParticipantes(100);

        mockMvc.perform(post("/api/eventos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(evento)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("AgileConf"))
                .andExpect(jsonPath("$.descricao").value("Conferência de Agilidade"));
    }

    @Test
    public void deveRetornarErroAoCriarEventoSemTitulo() throws Exception {
        Evento evento = new Evento();
        evento.setDescricao("Conferência de Agilidade");
        evento.setLocal("SP");
        evento.setCategoria("Tecnologia");
        evento.setDataHora(LocalDateTime.now().plusDays(1));
        evento.setMaxParticipantes(50);

        mockMvc.perform(post("/api/eventos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(evento)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.nome").value("O título é obrigatório"));
    }

    @Test
    public void deveRetornarErroAoCriarEventoComDataPassada() throws Exception {
        Evento evento = new Evento();
        evento.setNome("AgileConf");
        evento.setDescricao("Evento passado");
        evento.setLocal("RJ");
        evento.setCategoria("Tecnologia");
        evento.setDataHora(LocalDateTime.now().minusDays(1));
        evento.setMaxParticipantes(10);

        mockMvc.perform(post("/api/eventos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(evento)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.dataHora").value("A data e hora devem ser futuras"));
    }

    @Test
    public void deveRetornarErroAoCriarEventoComDescricaoMuitoLonga() throws Exception {
        Evento evento = new Evento();
        evento.setNome("AgileConf");
        evento.setDescricao("A".repeat(1001));
        evento.setLocal("BH");
        evento.setCategoria("Tech");
        evento.setDataHora(LocalDateTime.now().plusDays(2));
        evento.setMaxParticipantes(30);

        mockMvc.perform(post("/api/eventos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(evento)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.descricao").value("A descrição não pode ter mais de 1000 caracteres"));
    }

    @Test
    public void deveRetornarErroAoCriarEventoComLocalVazio() throws Exception {
        Evento evento = new Evento();
        evento.setNome("AgileConf");
        evento.setDescricao("Evento sem local");
        evento.setLocal(""); // Local inválido
        evento.setCategoria("Tech");
        evento.setDataHora(LocalDateTime.now().plusDays(1));
        evento.setMaxParticipantes(30);

        mockMvc.perform(post("/api/eventos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(evento)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.local").value("O local é obrigatório"));
    }

    @Test
    public void deveListarEventos() throws Exception {
        mockMvc.perform(get("/api/eventos"))
                .andExpect(status().isOk());
    }

    @Test
    public void deveRetornarErroSeTituloEstiverVazio() throws Exception {
        Evento evento = new Evento();
        evento.setNome(""); // inválido
        evento.setDescricao("Descrição válida");
        evento.setLocal("Online");
        evento.setCategoria("Educação");
        evento.setDataHora(LocalDateTime.now().plusDays(2));
        evento.setMaxParticipantes(100);

        mockMvc.perform(post("/api/eventos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(evento)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.nome").value("O título é obrigatório"));
    }

    // ---- GET /api/eventos/{id} ----

    @Test
    public void deveBuscarEventoPorIdComSucesso() throws Exception {
        Evento salvo = eventoRepository.save(criarEventoValido("Evento Buscável"));

        mockMvc.perform(get("/api/eventos/{id}", salvo.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(salvo.getId()))
                .andExpect(jsonPath("$.nome").value("Evento Buscável"));
    }

    @Test
    public void deveRetornar404AoBuscarEventoInexistente() throws Exception {
        mockMvc.perform(get("/api/eventos/{id}", 999999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.erro").value("Evento com ID 999999 não encontrado"));
    }

    // ---- PUT /api/eventos/{id} ----

    @Test
    public void deveAtualizarEventoComSucesso() throws Exception {
        Evento salvo = eventoRepository.save(criarEventoValido("Evento Original"));

        Evento atualizacao = criarEventoValido("Evento Atualizado");
        atualizacao.setLocal("Rio de Janeiro");

        mockMvc.perform(put("/api/eventos/{id}", salvo.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(atualizacao)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(salvo.getId()))
                .andExpect(jsonPath("$.nome").value("Evento Atualizado"))
                .andExpect(jsonPath("$.local").value("Rio de Janeiro"));
    }

    @Test
    public void deveRetornarErroAoAtualizarEventoComDadosInvalidos() throws Exception {
        Evento salvo = eventoRepository.save(criarEventoValido("Evento Original"));

        Evento atualizacaoInvalida = criarEventoValido("");

        mockMvc.perform(put("/api/eventos/{id}", salvo.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(atualizacaoInvalida)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.nome").value("O título é obrigatório"));
    }

    @Test
    public void deveRetornar404AoAtualizarEventoInexistente() throws Exception {
        Evento atualizacao = criarEventoValido("Evento Fantasma");

        mockMvc.perform(put("/api/eventos/{id}", 999999L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(atualizacao)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.erro").value("Evento com ID 999999 não encontrado"));
    }

    // ---- DELETE /api/eventos/{id} ----

    @Test
    public void deveDeletarEventoComSucesso() throws Exception {
        Evento salvo = eventoRepository.save(criarEventoValido("Evento a Deletar"));

        mockMvc.perform(delete("/api/eventos/{id}", salvo.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/eventos/{id}", salvo.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deveRetornar404AoDeletarEventoInexistente() throws Exception {
        mockMvc.perform(delete("/api/eventos/{id}", 999999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.erro").value("Evento com ID 999999 não encontrado"));
    }
}