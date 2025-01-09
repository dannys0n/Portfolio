package UI
fun preprocessLatex(latex: String): String {
    var output = latex

    // Handle inline math delimiters \( ... \)
    val inlineMathRegex = Regex("""\\\((.*?)\\\)""")
    output = inlineMathRegex.replace(output) { matchResult ->
        val inlineContent = matchResult.groupValues[1]
        preprocessLatexContent(inlineContent)
    }

    // Handle display math delimiters \[ ... \]
    val displayMathRegex = Regex("""\\\[(.*?)\\\]""", RegexOption.DOT_MATCHES_ALL)
    output = displayMathRegex.replace(output) { matchResult ->
        val displayContent = matchResult.groupValues[1]
        preprocessLatexContent(displayContent)
    }

    // Process other LaTeX content outside delimiters
    output = preprocessLatexContent(output)

    return output
}

// Function to process LaTeX content inside delimiters
private fun preprocessLatexContent(content: String): String {
    var output = content

    // Replace fractions \frac{a}{b} with a/b
    val fractionRegex = Regex("""\\frac\{(.*?)\}\{(.*?)\}""")
    output = fractionRegex.replace(output) { matchResult ->
        val numerator = matchResult.groupValues[1]
        val denominator = matchResult.groupValues[2]
        "$numerator/$denominator"
    }

    // Replace superscripts (e.g., x^2 -> x²)
    val superscriptRegex = Regex("""(\S)\^(\{.*?\}|\S)""")
    output = superscriptRegex.replace(output) { matchResult ->
        val base = matchResult.groupValues[1]
        val exponent = matchResult.groupValues[2].removeSurrounding("{", "}")
        "$base${convertToSuperscript(exponent)}"
    }

    // Replace subscripts (e.g., x_1 -> x₁)
    val subscriptRegex = Regex("""(\S)_(\{.*?\}|\S)""")
    output = subscriptRegex.replace(output) { matchResult ->
        val base = matchResult.groupValues[1]
        val subscript = matchResult.groupValues[2].removeSurrounding("{", "}")
        "$base${convertToSubscript(subscript)}"
    }

    // Replace integral symbols
    output = output.replace("\\int", "∫")

    // Replace LaTeX math symbols (expand as needed)
    output = output.replace("\\alpha", "α")
        .replace("\\beta", "β")
        .replace("\\gamma", "γ")
        .replace("\\Delta", "Δ")
        .replace("\\pi", "π")
        .replace("\\theta", "θ")
        .replace("\\infty", "∞")
        .replace("\\pm", "±")
        .replace("\\sqrt", "√")

    // Remove remaining braces
    output = output.replace(Regex("""[\{\}]"""), "")

    return output
}

// Helper function to convert numbers/characters to superscripts
private fun convertToSuperscript(text: String): String {
    val superscripts = mapOf(
        '0' to '⁰', '1' to '¹', '2' to '²', '3' to '³', '4' to '⁴',
        '5' to '⁵', '6' to '⁶', '7' to '⁷', '8' to '⁸', '9' to '⁹',
        '+' to '⁺', '-' to '⁻', '=' to '⁼', '(' to '⁽', ')' to '⁾'
    )
    return text.map { superscripts[it] ?: it }.joinToString("")
}

// Helper function to convert numbers/characters to subscripts
private fun convertToSubscript(text: String): String {
    val subscripts = mapOf(
        '0' to '₀', '1' to '₁', '2' to '₂', '3' to '₃', '4' to '₄',
        '5' to '₅', '6' to '₆', '7' to '₇', '8' to '₈', '9' to '₉',
        '+' to '₊', '-' to '₋', '=' to '₌', '(' to '₍', ')' to '₎'
    )
    return text.map { subscripts[it] ?: it }.joinToString("")
}