const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true, 
  configureWebpack:{
      resolve:{fallback:{fs:false}}
  },
  devServer: {
    proxy: {
        '/geoserver': {
            target: 'http://localhost:8080',
            changeOrigin: true,             //是否跨域
                ws: true,                       //是否代理 websockets
                secure: true,                   //是否https接口
            pathRewrite:{'^/geoserver':''}
            }
        }
    }
})
