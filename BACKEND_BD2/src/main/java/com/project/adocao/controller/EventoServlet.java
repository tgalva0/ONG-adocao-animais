package com.project.adocao.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.project.adocao.model.Evento;
import com.project.adocao.service.EventoService;
import com.project.adocao.util.LocalDateTypeAdapter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.stream.Collectors;

@WebServlet("/eventos/*")
public class EventoServlet extends HttpServlet {

    private EventoService eventoService;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        this.eventoService = new EventoService();

        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
                .create();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();
        String pathInfo = req.getPathInfo();

        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                out.print(gson.toJson(eventoService.listarTodosEventos()));
            } else {
                int id = Integer.parseInt(pathInfo.substring(1));
                Evento evento = eventoService.buscarEventoPorId(id)
                        .orElseThrow(() -> new RuntimeException("Evento não encontrado"));
                out.print(gson.toJson(evento));
            }
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\": \"ID inválido\"}");
        } catch (RuntimeException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            out.print("{\"error\": \"" + e.getMessage() + "\"}");
        }
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String body = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            Evento evento = gson.fromJson(body, Evento.class);
            eventoService.criarEvento(evento);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().print(gson.toJson(evento));

        } catch (IllegalArgumentException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print("{\"error\": \"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().print("{\"error\": \"Erro interno: " + e.getMessage() + "\"}");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String body = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print("{\"error\": \"ID é obrigatório\"}");
            return;
        }

        try {
            int id = Integer.parseInt(pathInfo.substring(1));
            Evento evento = gson.fromJson(body, Evento.class);
            evento.setIdEvento(id);

            eventoService.atualizarEvento(evento);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().print(gson.toJson(evento));

        } catch (IllegalArgumentException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print("{\"error\": \"" + e.getMessage() + "\"}");
        } catch (RuntimeException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().print("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print("{\"error\": \"ID é obrigatório\"}");
            return;
        }

        try {
            int id = Integer.parseInt(pathInfo.substring(1));
            eventoService.deletarEvento(id);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().print("{\"message\": \"Evento deletado com sucesso\"}");
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print("{\"error\": \"ID inválido\"}");
        }
    }
}