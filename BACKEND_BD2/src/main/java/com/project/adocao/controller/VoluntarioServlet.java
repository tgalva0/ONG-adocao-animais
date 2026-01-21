package com.project.adocao.controller;

import com.google.gson.Gson;
import com.project.adocao.dto.VoluntarioAtividadeDTO;
import com.project.adocao.model.Voluntario;
import com.project.adocao.service.VoluntarioService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;
import java.util.stream.Collectors;

@WebServlet("/voluntarios/*")
public class VoluntarioServlet extends HttpServlet {

    private VoluntarioService voluntarioService;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        this.voluntarioService = new VoluntarioService();
        this.gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();
        String pathInfo = req.getPathInfo();

        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                out.print(gson.toJson(voluntarioService.listarTodosVoluntarios()));

            } else if (pathInfo.startsWith("/atividade/")) {
                String nome = pathInfo.substring("/atividade/".length());

                Optional<VoluntarioAtividadeDTO> dto = voluntarioService.getAtividadeVoluntario(nome);
                if(dto.isPresent()) {
                    out.print(gson.toJson(dto.get()));
                } else {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.print("{\"error\": \"Atividade não encontrada para o voluntário: " + nome + "\"}");
                }

            } else {
                int id = Integer.parseInt(pathInfo.substring(1));
                Voluntario voluntario = voluntarioService.buscarVoluntarioPorId(id)
                        .orElseThrow(() -> new RuntimeException("Voluntário não encontrado"));
                out.print(gson.toJson(voluntario));
            }

        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\": \"ID inválido\"}");
        } catch (IllegalArgumentException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\": \"" + e.getMessage() + "\"}");
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
            Voluntario voluntario = gson.fromJson(body, Voluntario.class);
            Voluntario novoVoluntario = voluntarioService.criarVoluntario(voluntario);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().print(gson.toJson(novoVoluntario));

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
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print("{\"error\": \"ID do voluntário é obrigatório para atualização\"}");
            return;
        }

        try {
            int id = Integer.parseInt(pathInfo.substring(1));
            String body = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            Voluntario voluntario = gson.fromJson(body, Voluntario.class);
            voluntario.setIdVoluntario(id);

            voluntarioService.atualizarVoluntario(voluntario);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().print(gson.toJson(voluntario));

        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print("{\"error\": \"ID inválido\"}");
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
            resp.getWriter().print("{\"error\": \"ID do voluntário é obrigatório para deleção\"}");
            return;
        }

        try {
            int id = Integer.parseInt(pathInfo.substring(1));
            voluntarioService.deletarVoluntario(id);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().print("{\"message\": \"Voluntário deletado com sucesso\"}");

        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print("{\"error\": \"ID inválido\"}");
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().print("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
}