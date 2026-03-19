---

title: API
nav_order: 4
parent: Report

---

<div id="swagger-ui"></div>

<link rel="stylesheet" href="https://unpkg.com/swagger-ui-dist/swagger-ui.css" />

<script src="https://unpkg.com/swagger-ui-dist/swagger-ui-bundle.js"></script>

<script>
  window.onload = () => {
    SwaggerUIBundle({
      url: "./openapi.yml",
      dom_id: "#swagger-ui"
    });
  };
</script>