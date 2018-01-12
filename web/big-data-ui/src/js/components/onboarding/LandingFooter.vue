<template>
  <!-- Footer -->
  <footer>
    <div class="container">
      <div class="row">
        <div class="col-lg-12">
          <ul class="list-inline">
            <li>
              <a href="#">Home</a>
            </li>
            <li class="footer-menu-divider">&sdot;</li>
            <li>
              <a href="#about">About</a>
            </li>
            <li class="footer-menu-divider">&sdot;</li>
            <li>
              <a href="#services">Services</a>
            </li>
            <li class="footer-menu-divider">&sdot;</li>
            <li>
              <a href="#contact">Contact</a>
            </li>
          </ul>
          <p class="copyright text-muted small">Copyright &copy; pretrendr 2017. All Rights Reserved</p>
        </div>
      </div>
    </div>
  </footer>
</template>

<script>
  export default {
    name: 'landing-footer'
  }


</script>

<style lang="scss" scoped>

  footer {
    padding: 50px 0;
    background-color: #f8f8f8;
  }

  p.copyright {
    margin: 15px 0 0;
  }


</style>
