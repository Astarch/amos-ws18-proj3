<template>
  <div class="searchbar">
    <form novalidate="novalidate" onsubmit="return false;" class="searchbox">
      <div role="search" class="searchbox_wrapper">
        <input class="searchbar_input"
        type="search" 
        name="search" 
        placeholder="Trendsearch" 
        autocomplete="off" 
        required="required" 
        v-model.trim="searchTerm">
        <button type="submit" title="Submit your search query." class="submit" v-on:click.stop="submit()">
          <i class="ti-search"/>
        </button>
        <button type="reset" title="Clear the search query." class="reset">
          <i class="ti-close"/>
        </button>
      </div>
    </form>
    <div class="searchtype">
    <b>Search type: </b>
    <div class="pretty p-default p-round">
        <input type="radio" name="searchType" v-model="searchType" v-bind:value="'ANY'">
        <div class="state">
            <label>Match any of the terms</label>
        </div>
    </div>

    <div class="pretty p-default p-round">
        <input type="radio" name="searchType" v-model="searchType" v-bind:value="'ALL'">
        <div class="state">
            <label>Match all terms</label>
        </div>
    </div>

    <div class="pretty p-default p-round">
        <input type="radio" name="searchType" v-model="searchType" v-bind:value="'EXACT'">
        <div class="state">
            <label>Exact Match</label>
        </div>
    </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "search-bar",
  props: {
    initialSearchType: {
      validator: function(value) {
        return value === "ANY" || value === "EXACT" || value === "ALL";
      },
      default: function() {
        return "ANY";
      }
    },
    initialSearchTerm: {
      type: String,
      default: function() {
        return "";
      }
    }
  },
  data: function() {
    return {
      searchTerm: this.initialSearchTerm,
      searchType: this.initialSearchType
    };
  },
  watch: {
    initialSearchTerm(newVal, oldValue) {
      this.searchTerm = newVal;
    },
    initialSearchType(newVal, oldValue) {
      this.searchType = newVal;
    }
  },
  methods: {
    submit() {
      if(this.searchTerm.length <= 0){
        return
      }
      let query = this.searchTerm;
      let method = this.searchType;

      console.log("query:" + query);
      console.log("method:" + method);
      this.$emit("querySubmitted", {
        query: query,
        method: method
      });
    }
  }
};
</script>

<style lang="scss" scoped>
@import "~pretty-checkbox/src/pretty-checkbox.scss";

// change radio button color
.pretty.p-default input:checked ~ .state label:after {
  background-color: map_get($theme-colors, secondary) !important;
}
.pretty .state label {
  vertical-align: middle;
}
.searchtype {
  margin: 5px 5px 15px 5px;
}

.searchbox {
  display: inline-block;
  position: relative;
  width: 100%;
  height: 45px;
  white-space: nowrap;
  box-sizing: border-box;
  font-size: 16px;

  .searchbox_wrapper {
    width: 100%;
    height: 100%;
    box-shadow: 0 2px 2px rgba(204, 197, 185, 0.5);
    border: 0;
    border-radius: 6px;
  }
}

.searchbox {
  .searchbox_wrapper {
    .searchbar_input {
      display: inline-block;
      transition: box-shadow 0.4s ease, background 0.4s ease;
      border: 0;
      border-radius: 6px;
      background: #ffffff;
      padding: 0;
      padding-right: 77px;
      padding-left: 18px;
      width: 100%;
      height: 100%;
      vertical-align: middle;
      white-space: normal;
      font-size: inherit;
      appearance: none;

      ::-webkit-search-decoration,
      ::-webkit-search-cancel-button,
      ::-webkit-search-results-button,
      ::-webkit-search-results-decoration {
        display: none;
      }

      :hover {
        box-shadow: inset 0 0 0 1px silver;
      }

      :focus,
      :active {
        outline: 0;
        box-shadow: inset 0 0 0 1px #aaaaaa;
        background: #ffffff;
      }

      ::placeholder {
        color: #aaaaaa;
      }
    }
  }
}

.searchbox {
  .searchbox_wrapper {
    .submit {
      position: absolute;
      top: 0;
      right: 0;
      left: inherit;
      margin: 0;
      border: 0;
      border-radius: 0 3px 3px 0;
      background-color: map_get($theme-colors, secondary);
      padding: 0;
      width: 47px;
      height: 100%;
      vertical-align: middle;
      text-align: center;
      font-size: inherit;
      user-select: none;

      i {
        width: 19px;
        height: 19px;
        vertical-align: middle;
        color: map_get($theme-colors, white);
      }

      :hover,
      :active {
        cursor: pointer;
      }
      :focus {
        outline: 0;
      }
    }
  }
}

.searchbox {
  .searchbox_wrapper {
    .searchbar_input:valid ~ .reset {
      display: block;
      animation-name: reset-in;
      animation-duration: 0.15s;
    }
    .reset {
      display: none;
      position: absolute;
      top: 12px;
      right: 57px;
      margin: 0;
      border: 0;
      background: none;
      cursor: pointer;
      padding: 0;
      font-size: inherit;
      user-select: none;
      fill: rgba(0, 0, 0, 0.5);

      :focus {
        outline: 0;
      }
      i {
        display: block;
        margin: 4px;
        width: 13px;
        height: 13px;
      }
    }
  }
}

@keyframes reset-in {
  0% {
    transform: translate3d(-20%, 0, 0);
    opacity: 0;
  }
  100% {
    transform: none;
    opacity: 1;
  }
}
</style>
