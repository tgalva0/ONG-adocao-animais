package com.project.adocao.controller;

import com.google.gson.Gson;
import com.project.adocao.exception.CpfDuplicadoException;
import com.project.adocao.model.Usuario;
import com.project.adocao.service.UsuarioService;
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

@WebServlet("/usuarios/*")
public class UsuarioServlet extends HttpServlet {

    private UsuarioService usuarioService;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        this.usuarioService = new UsuarioService();
        this.gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        String emailParam = req.getParameter("email");

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();

        try {
            if (emailParam != null && !emailParam.trim().isEmpty()) {
                Optional<Usuario> usuario = usuarioService.buscarUsuarioPorEmail(emailParam);

                if (usuario.isPresent()) {
                    usuario.get().setSenha(null);
                    out.print(gson.toJson(usuario.get()));
                } else {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.print("{\"error\": \"Usuário não encontrado com este e-mail.\"}");
                }

            } else if (pathInfo == null || pathInfo.equals("/")) {
                List<Usuario> usuarios = usuarioService.listarTodosUsuarios();
                usuarios.forEach(u -> u.setSenha(null));
                out.print(gson.toJson(usuarios));

            } else {
                int id = Integer.parseInt(pathInfo.substring(1));
                Optional<Usuario> usuario = usuarioService.buscarUsuarioPorId(id);

                if (usuario.isPresent()) {
                    usuario.get().setSenha(null);
                    out.print(gson.toJson(usuario.get()));
                } else {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.print("{\"error\": \"Usuário não encontrado.\"}");
                }
            }
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\": \"ID inválido\"}");
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\": \"Erro interno ao processar requisição: " + e.getMessage() + "\"}");
        }
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String body = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            Usuario usuario = gson.fromJson(body, Usuario.class);
            usuarioService.criarUsuario(usuario);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().print(gson.toJson(usuario));

        } catch (CpfDuplicadoException e) {
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            resp.getWriter().print("{\"error\": \"" + e.getMessage() + "\"}");
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
        String pathInfo = req.getPathInfo();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        if (pathInfo == null || pathInfo.equals("/")) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print("{\"error\": \"ID do usuário é obrigatório para atualização\"}");
            return;
        }

        try {
            int id = Integer.parseInt(pathInfo.substring(1));
            String body = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            Usuario usuario = gson.fromJson(body, Usuario.class);
            usuario.setIdUsuario(id);
            usuarioService.atualizarUsuario(usuario);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().print("{\"message\": \"Usuário atualizado com sucesso\"}");

        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print("{\"error\": \"ID inválido\"}");
        } catch (CpfDuplicadoException | IllegalArgumentException e) {
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            resp.getWriter().print("{\"error\": \"" + e.getMessage() + "\"}");
        } catch (RuntimeException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().print("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        if (pathInfo == null || pathInfo.equals("/")) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print("{\"error\": \"ID do usuário é obrigatório para deleção\"}");
            return;
        }

        try {
            int id = Integer.parseInt(pathInfo.substring(1));
            usuarioService.deletarUsuario(id);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().print("{\"message\": \"Usuário deletado com sucesso\"}");
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print("{\"error\": \"ID inválido\"}");
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().print("{\"error\": \"Erro ao deletar usuário. Verifique se há registros (animais/eventos) vinculados.\"}" );
        }
    }


}