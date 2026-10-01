<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>${title}</title>
    <style>
        body { font-family: system-ui, sans-serif; margin: 40px; background-color: #f8fafc; }
        .card { background: white; padding: 24px; border-radius: 8px; box-shadow: 0 2px 8px rgba(0,0,0,0.1); max-width: 600px; }
        h1 { color: #1e293b; margin-top: 0; }
        .success { color: #16a34a; font-weight: 600; font-size: 1.1rem; }
        .time { color: #64748b; font-size: 0.9rem; }
        code { background: #e2e8f0; padding: 2px 6px; border-radius: 4px; }
    </style>
</head>
<body>
	<h1>Welcome to Spring MVC</h1>
	<p>${message}</p>
	<div class="card">
        <h1>${title}</h1>
        <p class="success">${status}</p>
        <p class="time">Server Time: ${timestamp}</p>
        <hr/>
        <p>Try testing query parameters: <a href="greet?name=Developer"><code>/greet?name=Developer</code></a></p>
    </div>

</body>
</html>