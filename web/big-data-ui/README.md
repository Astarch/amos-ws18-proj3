# AMOS Frontend Setup




## Build Setup

Precondditions:
1.  NodeJS needs to be installed
2.  (optional) yarn has to be installed (npm works too!)

``` bash
# install dependencies
npm install / yarn

# serve with hot reload at localhost:3000
npm run dev / yarn run dev

# build for production with minification
npm run build / yarn run build
```

- `run dev` will open a dashboard, which shows build progress and informations
about the deployment of the webapp (url, port etc)
- browser-sync is integrated to test the webapp easier on several devices
    + runs on port 3001 (localhost)

### build for production and view the bundle analyzer report
```
npm run build --report / yarn run build --report
```
### run unit tests
``` bash
npm run unit / yarn run unit
```


For detailed explanation on how things work, consult the [docs for vue-loader](http://vuejs.github.io/vue-loader).
=======
