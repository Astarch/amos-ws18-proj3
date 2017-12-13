<template>
  <div id="git-info" v-if="isActive" class="border border-primary">
    <span>{{branch}} : <i>({{commit}})</i> <b>by</b> <i>{{author}}</i> <b>on</b> <i>{{date}}</i></span>
  </div>
</template>

<script>

  export default {
    name: 'GitInfo',
    props: [
      'isActive'
    ],
    data() {
      return {
        date: '',
        author: '',
        commit: '',
        branch: '',
      }
    },
    mounted() {
      let date = new Date(GIT.DATE).toLocaleString("de-DE");
      this.date = date;
      this.author = GIT.AUTHOR;
      this.commit = GIT.COMMIT;
      this.branch = GIT.BRANCH;


    }
  }
</script>

<style lang="scss">
  #git-info {
    color: grey;
    display: flex;
    flex-direction: row;
    justify-content: center;
  }

  // fix to shift everything down to make space for the gitinfo
  #landing-page .intro-header {
    //padding-top: 80px;
  }

</style>


