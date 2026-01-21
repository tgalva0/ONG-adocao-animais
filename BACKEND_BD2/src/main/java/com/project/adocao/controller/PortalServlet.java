package com.project.adocao.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.project.adocao.model.Portal;
import com.project.adocao.service.PortalService;
import com.project.adocao.util.LocalDateTypeAdapter;

// IMPORTS JAKARTA
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
// FIM IMPORTS JAKARTA

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.stream.Collectors;

@WebServlet("/portal/*")
public class PortalServlet extends HttpServlet {

    private PortalService portalService;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        this.portalService = new PortalService();

        // Configuração para suportar LocalDate no JSON
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
                out.print(gson.toJson(portalService.listarTodosRegistros()));
            } else {
                int id = Integer.parseInt(pathInfo.substring(1));
                Portal portal = portalService.buscarPortalPorId(id)
                        .orElseThrow(() -> new RuntimeException("Registro não encontrado"));
                out.print(gson.toJson(portal));
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
            // Gson deserializa os novos campos double automaticamente
            Portal portal = gson.fromJson(body, Portal.class);
            portalService.criarRegistroPortal(portal);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().print(gson.toJson(portal));

        } catch (IllegalArgumentException e) {
            // Captura erros de validação (valores negativos)
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
            Portal portal = gson.fromJson(body, Portal.class);
            portal.setIdPortal(id);

            portalService.atualizarPortal(portal);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().print(gson.toJson(portal));

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
        // ... (lógica do doDelete permanece a mesma) ...
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
            portalService.deletarPortal(id);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().print("{\"message\": \"Registro deletado com sucesso\"}");
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print("{\"error\": \"ID inválido\"}");
        }
    }
}