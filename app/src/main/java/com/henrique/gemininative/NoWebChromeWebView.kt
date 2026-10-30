package com.henrique.gemininative

import android.content.Context
import android.util.AttributeSet
import android.view.ActionMode
import android.webkit.WebView

/**
 * WebView sem modos de ação (ActionMode).
 *
 * Ao retornar null em startActionMode, nenhuma fita flutuante de seleção de
 * texto é criada, nem pelo sistema, nem pelo conteúdo da página. Isso
 * elimina as alças azuis e o menu de ações que aparece ao manter o texto
 * pressionado.
 */
class NoWebChromeWebView : WebView {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
        super(context, attrs, defStyleAttr)

    override fun startActionMode(callback: ActionMode.Callback): ActionMode? = null

    override fun startActionMode(callback: ActionMode.Callback, type: Int): ActionMode? = null
}
