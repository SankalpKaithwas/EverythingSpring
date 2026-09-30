package com.sankalp;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class SquareServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

		HttpSession session = request.getSession();
		session.setAttribute("numberToSquare", Integer.parseInt(request.getParameter("numberToSquare")));
		int result = (int) (session.getAttribute("numberToSquare"));
		result = result * result;
		PrintWriter out = response.getWriter();
		out.println("We are calculating Square of a number in SquareServlet which is " + result);

		out.print("Hi ");
		ServletContext context = getServletContext();
		ServletConfig config = getServletConfig();

		/**
		 * IMPORTANT ABOVE : ServletContext object is given by tomcat and to access that
		 * object we use getServletContext()
		 */
		String initParameter = config.getInitParameter("name");
		out.println("config.getInitParameter - " + initParameter + " context.getInitParameter - "
				+ context.getInitParameter("company"));

		System.out.println("square");
	}
}
