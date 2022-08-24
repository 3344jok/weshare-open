import vue from '@vitejs/plugin-vue'

export default {
    base: './',
    plugins: [vue()],
    optimizeDeps: {
        include: ['schart.js']
    },
    server: {
        host: '0.0.0.0',
        port: 8081,
        // cors: true, //默认启用并允许任何源
        https: false,
        // open: true, //在服务器启动时自动在浏览器中打开应用程序
        proxy: { 
            '/api': {
              target: 'http://47.113.194.136:8080/',  //你要跨域访问的网址
              changeOrigin: true,   // 允许跨域
              rewrite: (path) => path.replace(/^\/api/, '') // 重写路径把路径变成空字符
            }

        }
    }
}