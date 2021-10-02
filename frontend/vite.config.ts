import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import {resolve} from 'path'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  build: {
    // 再度ビルドした時に、以前のファイルを消す
    // https://ja.vitejs.dev/config/#build-emptyoutdir
    emptyOutDir: true,
    // https://stackoverflow.com/questions/7083045/fs-how-do-i-locate-a-parent-folder/7083530
    outDir: resolve(__dirname, '..', 'public', 'vite_assets'),
    rollupOptions: {
      // https://vitejs.dev/guide/build.html#multi-page-app
      input: {
        apple: resolve(__dirname, 'src', 'pages', 'apple', 'index.tsx'),
        sweet_potato: resolve(__dirname, 'src', 'pages', 'sweet_potato', 'index.tsx')
      },
      output: {
        // [name] などのプレースホルダに関する説明
        // https://rollupjs.org/guide/en/#outputentryfilenames
        entryFileNames: '[name].js',
        chunkFileNames: '[name].js',
        assetFileNames: '[name][extname]',
      }
    }
  }
})
