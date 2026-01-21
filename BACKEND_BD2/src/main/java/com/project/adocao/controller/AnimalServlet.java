package com.project.adocao.controller;

import com.google.gson.Gson;
import com.project.adocao.model.Animal;
import com.project.adocao.service.AnimalService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@WebServlet("/animais/*")
public class AnimalServlet extends HttpServlet {

    private AnimalService animalService;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        this.animalService = new AnimalService();
        this.gson = new Gson();
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        String nomeParam = req.getParameter("nome");
        String especieParam = req.getParameter("especie");

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();

        try {
            List<Animal> animais;

            if ("/disponiveis".equalsIgnoreCase(pathInfo)) {
                // Rota 1: Busca apenas animais DISPONÍVEIS (/animais/disponiveis)
                animais = animalService.listarAnimaisDisponiveis();
                out.print(gson.toJson(animais));

            } else if (nomeParam != null || especieParam != null) {
                // Rota 2: Busca por FILTROS (/animais?nome=...)
                animais = animalService.buscarAnimaisPorFiltro(nomeParam, especieParam);
                out.print(gson.toJson(animais));

            } else if (pathInfo != null && pathInfo.length() > 1) {
                // Rota 3: Busca por ID (/animais/1)
                int id = Integer.parseInt(pathInfo.substring(1));
                Optional<Animal> animal = animalService.buscarAnimalPorId(id);

                if (animal.isPresent()) {
                    out.print(gson.toJson(animal.get()));
                } else {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.print("{\"error\": \"Animal não encontrado\"}");
                }

            } else {
                // Rota 4: Listar TODOS (/animais)
                animais = animalService.listarTodosAnimais();
                out.print(gson.toJson(animais));
            }

        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\": \"ID inválido\"}");
        } catch (RuntimeException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\": \"Erro interno no servidor: " + e.getMessage() + "\"}");
        }
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String body = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

        try {
            Animal animal = gson.fromJson(body, Animal.class);
            animalService.criarAnimal(animal);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().print("{\"message\": \"Animal criado com sucesso\"}");
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print("{\"error\": \"Erro ao criar animal: " + e.getMessage() + "\"}");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print("{\"error\": \"ID do animal é obrigatório para atualização\"}");
            return;
        }

        try {
            int id = Integer.parseInt(pathInfo.substring(1));
            String body = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            Animal animal = gson.fromJson(body, Animal.class);
            animal.setIdAnimal(id);

            animalService.atualizarAnimal(animal);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().print("{\"message\": \"Animal atualizado com sucesso\"}");

        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print("{\"error\": \"ID inválido\"}");
        } catch (RuntimeException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().print("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print("{\"error\": \"ID do animal é obrigatório para deleção\"}");
            return;
        }

        try {
            int id = Integer.parseInt(pathInfo.substring(1));
            animalService.deletarAnimal(id);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().print("{\"message\": \"Animal deletado com sucesso\"}");
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print("{\"error\": \"ID inválido\"}");
        }
    }
}