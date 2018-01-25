<template>
  <div class="card card-user">
    <div class="image">
      <img src="/static/img/background.jpg" alt="...">
    </div>
    <div class="content">
      <div class="author">
        <img class="avatar border-white" src="/static/img/faces/face-2.jpg" alt="...">
        <h4 class="title">{{user.firstname}} {{user.lastname}}
          <br>
          <a href="#">
            <small>@{{user.email}}</small>
          </a>
        </h4>
      </div>
      <p class="description text-center">
        "I like the way you work it
        <br> No diggity
        <br> I wanna bag it up"
      </p>
    </div>
    <hr>
    <div class="text-center">
      <div class="row">
        <div v-for="(info,index) in details" :class="getClasses(index)">
          <h5>{{info.title}}
            <br>
            <small>{{info.subTitle}}</small>
          </h5>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import { mapState } from "vuex";
export default {
  data() {
    return {
      details: [
        {
          title: "12",
          subTitle: "Files"
        },
        {
          title: "2GB",
          subTitle: "Used"
        },
        {
          title: "24,6$",
          subTitle: "Spent"
        }
      ]
    };
  },
  computed: {
    ...mapState({
      // arrow functions can make the code very succinct!
      user: state => state.user.user
    })
  },
  methods: {
    getClasses(index) {
      var remainder = index % 3;
      if (remainder === 0) {
        return "col-md-3 col-md-offset-1";
      } else if (remainder === 2) {
        return "col-md-4";
      } else {
        return "col-md-3";
      }
    }
  }
};
</script>
<style>

</style>
