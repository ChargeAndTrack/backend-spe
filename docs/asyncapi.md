---

title: AsyncAPI
nav_order: 2
parent: API

---

<div id="asyncapi"></div>

<link rel="stylesheet" href="https://unpkg.com/@asyncapi/react-component@latest/styles/default.min.css">

<script src="https://unpkg.com/@asyncapi/react-component@latest/browser/standalone/index.js"></script>

<script>
  window.onload = function() {
    AsyncApiStandalone.render({
      schema: {
        url: "./assets/asyncapi.yaml"
      },
      config: {
        show: {
          sidebar: true
        }
      }
    }, document.getElementById('asyncapi'));
  };
</script>