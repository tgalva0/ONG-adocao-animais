package com.project.adocao.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.project.adocao.exception.AnimalIndisponivelException;
import com.project.adocao.model.Adocao;
import com.project.adocao.service.AdocaoService;
import com.project.adocao.util.LocalDateTypeAdapter;

// Imports Jakarta
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.stream.Collectors;

@WebServlet("/adocoes/*")
public class AdocaoServlet extends HttpServlet {

    private AdocaoService adocaoService;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        this.adocaoService = new AdocaoService();
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
                out.print(gson.toJson(adocaoService.listarTodasAdocoes()));
            } else if (pathInfo.startsWith("/usuario/")) {
                int idUser = Integer.parseInt(pathInfo.substring("/usuario/".length()));
                out.print(gson.toJson(adocaoService.listarAdocoesPorUsuario(idUser)));
            } else {
                int id = Integer.parseInt(pathInfo.substring(1));
                Adocao adocao = adocaoService.buscarAdocaoPorId(id)
                        .orElseThrow(() -> new RuntimeException("Adoção não encontrada"));
                out.print(gson.toJson(adocao));
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
            Adocao adocao = gson.fromJson(body, Adocao.class);
            adocaoService.registrarAdocao(adocao);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().print(gson.toJson(adocao));

        } catch (IllegalArgumentException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print("{\"error\": \"" + e.getMessage() + "\"}");

        } catch (AnimalIndisponivelException e) { // 🚨 NOVA EXCEÇÃO
            // 409 Conflict: O estado do recurso (Animal) conflitua com o pedido
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
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
            Adocao adocao = gson.fromJson(body, Adocao.class);
            adocao.setIdAdocao(id);

            // Permite atualizar (ex: mudar status para 'Deferido')
            adocaoService.atualizarAdocao(adocao);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().print(gson.toJson(adocao));

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
            adocaoService.deletarAdocao(id);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().print("{\"message\": \"Adoção deletada com sucesso\"}");
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print("{\"error\": \"ID inválido\"}");
        }
    }
}