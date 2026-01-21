package com.project.adocao.controller;

import com.google.gson.Gson;
import com.project.adocao.dto.LoginDTO;
import com.project.adocao.model.Usuario;
import com.project.adocao.service.UsuarioService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private UsuarioService usuarioService;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        this.usuarioService = new UsuarioService();
        this.gson = new Gson();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        String body = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

        try {
            LoginDTO loginData = gson.fromJson(body, LoginDTO.class);

            Usuario usuarioLogado = usuarioService.autenticar(loginData.getEmail(), loginData.getSenha());

            usuarioLogado.setSenha(null);

            resp.getWriter().print(gson.toJson(usuarioLogado));

        } catch (IllegalArgumentException e) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().print("{\"error\": \"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().print("{\"error\": \"Erro interno no servidor.\"}");
        }
    }
}