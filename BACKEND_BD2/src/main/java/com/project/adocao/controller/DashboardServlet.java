package com.project.adocao.controller;

import com.google.gson.Gson;
import com.project.adocao.dto.DashboardStatsDTO;
import com.project.adocao.service.DashboardService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/dashboard/stats")
public class DashboardServlet extends HttpServlet {

    private DashboardService dashboardService;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        this.dashboardService = new DashboardService();
        this.gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            DashboardStatsDTO stats = dashboardService.getEstatisticasGerais();
            resp.getWriter().print(gson.toJson(stats));

        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().print("{\"error\": \"Falha ao carregar estatísticas do dashboard: " + e.getMessage() + "\"}");
        }
    }
}