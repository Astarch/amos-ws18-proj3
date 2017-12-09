<template>
  <div id="git-info" v-if="isActive" class="border border-primary">
    <!--{{branch}} : <i>({{commit}})</i> <b>by</b> <i>{{author}}</i> <b>on</b> <i>{{date}}</i>-->
  </div>
</template>

<script>

  export default {
    name: 'GitInfo',
    data() {
      return {
        date: '',
        author: '',
        commit: '',
        branch: '',
        isActive: false
      }
    },
    mounted() {
      if (GIT && GIT.BRANCH && !GIT.BRANCH.includes("master")) {
        this.isActive = true;
        let date = new Date(GIT.DATE).toLocaleString("de-DE");
        this.date = date;
        this.author = GIT.AUTHOR;
        this.commit = GIT.COMMIT;
        this.branch = GIT.BRANCH;
      }

    }
  }
</script>

<style lang="scss" scoped>
  #git-info {
    color: white;
  }

</style>


