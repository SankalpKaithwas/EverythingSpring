package com.arithmatics;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AddServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			int number = (int) request.getAttribute("number");
			number += 10;
			PrintWriter out = response.getWriter();
			out.println("(AddServlet Redirected from SquareServlet) We are calculating "
					+ "Addition by \"10\" of a number which is " + number);
		} catch (NumberFormatException e) {
			System.out.println("Cannot parse  - request.getParameter: - " + e);
		} catch (IOException io) {
			System.out.println(io);
		}
	}

}
