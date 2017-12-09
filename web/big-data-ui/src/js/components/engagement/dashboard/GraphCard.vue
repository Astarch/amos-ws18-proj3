<template>
  <div class="card">
    <div class="header">
      <slot name="title"></slot>
      <p class="category">
        <slot name="subTitle"></slot>
      </p>
    </div>

    <div class="content">

      <slot name="content"></slot>
      <div class="footer">
        <hr/>
        <slot name="footer"></slot>
      </div>
    </div>

  </div>
</template>
<script>
  export default {
    name: 'graph-card',
  }

</script>
<style>

</style>
