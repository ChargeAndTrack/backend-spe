---

title: OpenAPI
nav_order: 1
parent: API

---

<div id="swagger-ui"></div>

<link rel="stylesheet" href="https://unpkg.com/swagger-ui-dist/swagger-ui.css" />

<script src="https://unpkg.com/swagger-ui-dist/swagger-ui-bundle.js"></script>

<script>
  window.onload = () => {
    SwaggerUIBundle({
      url: "./assets/openapi.yml",
      dom_id: "#swagger-ui"
    });
  };
</script>