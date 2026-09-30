package com.arithmatics;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class SquareServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		usingSameSession(request, response);
//		passedCookieFromCubeServelt(request, response);

	}

	@SuppressWarnings("unused")
	private void passedCookieFromCubeServelt(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		int result = 0;
		Cookie[] cookies = request.getCookies();
		int inputNumber = 0;
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("number".equals(cookie.getName())) {
					// Safely handles both "12" and "12.0"
					inputNumber = (int) Double.parseDouble(cookie.getValue());
					break; // Stop after finding the match
				}
			}
		}
		result = inputNumber * inputNumber * inputNumber;
		PrintWriter out = response.getWriter();
		out.println(
				"(usingCookieWay) (SquareServlet) We are calculating square of " + inputNumber + " which is " + result);
		System.out.println("Square cookie");

	}

	public static void usingSameSession(HttpServletRequest request, HttpServletResponse response) {
		// 1. Fetch existing session (false means do not create a new one if missing)
		HttpSession session = request.getSession(false);
		PrintWriter out;
		try {
			out = response.getWriter();
			if (session != null && session.getAttribute("number") != null) {
				// 2. Read values stored previously by CubeServlet
				int originalNumber = (int) session.getAttribute("number");
				int cubeResult = (int) session.getAttribute("cubeResult");

				// 3. Compute square
				int squareResult = originalNumber * originalNumber;
				out.println("(SquareServlet Redirected usingSameSession from CubeServlet) We are calculating Cube of "
						+ originalNumber + " which is " + cubeResult);
				out.println(
						"(SquareServlet Redirected usingSameSession from CubeServlet) We are calculating Square of a number "
								+ originalNumber + " which is " + squareResult);

				// for addition (AddServlet)
				request.setAttribute("number", originalNumber);
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("addition");
				requestDispatcher.include(request, response);

				// Optional cleanup if you only needed them for this one-time flow:
				session.removeAttribute("cubeResult");
			} else {
				out.println("No session data found.");
			}

		} catch (NumberFormatException e) {
			System.out.println("Cannot parse  - session.getAttribute() :- " + e);
		} catch (IOException io) {
			System.out.println(io);
		} catch (ServletException e) {
			e.printStackTrace();
		}

	}

}
