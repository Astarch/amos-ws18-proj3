<template>
    <div class="row">
      <div class="col-sm-12 col-md-6">
        <user-card>

        </user-card>

      </div>
      <div class="col-sm-12 col-md-6">
        <members-card>

        </members-card>
      </div>
    </div>
</template>
<script>
  import UserCard from './UserProfile/UserCard.vue'
  import MembersCard from './UserProfile/MembersCard.vue'
  export default {
    components: {
      UserCard,
      MembersCard
    }
  }

</script>
<style>

</style>
