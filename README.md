# Gemini Native

Aplicativo Android nativo, escrito em Kotlin, que apresenta o Gemini
(https://gemini.google.com/app?hl=pt-BR) em uma WebView imersiva. Todo
comportamento típico de navegador foi bloqueado, para que o site se comporte
como um aplicativo nativo.

## Recursos de web bloqueados

| Recurso | Como é bloqueado |
| --- | --- |
| Zoom por pinça e por toque duplo | setSupportZoom(false), builtInZoomControls = false e CSS touch-action: pan-x pan-y |
| Barras de rolagem (nativas e do conteúdo) | isVerticalScrollBarEnabled = false, isHorizontalScrollBarEnabled = false e CSS ::-webkit-scrollbar com display none |
| Menu de contexto ao pressionar e segurar | Long-click consumido pela WebView e evento contextmenu cancelado via JavaScript |
| Fita de seleção de texto (alças azuis) | NoWebChromeWebView retorna null em startActionMode; modos de ação encerrados na Activity; CSS user-select: none |
| Barra de carregamento ao puxar para baixo (pull-to-refresh) | Sem SwipeRefreshLayout, overscroll desativado e bloqueio de touchmove no topo via JavaScript |
| Tremedeiras e efeitos de overscroll | overScrollMode = OVER_SCROLL_NEVER e CSS overscroll-behavior: none |
| Pop-ups e janelas múltiplas | setSupportMultipleWindows(false) |
| Navegação para fora dos domínios do Google | shouldOverrideUrlLoading permite apenas google.com, googleusercontent.com, gstatic.com e g.co |

## Como compilar

1. Instale o Android Studio (versão Hedgehog 2023.1.1 ou mais recente).
2. Abra a pasta raiz deste projeto (File, Open).
3. Gere o Gradle Wrapper, pois o repositório não inclui o binário
   gradle-wrapper.jar: execute "gradle wrapper --gradle-version 8.7" na raiz
   do projeto ou aceite a correção sugerida pelo Android Studio.
4. Execute a configuração "app" em um dispositivo ou emulador com
   Android 8.0 (API 26) ou mais recente.

## Observações importantes

- Login do Google: o app define um User-Agent do Chrome para reduzir bloqueios
  de autenticação em WebViews embutidas. Dependendo das configurações de
  segurança da conta, o Google pode solicitar uma confirmação adicional no
  primeiro acesso.
- Campo de digitação: para que continue possível escrever no chat, o campo de
  texto permanece editável. As alças e a fita de seleção são suprimidas no
  restante do conteúdo da página.

## Estrutura do projeto

- app/src/main/java/com/henrique/gemininative/MainActivity.kt: configura a
  WebView, injeta os bloqueios e controla a navegação.
- app/src/main/java/com/henrique/gemininative/NoWebChromeWebView.kt: WebView
  que elimina os modos de ação (ActionMode) do Android.
- app/src/main/res/layout/activity_main.xml: layout único, sem
  SwipeRefreshLayout e sem barras de progresso.
