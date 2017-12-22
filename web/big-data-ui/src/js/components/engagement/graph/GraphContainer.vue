<template>
  <div class="row">
    <!-- include child component and pass data -->
    <chart class="col s12" :data="dataset"></chart>
  </div>
</template>
<script>
  import http, {api} from 'js/utils/api';
  import chart from "./Chart"
  import * as d3 from 'd3'

  export default {
    name: 'graphContainer',
    components: {
      chart
    },
    data: function () {
      return {
        dataset: new Array(),
        options: {
        origin: window.location.origin,
        dummypath: '/api/dummy',
        path:'/api/s3/bucket/8cec40c2-f750-48fb-ace2-7c12fa61f88e/wordCount',
        size: '&size=10',
        num: '?number=',
        totalPages : 1,
        user: 'user2',
        pwd: 'pass2'
        }
      };
    },
   computed: {

  },
    created: function () {
      this.doLogin();
    },
    methods: {
        doLogin: function () {
        this.isSubmitting = true;
        api.auth.postLogin(this.options.user, this.options.pwd)
           .then(response => {
             this.isSubmitting = false;
             this.retrieveData(this.options.size, 0);
           })
           .catch(error => {
            console.log(error);
             this.isSubmitting = false;
             let errorCode = error.response.status;
             if (errorCode < 500) {
               this.showLoginError('Login failure - please try again!')
             } else {
               this.showLoginError('Server error - please try again later!')
             }
             console.log(error.response);
           });
      },
      retrieveData: function (size, i) {
       var self = this;
       console.log("retrieveing");
       // CORRECT: if(i==this.options.totalPages) !!!! NOTE !!!! Not activated since duplicate dummy data!!!!
       if(i==5){
        return 0;
       }
      var total_path = this.options.path.concat(this.options.num).concat(String(i)).concat(size);
       api.graph.getData(total_path)
           .then(response => {
            this.options.totalPages = response.data.totalPages;
           (response.data.content).forEach(function (e) {
            var obj = {count : e.count, label : e.word};
            //console.log(obj);
            (self.dataset).push(obj);
           });
           this.retrieveData(size, ++i);
           })
           .catch(error => {
            console.log(error);
             let errorCode = error.response.status;
             if (errorCode < 500) {
               console.log('Failure - please try again!');
             } else {
               console.log('Error - please try again later!');
             }
             console.log(error.response);
           });
      }
    }
  };
</script>
