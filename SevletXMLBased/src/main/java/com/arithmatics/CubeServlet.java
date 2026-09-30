package com.arithmatics;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class CubeServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

//		simplestWay(request, response);

		/**
		 * httpSessionWay is much better in terms of security and speed as it is server
		 * side and does not store data on clients browser as cookies.
		 */
		httpSessionWay(request, response);
//		settingCookie(response, "number", Integer.parseInt(request.getParameter("number")));

//		printingContextAndConfigData(response);

//		sendingToOtherServlet(request, response);
		/**
		 * When we send to other servlet using RequestDispatcher, we will not see output
		 * for previous method output.
		 */
	}

	@SuppressWarnings("unused")
	private void printingContextAndConfigData(HttpServletResponse response) throws IOException {
		PrintWriter writer = response.getWriter();
//		writer.print("Hi ");
		ServletContext context = getServletContext();
		ServletConfig config = getServletConfig();

		/**
		 * IMPORTANT ABOVE : ServletContext object is given by tomcat and to access that
		 * object we use getServletContext(). It is whole application level.
		 */

		String initParameter = config.getInitParameter("name");
		writer.println("config.getInitParameter - " + initParameter + " context.getInitParameter - "
				+ context.getInitParameter("company"));

	}

	@SuppressWarnings("unused")
	private void usingCookieWay(HttpServletRequest request, HttpServletResponse response) throws IOException {
		int result = 0;
		Cookie[] cookies = request.getCookies();
		Object inputAttr = request.getAttribute("inputNumber");
		int inputNumber = (inputAttr != null) ? Integer.parseInt(inputAttr.toString()) : 0;
		// Attribute inputNumber is setBy simplestWay()
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("number".equals(cookie.getName())) {
					// Safely handles both "12" and "12.0"
					result = (int) Double.parseDouble(cookie.getValue());
					break; // Stop after finding the match
				}
			}
		}
		result = result * result * result;
		PrintWriter out = response.getWriter();
		out.println("(usingCookieWay) We are calculating cube of " + inputNumber + " which is " + result);
		System.out.println("Cube cookie");

	}

	/**
	 * Calling usingCookieWay(request, response) directly after
	 * settingCookie(response, ...) within the same execution of doGet will fail
	 * because request.getCookies() only reads headers that the browser sent before
	 * the method began executing. On the initial request, request.getCookies() is
	 * either null (causing a NullPointerException on for (Cookie cookie : cookies))
	 * or missing the new cookie.
	 */

	private void httpSessionWay(HttpServletRequest request, HttpServletResponse response) throws IOException {
		/**
		 * We use session to complete ALL business logic within one session. So, this
		 * method might be called after another method has used setAttribute() to set
		 * some value for further calculation, i.e. cube calculation.
		 */
		HttpSession session = request.getSession();
		/** Use same session and pass to square */
		int result = Integer.parseInt(request.getParameter("number"));
		session.setAttribute("number", result);
		result = result * result * result;

		session.setAttribute("cubeResult", result);
		ServletContext context = getServletContext();
		ServletConfig config = getServletConfig();

//		session.setAttribute("config", config);
//		session.setAttribute("context", context);
		request.setAttribute("config", config);
		request.setAttribute("context", context);
		response.sendRedirect("square");

	}

	public static void simplestWay(HttpServletRequest request, HttpServletResponse response) throws IOException {
		int number = Integer.parseInt(request.getParameter("number"));
		double cube = Math.pow(number, 3);
		PrintWriter out = response.getWriter();
		out.println("(SimplestWay) We are calculating Cube of a number which is " + cube);
	}

	@SuppressWarnings("unused")
	private static void sendingToOtherServlet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int number = Integer.parseInt(request.getParameter("number"));
		settingAttribute(request, "number", number);// For sending number value to squareServlet

		// Sending one request to another servlet is by using (RequestDispatcher)
		RequestDispatcher requestDispatcher = request.getRequestDispatcher("addition");
//		requestDispatcher.forward(request, response);
		requestDispatcher.include(request, response);
		/**
		 * include() cube servlet's output directly into this same response of addition.
		 * So we will see output of all calculation of "CubeServlet" along with
		 * "AddServlet" output. But if we use forward(), only "AddServlet"(addition)
		 * output will be shown.
		 */

	}

	@SuppressWarnings("unused")
	private static void settingCookie(HttpServletResponse response, String attribute, int value) throws IOException {
		Cookie cookie = new Cookie(attribute, String.valueOf(value));
		cookie.setPath("/");
		cookie.setMaxAge(60 * 60); // Optional: keep for 1 hour
		response.addCookie(cookie);
//		response.sendRedirect("addition");
//		return; 
		// sendRedirect("square") - Triggers a brand new HTTP GET request
		// Terminate execution so nothing else writes to the response AND navigate away
		// to /square immediately."
		/**
		 * sendRedirect -> The next request will contain the cookie in
		 * request.getCookies()
		 */
		/**
		 * When we send to other servlet using sendRedirect("square"), we will not see
		 * output for previous method output because we are redirected.
		 */

	}

	public static void settingAttribute(HttpServletRequest request, String attribute, int value) {
		request.setAttribute(attribute, value);
	}

}
