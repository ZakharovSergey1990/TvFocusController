package com.example.mytvsample

class FocusNode(override val id: String) : Focusable {
    private var parent: Focusable? = null
    private var isFocused: Boolean = false

    override fun moveFocus(
        direction: Direction,
        onLeft: () -> Unit,
        onRight: () -> Unit,
        onTop: () -> Unit,
        onDown: () -> Unit
    ): Focusable? {
        return parent?.moveFocus(direction)
    }

    override fun setFocus(focused: Boolean): Focusable {
        isFocused = focused
        return this
    }

    override fun hasFocus(): Boolean = isFocused

    override fun getChildren(): List<Focusable> = emptyList()

    override fun setParent(parent: Focusable?) {
        this.parent = parent
    }

    override fun focusedId(): String? {
        return if(isFocused) id else null
    }

    override fun getParent(): Focusable? = parent
}


class Row(override val id: String) : Focusable {
    private val children = mutableListOf<Focusable>()
    private var parent: Focusable? = null
    private var focusedIndex: Int = 0  // Индекс фокусированного элемента, сохраняется при переключении

    override fun moveFocus(direction: Direction,
                           onLeft: ()-> Unit,
                           onRight: ()-> Unit,
                           onTop: ()-> Unit,
                           onDown: ()-> Unit,
                           ): Focusable? {
        return when (direction) {
            Direction.LEFT -> {
                if (focusedIndex > 0) {
                    focusedIndex--
                    children.forEach { it.setFocus(false) }
                    children[focusedIndex].setFocus(true)
                    children[focusedIndex]
                } else {
                    onLeft()
                    null
                }
            }
            Direction.RIGHT -> {
                if (focusedIndex < children.size - 1) {
                    focusedIndex++
                    children.forEach { it.setFocus(false) }
                    children[focusedIndex].setFocus(true)
                    children[focusedIndex]
                } else {
                    onRight()
                    null
                }
            }
            else -> {
                // Возвращаем null, чтобы колонна могла обработать переключение вверх/вниз
                return null
            }
        }
        // Установить фокус на текущем элементе
    }

    override fun setFocus(focused: Boolean): Focusable {
        // При переключении фокуса на ряд, передаем фокус непосредственно активному элементу
        return children[focusedIndex].setFocus(focused)

    }

    override fun focusedId(): String? {
        return children[focusedIndex].focusedId()
    }

    override fun hasFocus(): Boolean = children.any { it.hasFocus() }

    override fun getChildren(): List<Focusable> = children

    override fun setParent(parent: Focusable?) {
        this.parent = parent
    }

    override fun getParent(): Focusable? = parent

    fun addNode(node: Focusable) {
        children.add(node)
        node.setParent(this)
    }
}

class FColumn(override val id: String) : Focusable {
    private val children = mutableListOf<Focusable>()
    private var parent: Focusable? = null
    private var focusedIndex: Int = 0  // Сохраняет индекс последнего фокусированного ряда

    override fun moveFocus(
        direction: Direction,
        onLeft: () -> Unit,
        onRight: () -> Unit,
        onTop: () -> Unit,
        onDown: () -> Unit
    ): Focusable? {
        when (direction) {
            Direction.UP -> if (focusedIndex > 0) focusedIndex--
            Direction.DOWN -> if (focusedIndex < children.size - 1) focusedIndex++
            else -> {
                // Переключение фокуса внутри ряда
                return children[focusedIndex].moveFocus(direction, onLeft, onRight)
            }
        }
        // Восстановить фокус внутри ряда на активном элементе
        return children[focusedIndex].setFocus(true)
    }

    override fun setFocus(focused: Boolean): Focusable {
        return children[focusedIndex].setFocus(focused)
    }

    override fun hasFocus(): Boolean = children.any { it.hasFocus() }

    override fun getChildren(): List<Focusable> = children

    override fun setParent(parent: Focusable?) {
        this.parent = parent
    }

    override fun getParent(): Focusable? = parent

    override fun focusedId(): String? {
        return children[focusedIndex].focusedId()
    }

    fun addRow(row: Focusable) {
        children.add(row)
        row.setParent(this)
    }
}

interface Focusable {
    val id: String
    fun moveFocus(direction: Direction,
                  onLeft: ()-> Unit = {},
                  onRight: ()-> Unit = {},
                  onTop: ()-> Unit = {},
                  onDown: ()-> Unit = {},): Focusable?
    fun setFocus(focused: Boolean): Focusable
    fun hasFocus(): Boolean
    fun getChildren(): List<Focusable>
    fun setParent(parent: Focusable?)
    fun getParent(): Focusable?
    fun focusedId(): String?
}

class CircleRow(override val id: String) : Focusable {
    private val children = mutableListOf<Focusable>()
    private var parent: Focusable? = null
    private var focusedIndex: Int = 0  // Индекс фокусированного элемента, сохраняется при переключении

    override fun moveFocus(
        direction: Direction,
        onLeft: () -> Unit,
        onRight: () -> Unit,
        onTop: () -> Unit,
        onDown: () -> Unit
    ): Focusable? {
        when (direction) {
            Direction.LEFT -> if (focusedIndex > 0) focusedIndex-- else focusedIndex = children.lastIndex
            Direction.RIGHT -> if (focusedIndex < children.size - 1) focusedIndex++ else focusedIndex = 0
            else -> {
                // Возвращаем null, чтобы колонна могла обработать переключение вверх/вниз
                return null
            }
        }
        // Установить фокус на текущем элементе
        children.forEach { it.setFocus(false) }
        children[focusedIndex].setFocus(true)
        return children[focusedIndex]
    }

    override fun setFocus(focused: Boolean): Focusable {
        // При переключении фокуса на ряд, передаем фокус непосредственно активному элементу
        return children[focusedIndex].setFocus(focused)

    }

    override fun focusedId(): String? {
        return children[focusedIndex].focusedId()
    }

    override fun hasFocus(): Boolean = children.any { it.hasFocus() }

    override fun getChildren(): List<Focusable> = children

    override fun setParent(parent: Focusable?) {
        this.parent = parent
    }

    override fun getParent(): Focusable? = parent

    fun addNode(node: Focusable) {
        children.add(node)
        node.setParent(this)
    }
}